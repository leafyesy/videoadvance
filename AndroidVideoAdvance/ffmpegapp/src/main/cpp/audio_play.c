//
// Created by syye on 2020/3/10.
//

#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include "libswresample/swresample.h"
#include "libavformat/avformat.h"

#include "common.h"

#define MAX_AUDIO_FRAME_SIZE 48000 * 4

/**
 * 执行过程
 * 1.注册所有组件
 * 2.打开音频文件
 * 3.获取文件信息
 * 4.获取音频流索引位置
 * 5.获取并打开解码器
 * 6.准备AVPacket和AVFrame 以及获取 输入的sampleFormat/sampleRate/ChannelLayout等数据
 * 7.创建AudioTrack对象
 * 8.调用AudioTrack的play方法
 * 9.不断读取数据并使用AudioTrack对象的write方法进行写入
 * 10.释放所有资源和内存
 * @param env
 * @param obj
 * @param input_str
 * @return -1 文件无法打开 -2 读取文件信息失败 -3 获取解码器失败 -4 打开解码器失败
 */
JNIEXPORT jint JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_ffmpegPlay(
        JNIEnv *env, jobject obj, jstring input_str
) {
    const char *input_cstr = (*env)->GetStringUTFChars(env, input_str, NULL);
    ALOGE("input str:%s", input_cstr)
    av_register_all();
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    //打开音频文件
    if (avformat_open_input(&pFormatCtx, input_cstr, NULL, NULL) != 0) {
        ALOGE("%s", "无法打开音频文件")
        return -1;
    }
    //文件信息
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        ALOGE("%s", "获取文件信息失败")
        return -2;
    }
    //获取音频流索引位置
    int i = 0, audio_stream_idx = -1;
    for (; i < pFormatCtx->nb_streams; ++i) {
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_idx = i;
            break;
        }
    }
    //获取音频解码器
    AVCodecContext *codecCtx = pFormatCtx->streams[audio_stream_idx]->codec;
    AVCodec *codec = avcodec_find_decoder(codecCtx->codec_id);
    if (NULL == codec) {
        ALOGE("%s", "无法获取解码器")
        return -3;
    }
    //打开解码器
    if (avcodec_open2(codecCtx, codec, NULL) < 0) {
        ALOGE("%s", "无法打开解码器")
        return -4;
    }
    //压缩数据
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
    //解压缩数据
    AVFrame *frame = av_frame_alloc();

    SwrContext *swrCtx = swr_alloc();
    //输入的采样格式
    enum AVSampleFormat in_sample_fmt = codecCtx->sample_fmt;
    //输出格式采样16bit pcm
    enum AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
    //输入的采样率
    int in_sample_rate = codecCtx->sample_rate;
    //输出的采样率
    int out_sample_rate = in_sample_rate;
    //声道布局
    uint64_t in_ch_layout = codecCtx->channel_layout;
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;

    swr_alloc_set_opts(swrCtx,
                       out_ch_layout, out_sample_fmt, out_sample_rate,
                       in_ch_layout, in_sample_fmt, in_sample_rate,
                       0, NULL);
    swr_init(swrCtx);
    int out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
    jclass player_class = (*env)->GetObjectClass(env, obj);
    if (!player_class) {
        ALOGE("%s", "player class not fount");
        return -5;
    }
    //AudioTrack对象
    jmethodID audio_track_create_method = (*env)->GetMethodID(env, player_class, "createAudioTrack",
                                                              "(II)Landroid/media/AudioTrack;");
    if (!audio_track_create_method) {
        ALOGE("%s", "method createAudioTrack not fount")
        return -5;
    }
    jobject audio_track = (*env)->CallObjectMethod(env, obj, audio_track_create_method,
                                                   out_sample_rate,
                                                   out_channel_nb);


    //调用play方法
    jclass audio_track_class = (*env)->GetObjectClass(env, audio_track);
    jmethodID audio_track_play_mid = (*env)->GetMethodID(env, audio_track_class, "play", "()V");
    (*env)->CallVoidMethod(env, audio_track, audio_track_play_mid);

    //获取write方法
    jmethodID audio_track_write_mid = (*env)->GetMethodID(env, audio_track_class, "write",
                                                          "([BII)I");
    uint8_t *out_buffer = (uint8_t *) av_malloc(MAX_AUDIO_FRAME_SIZE);
    int got_frame = 0, index = 0, ret;
    //不断读取编码数据
    while (av_read_frame(pFormatCtx, packet) >= 0) {
        if (packet->stream_index == audio_stream_idx) {
            //解码
            ret = avcodec_decode_audio4(codecCtx, frame, &got_frame, packet);
            if (ret < 0) {
                break;
            }
            if (got_frame > 0) {
                ALOGI("decode frame count=%d", index++);
                //音频格式转换
                swr_convert(swrCtx, &out_buffer, MAX_AUDIO_FRAME_SIZE,
                            (const uint8_t **) frame->data,
                            frame->nb_samples);
                int out_buffer_size
                        = av_samples_get_buffer_size(NULL,
                                                     out_channel_nb,
                                                     frame->nb_samples,
                                                     out_sample_fmt,
                                                     1);
                jbyteArray audio_sample_array
                        = (*env)->NewByteArray(env, out_buffer_size);
                jbyte *sample_byte_array
                        = (*env)->GetByteArrayElements(env,
                                                       audio_sample_array,
                                                       NULL);
                //拷贝缓冲数据
                memcpy(sample_byte_array, out_buffer, (size_t) out_buffer_size);
                //释放数组
                (*env)->ReleaseByteArrayElements(env,
                                                 audio_sample_array,
                                                 sample_byte_array,
                                                 0);
                //调用write方法进行播放
                (*env)->CallIntMethod(env,
                                      audio_track,
                                      audio_track_write_mid,
                                      audio_sample_array,
                                      0,
                                      out_buffer_size);
                //释放局部引用
                (*env)->DeleteLocalRef(env, audio_sample_array);
                usleep(1000 * 8);
            }
        }
        av_free_packet(packet);
    }
    ALOGI("%s", "decode audio finish!");
    av_frame_free(&frame);
    av_free(out_buffer);
    swr_free(&swrCtx);
    avcodec_close(codecCtx);
    avformat_close_input(&pFormatCtx);
    (*env)->ReleaseStringChars(env, input_str, input_cstr);




    return 0;
}