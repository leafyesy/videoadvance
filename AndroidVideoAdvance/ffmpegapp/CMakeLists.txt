# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( avcodec
             SHARED
             IMPORTED )
set_target_properties( avcodec
                       PROPERTIES IMPORTED_LOCATION
                       ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/armeabi-v7a}/libavcodec.so )

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/jni/include)

#4.设置第三方库头库所在位置
# link_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/)
#
# message("path:" ${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/)

#5.对应的库
# link_libraries(libavcodec.so)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# find_library( # Sets the name of the path variable.
#               log-lib
#               log )

target_link_libraries( # Specifies the target library.
                     avcodec
                       #mp3lame
                       #ffmpeg
                       #-landroid #native_window
                       #-ljnigraphics #bitmap
                       #-lOpenSLES #openSLES
                       # Links the target library to the log library
                       # included in the NDK.
                       #${log-lib}
                     )