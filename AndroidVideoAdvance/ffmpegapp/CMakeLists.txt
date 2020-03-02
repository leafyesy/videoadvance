cmake_minimum_required(VERSION 3.6.0)#版本
set(DIR ${CMAKE_CURRENT_SOURCE_DIR})#CMAKE_SOURCE_DIR为CmakeList.txt所在的位置
set(FFMPEG_SO_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs)
message("path:" ${DIR})
message("path2:" ${FFMPEG_SO_DIR})

#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${DIR}/../../jniLibs/${ANDROID_ABI})

#include_directories(${DIR}/src/main/cpp)#加载头文件
include_directories(${DIR}/src/main/cpp/include)#加载头文件

find_library(log-lib log)

#add_library(yeffmpeg SHARED ${DIR}/src/main/cpp/nativelib.cpp)
add_library(yeffmpeg SHARED ${DIR}/src/main/cpp/com_leafye_ffmpegapp_YeFFmpeg.cpp)

#add_library(avcodec SHARED IMPORTED)
#set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavcodec.so)
#
#add_library(avfilter SHARED IMPORTED)
#set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavfilter.so)
#
add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavformat.so)
#
#add_library(avutil SHARED IMPORTED)
#set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${FFMPEG_SO_DIR}/arm64-v8a/libavutil.so)

target_link_libraries(yeffmpeg ${log-lib})#avcodec avfilter avformat avutil