// vyrenderuje png obrazok
#include <cassert>
#include <jni.h>
#include <android/log.h>
#include <AL/alc.h>
#include "al/audio.hpp"
#include "al/waveform.hpp"

#define LOG_TAG "openal_sample"

char const * sound_path = "/sdcard/test_files/test_clip.wav";

ALCdevice * __dev = nullptr;
ALCcontext * __ctx = nullptr;
audio_source * __as = nullptr;

void openal_init()
{
	__dev = alcOpenDevice(nullptr);  // open default device
	assert(__dev && "failed to open a device");

	__ctx = alcCreateContext(__dev, nullptr);
	assert(__ctx && "failed to create an audio context");

	alcMakeContextCurrent(__ctx);
	assert(alGetError() == AL_NO_ERROR && "context stuff failed");
}

void openal_free()
{
	alcDestroyContext(__ctx);
	alcCloseDevice(__dev);
}

extern "C" {

JNIEXPORT void JNICALL Java_org_example_openal_MainActivity_play_1sound(
	JNIEnv * env, jobject obj)
{
	if (!__as)
	{
		openal_init();
		__as = new audio_source;
		__as->attach(std::shared_ptr<wave_data>{new waveform_wave{sound_path}});
	}

	__as->play();
}

JNIEXPORT void JNICALL Java_org_example_openal_MainActivity_update(
	JNIEnv * env, jobject obj)
{
	__as->update();
}

JNIEXPORT void JNICALL Java_org_example_openal_MainActivity_free(
	JNIEnv * env, jobject obj)
{
	delete __as;
	openal_free();
}

}  // extern "C"
