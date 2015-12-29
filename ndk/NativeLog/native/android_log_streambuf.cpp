#include <streambuf>
#include <iostream>
#include <android/log.h>
#include <jni.h>
#include <cassert>

class android_log_streambuf : public std::streambuf
{
public:
	android_log_streambuf(int priority);
	~android_log_streambuf();

protected:
	int_type overflow(int_type c) override;
	int sync() override;

private:
	void flush();

	int _prio;
	static int const BUFSIZE = 1024;
	char _buf[BUFSIZE];
};

android_log_streambuf::android_log_streambuf(int priority)
	: _prio{priority}
{
	setp(_buf, _buf + BUFSIZE-1);  // rezervuj jeden znak pre '\0'
}

android_log_streambuf::~android_log_streambuf()
{
	flush();
}

android_log_streambuf::int_type android_log_streambuf::overflow(int_type c)
{
	assert(0 && "stream buffer overflow");
	flush();
	return c;
}

int android_log_streambuf::sync()
{
	if (*(pptr()-1) == '\n')
		flush();
	return 0;
}

void android_log_streambuf::flush()
{
	if (pptr() == _buf)
		return;  // buffer je prazdny

	*pptr() = '\0';
	__android_log_write(_prio, "NativeLog", _buf);
	setp(_buf, _buf + BUFSIZE-1);
}


extern "C" {

JNIEXPORT void JNICALL Java_org_example_nativelog_MainActivity_main(JNIEnv * env, jobject thiz)
{
	android_log_streambuf logbuf{ANDROID_LOG_DEBUG};
	std::streambuf * default_cerr_buf = std::cerr.rdbuf();
	std::cerr.rdbuf(&logbuf);

	std::cerr << "hello from native-code" << std::endl;
	std::cerr << "another hello from native-code" << std::endl;

	int dummy = 3;
	std::cerr << "dummy:" << dummy << std::endl;

	std::cerr.rdbuf(default_cerr_buf);
}

}  // extern "C"

