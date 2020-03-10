//
// Created by syye on 2020/3/10.
//

#ifndef ANDROIDVIDEOADVANCE_COMMON_H
#define ANDROIDVIDEOADVANCE_COMMON_H

#include <android/log.h>

#define ALOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"ffmpeg",FORMAT,##__VA_ARGS__);
#define ALOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"ffmpeg",FORMAT,##__VA_ARGS__);

#endif //ANDROIDVIDEOADVANCE_COMMON_H


