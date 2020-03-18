cmake_minimum_required(VERSION 3.6.0)#版本
set(DIR ${CMAKE_CURRENT_SOURCE_DIR})#CMAKE_SOURCE_DIR为CmakeList.txt所在的位置
set(FFMPEG_SO_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${DIR}/src/main/jniLibs/${ANDROID_ABI})

include_directories(${DIR}/src/main/cpp)#加载头文件
include_directories(${DIR}/src/main/cpp/include)#加载头文件

find_library(log-lib log)

add_library(yeffmpeg SHARED
        ${DIR}/src/main/cpp/include/ffmpeg/cmdutils.c
        ${DIR}/src/main/cpp/include/ffmpeg/ffmpeg.c
        ${DIR}/src/main/cpp/include/ffmpeg/ffmpeg_filter.c
        ${DIR}/src/main/cpp/include/ffmpeg/ffmpeg_opt.c
        ${DIR}/src/main/cpp/include/ffmpeg/ffprobe.c
        ${DIR}/src/main/cpp/ffmpeg_cmd.c
        ${DIR}/src/main/cpp/AVpacket_queue.c
        ${DIR}/src/main/cpp/audio_play.c
        ${DIR}/src/main/cpp/openSL_audio_player.c
        ${DIR}/src/main/cpp/video_filter.c
        ${DIR}/src/main/cpp/video_player.c
        ${DIR}/src/main/cpp/media_player.c
        ${DIR}/src/main/cpp/com_leafye_ffmpegapp_YeFFmpeg.cpp)

add_library(avffmpeg SHARED IMPORTED)
set_target_properties(avffmpeg PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libffmpeg.so)

#add_library(avformat SHARED IMPORTED)
#set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavformat.so)

target_link_libraries(yeffmpeg
        avffmpeg
        -landroid #native_window
        -ljnigraphics #bitmap
        -lOpenSLES #openSLES
        ${log-lib})#avcodec avfilter avformat avutil