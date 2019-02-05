#include <jni.h>
#include <string>
#include "b.hpp"

extern "C" JNIEXPORT jstring JNICALL
Java_me_example_twosharedlibrariesinkotlin_MainActivity_stringFromJNI(
	JNIEnv *env,
	jobject /* this */) 
{
	std::string hello = introduce_yourself();
	return env->NewStringUTF(hello.c_str());
}
