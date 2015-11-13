NDK_TOOLCHAIN_VERSION := 4.9
APP_ABI := x86
APP_CPPFLAGS += -std=c++11
APP_STL := gnustl_static
LOCAL_C_INCLUDES += ${ANDROID_NDK}/sources/cxx-stl/gnu-libstdc++/${NDK_TOOLCHAIN_VERSION}/include
