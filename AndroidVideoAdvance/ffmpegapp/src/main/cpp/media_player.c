//
// Created by syye on 2020/3/18.
//
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libswresample/swresample.h"
#include "AVpacket_queue.h"
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <stdio.h>
#include <unistd.h>
#include <libavutil/imgutils.h>
#include <android/log.h>
#include <pthread.h>
#include <jni.h>
#include <libavutil/time.h>
#include "common.h"

#define TAG "MediaPlayer"

#define MAX_AUDIO_FRAME_SIZE 48000 * 4
#define PACKET_SIZE 50
#define MIN_SLEEP_TIME_US 1000ll
#define AUDIO_TIME_ADJUST_US -200000ll

#define LOGE ALOGE
#define LOGI ALOGI

#define ERROR_FILE_OPEN_ERROR -1
#define ERROR_FIND_STREAM -2
#define ERROR_FIND_VIDEO_STREAM -3
#define ERROR_FIND_AUDIO_STREAM -4
#define ERROR_FIND_VIDEO_CODEC -5
#define ERROR_OPEN_VIDEO_CODEC -6
#define ERROR_FIND_AUDIO_CODEC -7
#define ERROR_OPEN_AUDIO_CODEC -8
#define ERROR_ALLOCATE_VIDEO_FRAME -9
#define ERROR_MALLOC_PLAYER -10
#define ERROR_DECODE_VIDEO2 -10000000

//当前时间
struct timeval now;
struct timespec timeout;
/**
 * 播放器
 */
typedef struct MediaPlayer {
    //音视频文件上下文
    AVFormatContext *format_context;
    //视频文件流的位置
    int video_stream_index;
    //音频文件流的位置
    int audio_stream_index;
    //视频编解码器上下文
    AVCodecContext *video_codec_context;
    //音频编解码器上下文
    AVCodecContext *audio_codec_context;

    //视频编解码器
    AVCodec *video_codec;
    //音频编解码器
    AVCodec *audio_codec;

    //Android window
    ANativeWindow *native_window;

    //缓存
    uint8_t *buffer;

    //视频原始数据 yuv
    AVFrame *yuv_frame;
    //视频原始数据 rgba
    AVFrame *rgba_frame;

    //视频宽
    int video_width;
    //视频高
    int video_height;

    //重采样 上下文
    SwrContext *swr_context;

    //输出的channel
    int out_channel_nb;
    //输出的采样率
    int out_sample_rate;
    //输出的采样格式
    enum AVSampleFormat out_sample_fmt;
    //音频播放器对象 使用AudioTrack
    jobject audio_track;
    //AudioTrack写入的java方法对象
    jmethodID audio_track_write_mid;
    //音频缓存
    uint8_t *audio_buffer;
    //音频原始数据
    AVFrame *audio_frame;
    //
    AVPacketQueue *packets[2];
    //线程互斥锁
    pthread_mutex_t mutex;
    //线程条件变量
    pthread_cond_t cond;
    //开始时间
    int64_t start_time;
    //音频时钟
    int64_t audio_clock;
    //写入线程
    pthread_t write_thread;
    //视频线程
    pthread_t video_thread;
    //音频线程
    pthread_t audio_thread;

} MediaPlayer;

/**
 * 解码器
 */
typedef struct Decoder {
    MediaPlayer *player;
    int stream_index;
} Decoder;
//java虚拟机对象
JavaVM *javaVm;

MediaPlayer *player;

jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVm = vm;
    return JNI_VERSION_1_6;
}

//初始化输入文件
//1.注册所有的组件
//2.创建formatContext
//3.打开输入的文件
//4.寻找流文件
//5.定位音频/视频流
int init_input_format_context(MediaPlayer *player, const char *file_name) {
    av_register_all();
    player->format_context = avformat_alloc_context();
    if (avformat_open_input(&player->format_context, file_name, NULL, NULL) != 0) {
        LOGE(TAG, "Couldn't not open file:%s\n", file_name);
        return ERROR_FILE_OPEN_ERROR;
    }
    if (avformat_find_stream_info(player->format_context, NULL) < 0) {
        LOGE(TAG, "Couldn't find stream information.\n")
        return ERROR_FIND_STREAM;
    }
    int i;
    player->video_stream_index = -1;
    player->audio_stream_index = -1;
    for (i = 0; i < player->format_context->nb_streams; ++i) {
        if (player->format_context->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO
            && player->video_stream_index < 0) {
            player->video_stream_index = i;
        } else if (player->format_context->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO
                   && player->audio_stream_index < 0) {
            player->audio_stream_index = i;
        }
    }
    if (player->video_stream_index == -1) {
        LOGE(TAG, "Couldn't find video stream.\n")
        return ERROR_FIND_VIDEO_STREAM;
    }
    if (player->audio_stream_index == -1) {
        LOGE(TAG, "Couldn't find audio stream.\n")
        return ERROR_FIND_AUDIO_STREAM;
    }
    LOGI(TAG, "video stream index:%d  audio stream index:%d",
         player->video_stream_index,
         player->audio_stream_index);
    return 0;
}

//音视频解码器
//一.视频
//1.从视频流中获取编解码器
//2.根据获取的视频编解码器寻找解码器对象
//3.尝试打开视频解码器
//二.音频
//1.从音频流中获取编解码器
//2.根据获取的音频编解码器寻找解码器对象
//3.尝试打开音频解码器
//三.other
//视频的宽高
int init_condec_context(MediaPlayer *player) {
    player->video_codec_context = player->format_context->streams[player->video_stream_index]->codec;
    //寻找视频流的解码器
    player->video_codec = avcodec_find_decoder(player->video_codec_context->codec_id);
    if (player->video_codec == NULL) {
        LOGE("Couldn't find video Codec.\n");
        return ERROR_FIND_VIDEO_CODEC;
    }
    if (avcodec_open2(player->video_codec_context, player->video_codec, NULL) < 0) {
        LOGE("Couldn't open video Codec.\n");
        return ERROR_OPEN_VIDEO_CODEC;
    }
    player->audio_codec_context = player->format_context->streams[player->audio_stream_index]->codec;
    player->audio_codec = avcodec_find_decoder(player->audio_codec_context->codec_id);
    if (player->audio_codec == NULL) {
        LOGE(TAG, "Couldn't find audio Codec.\n");
        return ERROR_FIND_AUDIO_CODEC;
    }
    if (avcodec_open2(player->audio_codec_context, player->audio_codec, NULL) < 0) {
        LOGE(TAG, "Couldn't open audio Codec.\n");
        return ERROR_OPEN_AUDIO_CODEC;
    }
    // 获取视频宽高
    player->video_width = player->video_codec_context->width;
    player->video_height = player->video_codec_context->height;
    LOGI(TAG, "video width:%d  height:%d", player->video_width, player->video_height);
    return 0;
}

//初始化ANativeWindow
void video_player_prepare(MediaPlayer *player, JNIEnv *env, jobject surface) {
    player->native_window = ANativeWindow_fromSurface(env, surface);
}

//获取播放时长
int64_t get_play_time(MediaPlayer *player) {
    return av_gettime() - player->start_time;
}

//音视频同步
//就是音频快了 等视频 视频快了 等音频
//1.加锁
//2.开启循环
//  1.获取视频播放的时间
//  2.根据传入的时间确定休眠时间??
//  3.如果时间小于300ms
void player_wait_for_frame(MediaPlayer *player, int64_t stream_time) {
    pthread_mutex_lock(&player->mutex);
    for (;;) {
        int64_t current_video_time = get_play_time(player);
        int64_t sleep_time = stream_time - current_video_time;
        if (sleep_time < -300000ll) {//300ms
            int64_t new_value = player->start_time - sleep_time;
            player->start_time = new_value;
            pthread_cond_broadcast(&player->cond);
        }
        if (sleep_time <= MIN_SLEEP_TIME_US) {
            break;
        }
        if (sleep_time > 500000ll) {
            sleep_time = 500000ll;
        }
        gettimeofday(&now, NULL);
        timeout.tv_sec = now.tv_sec;
        timeout.tv_nsec = (now.tv_usec + sleep_time) * 1000;
        pthread_cond_timedwait(&player->cond, &player->mutex, &timeout);
    }
    pthread_mutex_unlock(&player->mutex);
}

//视频解码
//1.初始化yuv/rgba frame
//2.获取缓存的大小 并 初始化缓存
//3.填充AVFrame数据缓冲
//4.获取转换的上下文
//5.解码视频文件
//6.显示
int decode_video(MediaPlayer *player, AVPacket *packet) {
    ANativeWindow_setBuffersGeometry(player->native_window,
                                     player->video_width,
                                     player->video_height,
                                     WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer window_buffer;
    player->yuv_frame = av_frame_alloc();
    player->rgba_frame = av_frame_alloc();
    if (player->rgba_frame == NULL || player->yuv_frame == NULL) {
        LOGE("Couldn't allocate video frame.\n");
        return ERROR_ALLOCATE_VIDEO_FRAME;
    }
    int num_bytes = av_image_get_buffer_size(AV_PIX_FMT_RGBA,
                                             player->video_width,
                                             player->video_height,
                                             1);
    player->buffer = av_malloc(num_bytes * sizeof(uint8_t));

    av_image_fill_arrays(player->rgba_frame->data,
                         player->rgba_frame->linesize,
                         player->buffer,
                         AV_PIX_FMT_RGBA,
                         player->video_width, player->video_height,
                         1);
    struct SwsContext *sws_ctx = sws_getContext(
            player->video_width,
            player->video_height,
            player->video_codec_context->pix_fmt,
            player->video_width,
            player->video_height,
            AV_PIX_FMT_RGBA,
            SWS_BILINEAR,
            NULL,
            NULL,
            NULL
    );
    int frame_finished;
    int ret = avcodec_decode_video2(player->video_codec_context, player->yuv_frame, &frame_finished,
                                    packet);
    if (ret < 0) {
        LOGE("avcodec decode video2 error... ret:%d", ret);
        return ERROR_DECODE_VIDEO2 + ret;
    }
    //6.显示
    if (frame_finished) {
        //1.锁定显示
        ANativeWindow_lock(player->native_window, &window_buffer, 0);
        //2.转换一帧图像 yuv->rgba
        sws_scale(sws_ctx, player->yuv_frame->data, player->yuv_frame->linesize, 0,
                  player->video_height, player->rgba_frame->data, player->rgba_frame->linesize);

        uint8_t *dst = window_buffer.bits;//目标数据地址
        int dstStride = window_buffer.stride * 4;//步长
        uint8_t *src = player->rgba_frame->data[0];//源数据数组头地址
        int srcStride = player->rgba_frame->linesize[0];//For video, size in bytes of each picture line.
        //拷贝
        for (int h = 0; h < player->video_height; ++h) {
            //memcpy c/c++中使用的内存拷贝函数
            memcpy(dst + h * dstStride, src + h * srcStride, (size_t) srcStride);
        }
        //计算延迟
        //获取 AVFrame中的best_effort_timestamp
        int64_t pts = av_frame_get_best_effort_timestamp(player->yuv_frame);
        AVStream *stream = player->format_context->streams[player->video_stream_index];
        //转换（不同时间基时间转换）
        int64_t time = av_rescale_q(pts, stream->time_base, AV_TIME_BASE_Q);
        //音视频同步
        player_wait_for_frame(player, time);

        ANativeWindow_unlockAndPost(player->native_window);
    }
    return 0;
}

/**
 *
 * 音频解码准备
 */
void audio_decoder_prepare(MediaPlayer *player) {
    player->swr_context = swr_alloc();
    //根据MediaPlayer中存储的数据获取输入和输出的音频各种参数
    enum AVSampleFormat in_sample_fmt = player->audio_codec_context->sample_fmt;
    //输出采样格式16bit PCM
    player->out_sample_fmt = AV_SAMPLE_FMT_S16;
    //输入采样率
    int in_sample_rate = player->audio_codec_context->sample_rate;
    //输出采样率
    player->out_sample_rate = in_sample_rate;
    //声道布局（2个声道，默认立体声stereo）
    uint64_t in_ch_layout = player->audio_codec_context->channel_layout;
    //输出的声道布局（立体声）
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;
    //设置参数
    swr_alloc_set_opts(player->swr_context,
                       out_ch_layout, player->out_sample_fmt, player->out_sample_rate,
                       in_ch_layout, in_sample_fmt, in_sample_rate, 0, NULL);
    //初始化
    swr_init(player->swr_context);
    //修改player中的channel
    player->out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
}

/**
 * 音频播放准备
 * 主要是准备AudioTrack和write方法
 * @param player
 * @param env
 * @param jthiz
 * @return
 */
int audio_player_prepare(MediaPlayer *player, JNIEnv *env, jclass jthiz) {
    jclass player_class = (*env)->GetObjectClass(env, jthiz);
    if (!player_class) {
        LOGE("player class not found");
        return -1;
    }
    //AudioTrack对象
    jmethodID audio_track_method = (*env)->GetMethodID(
            env, player_class, "createAudioTrack", "(II)Landroid/media/AudioTrack;");
    if (!audio_track_method) {
        LOGE("audio track method not found ...");
        return -2;
    }
    jobject audio_track = (*env)->CallObjectMethod(
            env, jthiz, audio_track_method, player->out_sample_rate, player->out_channel_nb);
    jclass audio_track_class = (*env)->GetObjectClass(env, audio_track);
    jmethodID audio_track_play_mid = (*env)->GetMethodID(env, audio_track_class, "play", "()V");
    (*env)->CallVoidMethod(env, audio_track, audio_track_play_mid);

    player->audio_track = (*env)->NewGlobalRef(env, audio_track);
    player->audio_track_write_mid = (*env)->GetMethodID(env, audio_track_class, "write", "([BII)I");
    player->audio_buffer = av_malloc(MAX_AUDIO_FRAME_SIZE);
    player->audio_frame = av_frame_alloc();
}

/**
 * 解码audio
 * @param player
 * @param packet
 * @return
 */
int decode_audio(MediaPlayer *player, AVPacket *packet) {
    int got_frame = 0, ret;
    ret = avcodec_decode_audio4(player->audio_codec_context,
                                player->audio_frame,
                                &got_frame,
                                packet);
    if (ret < 0) {
        LOGE("avcodec decode audio4 error.\n");
        return -1;
    }
    if (got_frame > 0) {
        //转换格式
        swr_convert(player->swr_context, &player->audio_buffer,
                    MAX_AUDIO_FRAME_SIZE,
                    (const uint8_t **) player->audio_frame->data,
                    player->audio_frame->nb_samples);
        //获取缓存size
        int out_buffer_size = av_samples_get_buffer_size(NULL, player->out_channel_nb,
                                                         player->audio_frame->nb_samples,
                                                         player->out_sample_fmt, 1);
        //音视频同步
        int64_t pts = packet->pts;
        if (pts != AV_NOPTS_VALUE) {
            AVStream *stream = player->format_context->streams[player->audio_stream_index];
            player->audio_clock = av_rescale_q(pts, stream->time_base, AV_TIME_BASE_Q);
            player_wait_for_frame(player, player->audio_clock + AUDIO_TIME_ADJUST_US);
        }
        //组装数据 并 写入AudioTrack中
        if (javaVm != NULL) {
            JNIEnv *env;
            (*javaVm)->AttachCurrentThread(javaVm, &env, NULL);
            jbyteArray audio_sample_array = (*env)->NewByteArray(env, out_buffer_size);
            jbyte *sample_byte_array = (*env)->GetByteArrayElements(env, audio_sample_array, NULL);
            memcpy(sample_byte_array, player->audio_buffer, (size_t) out_buffer_size);
            (*env)->ReleaseByteArrayElements(env, audio_sample_array, sample_byte_array, 0);
            (*env)->CallIntMethod(env, player->audio_track, player->audio_track_write_mid,
                                  audio_sample_array, 0, out_buffer_size);
            (*env)->DeleteLocalRef(env, audio_sample_array);
        }
    }
    if (javaVm != NULL) {
        (*javaVm)->DetachCurrentThread(javaVm);
    }
    return 0;
}

//初始化queue
void init_queue(MediaPlayer *player, int size) {
    for (int i = 0; i < 2; ++i) {
        AVPacketQueue *queue = queue_init(size);
        player->packets[i] = queue;
    }
}

//删除queue
void delete_queue(MediaPlayer *player) {
    for (int i = 0; i < 2; ++i) {
        queue_free(player->packets[i]);
    }
}

//读取AVPacket线程(生产者)
void *write_packet_to_queue(void *arg) {
    MediaPlayer *player = (MediaPlayer *) arg;
    AVPacket packet, *pkt = &packet;
    int ret;
    for (;;) {
        //读取一个frame数据
        ret = av_read_frame(player->format_context, pkt);
        if (ret < 0) {
            break;
        }
        //判断数据为视频/音频
        if (pkt->stream_index == player->video_stream_index ||
            pkt->stream_index == player->audio_stream_index) {
            //根据AVPacket->stream_index获取对应的队列
            AVPacketQueue *queue = player->packets[pkt->stream_index];
            pthread_mutex_lock(&player->mutex);
            //得到要被推入的内存指针
            AVPacket *data = queue_push(queue, &player->mutex, &player->cond);
            pthread_mutex_unlock(&player->mutex);
            //拷贝（间接赋值，拷贝结构体数据）
            *data = packet;
        }
    }
}

//音视频解码线程
void *decode_func(void *arg) {
    Decoder *decoder_data = (Decoder *) arg;
    MediaPlayer *player = decoder_data->player;
    int stream_index = decoder_data->stream_index;
    AVPacketQueue *queue = player->packets[stream_index];
    int ret = 0;
    for (;;) {
        pthread_mutex_lock(&player->mutex);
        //得到pop出来的内存指针
        AVPacket *packet = (AVPacket *) queue_pop(queue, &player->mutex, &player->cond);
        pthread_mutex_unlock(&player->mutex);
        //根据stream_index确定解码的类型
        if (stream_index == player->video_stream_index) {
            ret = decode_video(player, packet);
        } else if (stream_index == player->audio_stream_index) {
            ret = decode_audio(player, packet);
        }
        //释放已经被解码的地址
        av_packet_unref(packet);
        if (ret < 0) {
            break;
        }
    }
}

MEDIA_PLAYER_FUNC(jint, setupMedia, jstring filePath, jobject surface) {
    const char *file_name = (*env)->GetStringUTFChars(env, filePath, JNI_FALSE);
    int ret;
    player = malloc(sizeof(MediaPlayer));
    if (player == NULL) {
        LOGE("Couldn't malloc player mem.\n")
        return ERROR_MALLOC_PLAYER;
    }
    ret = init_input_format_context(player, file_name);
    if (ret < 0) {
        return ret;
    }
    ret = init_condec_context(player);
    if (ret < 0) {
        return ret;
    }
    video_player_prepare(player, env, surface);
    audio_decoder_prepare(player);
    audio_player_prepare(player, env, thiz);
    init_queue(player, PACKET_SIZE);
    return 0;
}


MEDIA_PLAYER_FUNC(jint, playMedia) {
    pthread_mutex_init(&player->mutex, NULL);
    pthread_cond_init(&player->cond, NULL);
    pthread_create(&player->write_thread, NULL, write_packet_to_queue, (void *) player);
    sleep(1);
    player->start_time = 0;
    //创建一个decoder data1 并赋值给*decoder_data1
    Decoder data1 = {player, player->video_stream_index}, *decoder_data1 = &data1;
    pthread_create(&player->video_thread, NULL, decode_func, (void *) decoder_data1);
    Decoder data2 = {player, player->audio_stream_index}, *decoder_data2 = &data2;
    pthread_create(&player->audio_thread, NULL, decode_func, (void *) decoder_data2);
    pthread_join(player->write_thread, NULL);
    pthread_join(player->video_thread, NULL);
    pthread_join(player->audio_thread, NULL);

    return 0;
}

MEDIA_PLAYER_FUNC(void, releaseMedia) {
    //释放内存以及关闭文件
    free(player->audio_track);
    free(player->audio_track_write_mid);
    av_free(player->buffer);
    av_free(player->rgba_frame);
    av_free(player->yuv_frame);
    av_free(player->audio_buffer);
    av_free(player->audio_frame);
    avcodec_close(player->video_codec_context);
    avcodec_close(player->audio_codec_context);
    avformat_close_input(&player->format_context);
    ANativeWindow_release(player->native_window);
    delete_queue(player);
    pthread_cond_destroy(&player->cond);
    pthread_mutex_destroy(&player->mutex);
    free(player);
    (*javaVm)->DestroyJavaVM(javaVm);
}
























