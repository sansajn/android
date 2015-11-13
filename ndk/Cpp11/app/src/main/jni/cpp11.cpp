#include <algorithm>
#include <iterator>
#include <vector>
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "Cpp11_sumArray"

using namespace std;

extern "C" {

JNIEXPORT jint JNICALL Java_org_example_cpp11_Cpp11Activity_sumArray(
	JNIEnv * env, jobject obj, jintArray arr)
{
	auto len = env->GetArrayLength(arr);
	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "arr length is %d", len);

	jint * body = env->GetIntArrayElements(arr, 0);
	vector<int> numbers(body, body + len);

	int sum = 0;
	for_each(begin(numbers), end(numbers), [&sum](int y) {sum += y;});

	env->ReleaseIntArrayElements(arr, body, 0);

	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "sum(arr) = %d", sum);

	return sum;
}


}  // extern "C"
