//
// Created by syye on 2020/2/21.
//

#include <jni.h>
#include <string>
#include <android/log.h>
#include "com_leafye_ffmpegapp_YeFFmpeg.h"
#include "include/libavutil/avutil.h"
#include "include/libavcodec/avcodec.h"

extern "C" {//按照C进行编译
//编码
#include "include/libavcodec/avcodec.h"
//format
#include "include/libavformat/avformat.h"
#include "include/libswscale/swscale.h"
#include "include/libavfilter/avfilter.h"
}

#define ALOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"ffmpeg",FORMAT,##__VA_ARGS__);
#define ALOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"ffmpeg",FORMAT,##__VA_ARGS__);

extern "C"
jstring stringFromJNI(JNIEnv * env, jobject) {
    return env->NewStringUTF(avcodec_configuration());
}

JNIEXPORT void JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_decode
(JNIEnv *env, jobject type, jstring input_, jstring output_){
const char *input = env->GetStringUTFChars(input_,0);
const char *output = env->GetStringUTFChars(output_,0);
int ret;
//1.注册所有组件
av_register_all();
//封装格式上下文(format_context)
AVFormatContext *pFormatCtx = avformat_alloc_context();
//2.打开输入视频文件
ret = avformat_open_input(&pFormatCtx,input,NULL,NULL);
if(ret!=0){
ALOGE("%s%d","无法打开输入视频文件,code:",ret);
return;
}
//3.获取视频文件信息
ret = avformat_find_stream_info(pFormatCtx,NULL);
if(ret<0){
ALOGE("%s%d","寻找视频流信息失败,code:",ret);
return;
}
int v_stream_idx = -1;
int i = 0;
for(;i<pFormatCtx->nb_streams;i++){
if(pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO){
v_stream_idx = i;
break;
}
}
if(v_stream_idx == -1){
ALOGE("%s","找不到视频流");
return;
}
//根据找到的视频编码方式,去找编码器
AVCodecContext *pCodecCtx = pFormatCtx->streams[v_stream_idx]->codec;
//4.根据编码器上下文中的编码id查找对应的解码
AVCodec *pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
if(pCodec == NULL){
ALOGE("%s","找不到解码器");
return;
}
//
ALOGI("视频的文件格式：%s",pFormatCtx->iformat->name);
ALOGI("视频时长：%d", (pFormatCtx->duration)/1000000);
ALOGI("视频的宽高：%d,%d",pCodecCtx->width,pCodecCtx->height);
ALOGI("解码器的名称：%s",pCodec->name);

//准备读取
//AVPacket用于存储一帧一帧的压缩数据(h264)
AVPacket *packet = (AVPacket)av_malloc(sizeof(AVPacket));
//AVFrame用于存储解码后的像素数据(YUV)
AVFrame *pFrame = av_frame_alloc();
AVFrame *pFrameYUV = av_frame_alloc();
uint8_t *out_buffer = (uint8_t *)av_malloc(avpicture_get_size(AV_PIX_FMT_YUV420P,pCodecCtx->width,pCodecCtx->height));
avpicture_fill((AVPicture *)pFrameYUV,out_buffer,AV_PIX_FMT_YUV420P,pCodecCtx->width,pCodecCtx->height);
//用于转码(缩放)的参数
struct SwsContext *sws_getContext (pCodecCtx->width,pCodecCtx->height,pCodecCtx->pix_fmt,
                                   pCodecCtx->width,pCodecCtx->height,AV_PIX_FMT_YUV420P,
                                   SWS_BICUBIC,NULL,NULL,NULL);
int got_picture;
FILE *fp_yuv = fopen(output,"wb+");
int frame_count = 0;
//6.一帧一帧读取压缩数据
while(av_read_frame(pFrameCtx,packet)>0){
//只要视频压缩数据,得到视频像素数据
ret = avcodec_decode_video2(pCodecCtx,pFrame,&got_picture,packet);
if(ret<0){
ALOGE("%s","解码错误");
return;
}
if(got_picture){
//AVFrame转为像素格式YUV420,宽高
//2 6输入/输出数据
//3 7输入/输出画面一行的数据大小 AVFrame转换是一行一行转换的
//4 输入数据第一列要转码的位置 从0开始
//5 输入画面的高度
sws_scale(sws_ctx,pFrame->data,pFrame->linesize,0,pCodecCtx->height,pFrameYUV->data,pFrameYUV->linesize);
//输出到YUV文件
//AVFrame像素帧写入文件
//data解码后的图像像素数据(音频采样数据)
//Y 亮度 UV色度(压缩) 人对亮度比较敏感
//UV个数是Y的1/4
int y_size = pCodecCtx->width * pCodecCtx->height;
fwrite(pFrameYUV->data[0],1,y_size,fp_yuv);
fwrite(pFrameYUV->data[1],1,y_size/4,fp_yuv);
fweite(pFrameYUV->data[2],1,y_size/4,fp_yuv);
frame_count++;
ALOGI("解码第%d帧",frame_count);
}
av_free_packet(packet);
}
fclose(fp_yuv);
av_frame_free(&pFrame);
avcodec_close(pCodecCtx);
avformat_free_context(pFormatCtx);
env->ReleaseStringUTFChars(input_,input);
env->ReleaseStringUTFChars(output_,output);
}