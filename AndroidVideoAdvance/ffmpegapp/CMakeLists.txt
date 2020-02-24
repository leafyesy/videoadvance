cmake_minimum_required(VERSION 3.10.2)#版本
find_library(log-lib log)
include_directories(libs/include)#加载头文件
set(DIR ${CMAKE_SOURCE_DIR}/../../../../libs)#CMAKE_SOURCE_DIR为CmakeList.txt所在的位置
set(FFMPEG_DIR ${CMAKE_SOURCE_DIR}/../../../../src/main/jni/include)
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${DIR}/arm64-v8a/libavcodec.so)

add_library(avfilter SHARE IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${DIR}/arm64-v8a/libavfilter.so)

add_library(avformat SHARE IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${DIR}/arm64-v8a/libavformat.so)

add_library(avutil SHARE IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${DIR}/arm64-v8a/libavutil.so)

target_link_libraries(avcodec avfilter avformat avutil ${log-lib})