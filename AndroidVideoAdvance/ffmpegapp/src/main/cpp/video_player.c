//
// Created by syye on 2020/3/16.
//
#include <jni.h>

#include "common.h"

//原生组件支持
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <unistd.h>

#include "libavformat/avformat.h"
#include "libavutil/imgutils.h"
#include "libswscale/swscale.h"


long duration;
float play_rate = 1;//播放速度

VIDEO_PLAYER_FUNC(jint, play, jstring filePath, jobject surface) {
    const char *file_name = (*env)->GetStringUTFChars(env, filePath, JNI_FALSE);
    ALOGI("file path:%s", file_name);
    av_register_all();
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    int open_result = avformat_open_input(&pFormatCtx, file_name, NULL, NULL);
    if (open_result != 0) {
        ALOGE("Couldn't open file:%s", file_name);
        return -1;
    }
    int find_stream_result = avformat_find_stream_info(pFormatCtx, NULL);
    if (find_stream_result < 0) {
        ALOGE("Couldn't find stream info!!")
        return -2;
    }
    int video_stream = -1, i;
    for (i = 0; i < pFormatCtx->nb_streams; ++i) {
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO && video_stream < 0) {
            video_stream = i;
        }
    }
    if (video_stream == -1) {
        ALOGE("couldn't find a video stream!!!")
        return -3;
    }
    if (pFormatCtx->duration != AV_NOPTS_VALUE) {
        duration = pFormatCtx->duration / AV_TIME_BASE;
        ALOGI("video duration:%ld", duration);
    }
    AVCodecContext *pCodecCtx = pFormatCtx->streams[video_stream]->codec;
    AVCodec *pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
    if (pCodec == NULL) {
        ALOGE("couldn't find codec!!!")
        return -4;
    }
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        ALOGE("couldn't open codec")
        return -4;
    }
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    //获取视频宽高
    int video_width = pCodecCtx->width;
    int video_height = pCodecCtx->height;
    //设置native window的buffer大小,可自动拉伸
    ANativeWindow_setBuffersGeometry(nativeWindow, video_width, video_height,
                                     WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer windowBuffer;
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        ALOGE("Couldn't open codec")
        return -4;
    }
    AVFrame *pFrame = av_frame_alloc();
    AVFrame *pFrameRGBA = av_frame_alloc();
    if (pFrame == NULL || pFrameRGBA == NULL) {
        ALOGE("couldn't allocate video frame!!");
        return -4;
    }

    int numBytes = av_image_get_buffer_size(AV_PIX_FMT_RGBA,
                                            pCodecCtx->width,
                                            pCodecCtx->height,
                                            1);
    uint8_t *buffer = av_malloc(numBytes * sizeof(uint8_t));
    av_image_fill_arrays(pFrameRGBA->data, pFrameRGBA->linesize, buffer, AV_PIX_FMT_RGBA,
                         pCodecCtx->width, pCodecCtx->height, 1);

    //格式转为RGBA
    struct SwsContext *sws_ctx = sws_getContext(pCodecCtx->width,
                                                pCodecCtx->height,
                                                pCodecCtx->pix_fmt,
                                                pCodecCtx->width,
                                                pCodecCtx->height,
                                                AV_PIX_FMT_RGBA,
                                                SWS_BILINEAR,
                                                NULL,
                                                NULL,
                                                NULL);
    int frameFinished;
    AVPacket packet;
    while (av_read_frame(pFormatCtx, &packet) >= 0) {
        if (packet.stream_index == video_stream) {
            avcodec_decode_video2(pCodecCtx, pFrame, &frameFinished, &packet);
            if (frameFinished) {
                // lock native window
                ANativeWindow_lock(nativeWindow, &windowBuffer, 0);
                sws_scale(sws_ctx, (uint8_t const *const *) pFrame->data, pFrame->linesize, 0,
                          pCodecCtx->height,
                          pFrameRGBA->data, pFrameRGBA->linesize);
                uint8_t *dst = windowBuffer.bits;
                int dstStride = windowBuffer.stride * 4;
                uint8_t *src = pFrameRGBA->data[0];
                int srcStride = pFrameRGBA->linesize[0];
                // 由于window的stride和帧的stride不同,因此需要逐行复制
                for (int h = 0; h < video_height; h++) {
                    memcpy(dst + h * dstStride, src + h * srcStride, (size_t) srcStride);
                }
//                uint8_t *dst = windowBuffer.bits;
//                int dstStride = windowBuffer.stride * 4;
//                uint8_t *src = pFrameRGBA->data[0];
//                int srcStride = pFrameRGBA->linesize[0];
//                // 由于window的stride和帧的stride不同,因此需要逐行复制
//                int h;
//                for (h = 0; h < video_height; h++) {
//                    memcpy(dst + h * dstStride, src + h * srcStride, (size_t) srcStride);
//                }
                ANativeWindow_unlockAndPost(nativeWindow);
            }
            //延迟等待
            usleep((unsigned long) (1000 * 40 * play_rate));
        }
        av_packet_unref(&packet);
    }
    //释放一切
    av_free(buffer);
    av_free(pFrame);
    av_free(pFrameRGBA);
    avcodec_close(pCodecCtx);
    avformat_close_input(&pFormatCtx);
    return 0;
}

//设置播放速率
VIDEO_PLAYER_FUNC(void, setPlayRate, jfloat playRate) {
    play_rate = playRate;
}

//获取视频总时长
VIDEO_PLAYER_FUNC(jint, getDuration) {
    return duration;
}
