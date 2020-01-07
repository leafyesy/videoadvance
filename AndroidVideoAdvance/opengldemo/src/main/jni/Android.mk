LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libopengl
LOCAL_CFLAGS = -DFIXED_POINT -DUSE_KISS_FFT -DEXPORT="" -UHAVE_CONFIG_H
LOCAL_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_LDLIBS :=-llog
LOCAL_LDLIBS+=-1EGL
LOCAL_LDLIBS+=-1GLESv2


#LOCAL_SRC_FILES :=
#LOCAL_SRC_FILES :=

include $(BUILD_SHARED_LIBRARY)