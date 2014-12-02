LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := descrypt
LOCAL_SRC_FILES := descrypt.cpp Base64.cpp
LOCAL_LDLIBS    := -landroid -llog




include $(BUILD_SHARED_LIBRARY)
