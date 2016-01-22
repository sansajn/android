#include <stdlib.h>
#include <jni.h>
#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "NDKApp", __VA_ARGS__))

extern "C" {

JNIEXPORT void JNICALL Java_org_example_MainActivity_onCreateNative(JNIEnv * env, jobject ojb)
{
	LOGI("Hello Android NDK!");
}

}  // extern "C"
