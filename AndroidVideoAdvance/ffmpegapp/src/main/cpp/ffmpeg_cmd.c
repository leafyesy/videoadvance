//
// Created by syye on 2020/3/10.
//

#include <jni.h>
#include <android/log.h>
#include "ffmpeg/ffmpeg.h"
#include "common.h"

JNIEXPORT jint JNICALL
Java_com_leafye_ffmpegapp_YeFFmpeg_handle
        (JNIEnv *env, jclass obj, jobjectArray cmdArr) {
    ALOGI("handle cmd start");
    int argc = (*env)->GetArrayLength(env, cmdArr);
    ALOGI("handle cmd 1");
    char **argv = (char **) malloc(argc * sizeof(char *));
    ALOGI("handle cmd 2");
    int i;
    int result;
    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env)->GetObjectArrayElement(env, cmdArr, i);
        char *temp = (char *) (*env)->GetStringUTFChars(env, jstr, 0);
        ALOGI("handle cmd %s  %d", temp, i);
        argv[i] = malloc(1024);
        strcpy(argv[i], temp);
        (*env)->ReleaseStringUTFChars(env, jstr, temp);
        ALOGI("handle cmd 3");
    }
    //执行ffmpeg命令
    result = run(argc, argv);
    //释放内存
    for (i = 0; i < argc; i++) {
        free(argv[i]);
        ALOGI("handle cmd 4");
    }
    free(argv);
    ALOGI("handle cmd 5");
    return result;
}