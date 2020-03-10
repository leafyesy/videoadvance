cmake_minimum_required(VERSION 3.10.2)#版本
set(DIR ${CMAKE_CURRENT_SOURCE_DIR})#CMAKE_SOURCE_DIR为CmakeList.txt所在的位置
set(FFMPEG_SO_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs)

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${DIR}/../../jniLibs/${ANDROID_ABI})

find_library(log-lib log)

include_directories(${DIR}/src/main/cpp)#加载头文件
#include_directories(${DIR}/src/main/cpp/ffmpeg)#加载头文件
#include_directories(${DIR}/src/main/cpp/include/arm64-v8a)
include_directories(${DIR}/src/main/cpp/include)#加载头文件

add_library(yeffmpeg SHARED
        ${DIR}/src/main/cpp/ffmpeg/cmdutils.c
        ${DIR}/src/main/cpp/ffmpeg/ffmpeg.c
        ${DIR}/src/main/cpp/ffmpeg/ffmpeg_filter.c
        ${DIR}/src/main/cpp/ffmpeg/ffmpeg_opt.c
        #${DIR}/src/main/cpp/ffmpeg_cmd.c
        ${DIR}/src/main/cpp/ffmpeg/ffprobe.c
        ${DIR}/src/main/cpp/com_leafye_ffmpegapp_YeFFmpeg.cpp)

add_library(avffmpeg SHARED IMPORTED)
set_target_properties(avffmpeg PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libffmpeg.so)

#add_library(avformat SHARED IMPORTED)
#set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavformat.so)
#
#add_library(avcodec SHARED IMPORTED)
#set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavcodec.so)
#
#add_library(avfilter SHARED IMPORTED)
#set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavfilter.so)
#
#add_library(avutil SHARED IMPORTED)
#set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavutil.so)



#if(${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
#    include_directories(src/main/cpp/include/armeabi-v7a)
#    message("This is armeabi-v7a")
#elseif(${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
#    include_directories(src/main/cpp/include/arm64-v8a)
#    message("This is arm64-v8a")
#endif()

target_link_libraries(
        yeffmpeg
        avffmpeg
        #        avformat
#        avcodec
#        avfilter
#        avutil
#        -landroid #native_window
#        -ljnigraphics #bitmap
#        -lOpenSLES #openSLES
        ${log-lib})#avcodec avfilter avformat avutil