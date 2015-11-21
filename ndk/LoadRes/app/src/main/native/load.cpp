#include <cassert>
#include <jni.h>
#include <android/log.h>
#include <android/asset_manager_jni.h>

using namespace std;

extern "C" {

JNIEXPORT jstring JNICALL Java_org_example_loadres_LoadResActivity_load_1text(
	JNIEnv * env, jclass jclazz, jobject asset_manager, jstring path)
{
	AAssetManager * mgr = AAssetManager_fromJava(env, asset_manager);
	assert(mgr);
	
	char const * path_cstr = env->GetStringUTFChars(path, 0);
	AAsset * asset = AAssetManager_open(mgr, path_cstr, AASSET_MODE_BUFFER);
	if (!asset)
		return nullptr;  // unable to find a file
	
	void const * buf = AAsset_getBuffer(asset);
	assert(buf);
	jstring result = env->NewStringUTF((char const *)buf);
	
	AAsset_close(asset);
	env->ReleaseStringUTFChars(path, path_cstr);
	
	return result;
}

}  // extern "C"
