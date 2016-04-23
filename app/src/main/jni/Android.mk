
MY_LOCAL_PATH := $(call my-dir)
LOCAL_PATH := $(MY_LOCAL_PATH)

# Pull ARToolKit into the build
include $(CLEAR_VARS)
ARTOOLKIT_DIR := $(MY_LOCAL_PATH)/../../../../../../android
ARTOOLKIT_LIBDIR := $(call host-path, $(ARTOOLKIT_DIR)/obj/local/$(TARGET_ARCH_ABI))
define add_artoolkit_module
	include $(CLEAR_VARS)
	LOCAL_MODULE:=$1
	LOCAL_SRC_FILES:=lib$1.a
	include $(PREBUILT_STATIC_LIBRARY)
endef
ARTOOLKIT_LIBS := ar2 kpm util eden argsub_es armulti ar aricp jpeg arvideo
LOCAL_PATH := $(ARTOOLKIT_LIBDIR)
$(foreach module,$(ARTOOLKIT_LIBS),$(eval $(call add_artoolkit_module,$(module))))

LOCAL_PATH := $(MY_LOCAL_PATH)

# Android arvideo depends on CURL.
CURL_DIR := $(ARTOOLKIT_DIR)/jni/curl
CURL_LIBDIR := $(call host-path, $(CURL_DIR)/libs/$(TARGET_ARCH_ABI))
define add_curl_module
	include $(CLEAR_VARS)
	LOCAL_MODULE:=$1
	#LOCAL_SRC_FILES:=lib$1.so
	#include $(PREBUILT_SHARED_LIBRARY)
	LOCAL_SRC_FILES:=lib$1.a
	include $(PREBUILT_STATIC_LIBRARY)
endef
#CURL_LIBS := curl ssl crypto
CURL_LIBS := curl
LOCAL_PATH := $(CURL_LIBDIR)
$(foreach module,$(CURL_LIBS),$(eval $(call add_curl_module,$(module))))

LOCAL_PATH := $(MY_LOCAL_PATH)
include $(CLEAR_VARS)

# ARToolKit libs use lots of floating point, so don't compile in thumb mode.
LOCAL_ARM_MODE := arm

LOCAL_PATH := $(MY_LOCAL_PATH)
LOCAL_MODULE := nftSimpleNative
LOCAL_SRC_FILES := nftSimple.cpp ARMarkerNFT.c trackingSub.c

# Make sure DEBUG is defined for debug builds. (NDK already defines NDEBUG for release builds.)
ifeq ($(APP_OPTIM),debug)
    LOCAL_CPPFLAGS += -DDEBUG
endif

LOCAL_C_INCLUDES += $(ARTOOLKIT_DIR)/../include/android $(ARTOOLKIT_DIR)/../include
LOCAL_LDLIBS += -llog -lGLESv1_CM -lz
LOCAL_WHOLE_STATIC_LIBRARIES += ar
LOCAL_STATIC_LIBRARIES += ar2 kpm util eden argsub_es armulti aricp cpufeatures jpeg arvideo
#LOCAL_SHARED_LIBRARIES += $(CURL_LIBS)
LOCAL_STATIC_LIBRARIES += $(CURL_LIBS)

include $(BUILD_SHARED_LIBRARY)

$(call import-module,android/cpufeatures)

