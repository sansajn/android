#include <jni.h>

extern "C" {

JNIEXPORT jstring JNICALL Java_org_example_MainActivity_hello(
	JNIEnv * env, jobject obj)
{
	return env->NewStringUTF("welcome in a native world");
}

}  // extern "C"