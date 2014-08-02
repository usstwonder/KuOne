# Copyright 2007-2008 The Android Open Source Project

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_SRC_FILES += \
                 src/com/kukool/iosapp.weather/service/IWeather.aidl
LOCAL_STATIC_JAVA_LIBRARIES := libAdmob
LOCAL_STATIC_JAVA_LIBRARIES += iflytek
LOCAL_STATIC_JAVA_LIBRARIES += locSDK
LOCAL_PACKAGE_NAME := Weather
LOCAL_JAVA_LIBRARIES += android.policy
LOCAL_PROGUARD_ENABLED :=full
LOCAL_PROGUARD_FLAG_FILES := proguard.cfg
include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := libAdmob:libs/admob-sdk-android.jar
include $(BUILD_MULTI_PREBUILT)
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := iflytek:/libs/iflytek.jar
include $(BUILD_MULTI_PREBUILT)
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := locSDK:/libs/locSDK_3.1.jar
include $(BUILD_MULTI_PREBUILT)
LOCAL_MODULE_TAGS := optional
include $(BUILD_MULTI_PREBUILT)

# This finds and builds the test apk as well, so a single make does both.
include $(call all-makefiles-under,$(LOCAL_PATH))
