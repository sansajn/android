// precita subor pomocou fopen() alebo ifstream ...
#include <string>
#include <fstream>
#include <sstream>
#include <cstdio>
#include <cassert>
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "load"

using namespace std;

extern "C" {

// old plain c
JNIEXPORT jstring JNICALL Java_org_example_readfile_ReadFileActivity_read_1text_1c(
	JNIEnv * env, jclass jclazz, jstring path)
{
	char const * c_path = env->GetStringUTFChars(path, NULL);
	FILE * fin = fopen(c_path, "r");
	if (fin)
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "file '%s' opened", c_path);
	assert(fin && "unable to open a file with fopen()");
	
	env->ReleaseStringUTFChars(path, c_path);
	
	char buf[1024];
	fread(buf, 1, 1024, fin);
	assert(feof(fin) && "file is bigger then 1024 bytes");
	
	jstring result = env->NewStringUTF(buf);
	
	fclose(fin);
	return result;
}

// c++ file read
JNIEXPORT jstring JNICALL Java_org_example_readfile_ReadFileActivity_read_1text_1cpp(
	JNIEnv * env, jclass jclazz, jstring path)
{
	char const * c_path = env->GetStringUTFChars(path, NULL);
	std::ifstream fin{c_path};
	env->ReleaseStringUTFChars(path, c_path);
	assert(fin.is_open() && "unable to open a file with ifstream");
	
	std::ostringstream ss;
	ss << fin.rdbuf();
	std::string s = ss.str();
	
	return env->NewStringUTF(s.c_str());
}

}  // extern "C"
