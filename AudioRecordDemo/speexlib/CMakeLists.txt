##cmake 最低版本
#cmake_minimum_request(VERSION 3.6.0)
##指定项目
#project(Main)
#add_executable()#生成可执行文件
#add_library(common STATIC util.cpp)#生成静态库
#add_library(common SHARED util.cpp)#生成动态库
#
#aux_source_directory(. SRC_LIST)#搜索当前目录下的所有CPP文件,但不能查找子目录
#add_library(demo $(SRC_LIST))
#
##或者
#file(GLOB DIR_SRCS *.c)
#add_executable(main ${DIR_SRCS})
#
#
#add_library( # Sets the name of the library.
#             Hello
#
#             # Sets the library as a shared library.
#             SHARED
#
#             # Provides a relative path to your source file(s).
#             src/main/jni/Hello.cpp )