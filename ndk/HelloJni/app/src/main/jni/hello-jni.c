#include <string.h>
#include <jni.h>

jstring Java_org_example_hellojni_HelloJni_stringFromJNI(JNIEnv * env, jobject thiz)
{
	return (*env)->NewStringUTF(env, "Hello from JNI!");
}
