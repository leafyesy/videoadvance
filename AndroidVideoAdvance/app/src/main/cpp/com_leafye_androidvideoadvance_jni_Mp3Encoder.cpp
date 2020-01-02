#include<jni.h>

#define LOGE(FORMAT,...) _android_log_print(ANDROID_LOG_ERROR,"lame",FORMAT,##_VA_ARGS__);

JNIEXPORT jint JNICALL Java_com_leafye_androidvideoadvance_jni_Mp3Encoder_init
  (JNIEnv *, jobject, jstring, jint, jint, jint, jstring){


  }