LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := cpp11
LOCAL_SRC_FILES := cpp11.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
