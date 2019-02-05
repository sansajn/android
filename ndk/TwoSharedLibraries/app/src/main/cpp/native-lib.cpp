#include <jni.h>
#include <string>
#include "b.hpp"

extern "C" JNIEXPORT jstring JNICALL
Java_two_shared_libraries_twosharedlibraries_MainActivity_stringFromJNI(
	JNIEnv *env,
	jobject /* this */) 
{
	std::string hello = introduce_yourself();
	return env->NewStringUTF(hello.c_str());
}
