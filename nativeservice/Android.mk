LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE:= nservice

LOCAL_SRC_FILES :=main.cpp NativeService.cpp INativeService.cpp

LOCAL_SHARED_LIBRARIES := libbinder libutils liblog 

include $(BUILD_EXECUTABLE)

####################client###################




include $(CLEAR_VARS)

LOCAL_MODULE:= nclient

LOCAL_SRC_FILES :=client.cpp INativeService.cpp

LOCAL_SHARED_LIBRARIES := libbinder libutils liblog 

include $(BUILD_EXECUTABLE)
