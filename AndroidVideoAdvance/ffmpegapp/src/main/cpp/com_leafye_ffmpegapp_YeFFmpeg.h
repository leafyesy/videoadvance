/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_leafye_ffmpegapp_YeFFmpeg */

#ifndef _Included_com_leafye_ffmpegapp_YeFFmpeg
#define _Included_com_leafye_ffmpegapp_YeFFmpeg
#ifdef __cplusplus
extern "C" {


#endif
/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    decode
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_decode
        (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    avfilterinfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_avfilterinfo
        (JNIEnv *, jobject);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    avcodecinfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_avcodecinfo
        (JNIEnv *, jobject);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    avformatinfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_avformatinfo
        (JNIEnv *, jobject);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    urlprotocolinfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_urlprotocolinfo
        (JNIEnv *, jobject);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    handle
 * Signature:(jobjectArray)Ljava/lang/int
 */
JNIEXPORT jint JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_handle
        (JNIEnv *, jclass, jobjectArray);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    ffmpegplay
 * Signature:(jstring)Ljava/lang/int
 */
JNIEXPORT jint JNICALL Java_com_leafye_ffmpegapp_YeFFmpeg_ffmpegPlay
        (JNIEnv *, jobject, jstring);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    openslPlayer
 * Signature:(jstring)Ljava/lang/int
 */
JNIEXPORT jint JNICALL
Java_com_leafye_ffmpegapp_YeFFmpeg_openslPlayer(JNIEnv *, jobject, jstring);

/*
 * Class:     com_leafye_ffmpegapp_YeFFmpeg
 * Method:    openslPlayer
 * Signature:()Ljava/lang/int
 */
JNIEXPORT jint JNICALL
Java_com_leafye_ffmpegapp_YeFFmpeg_stopOpenslPlayer(JNIEnv *env, jobject thiz);

#ifdef __cplusplus
}
#endif
#endif
