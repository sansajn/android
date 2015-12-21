// zobrazi vsetky subory s /sdcard/test_files
#include <chrono>
#include <string>
#include <sstream>
#include <cassert>
#include <glm/gtx/transform.hpp>
#include <Magick++.h>
#include <jni.h>
#include <android/log.h>
#include "gl/camera.hpp"
#include "gl/shapes.hpp"
#include "gl/gles2/program_gles2.hpp"
#include "gl/gles2/mesh_gles2.hpp"
#include "gl/gles2/texture_gles2.hpp"
#include "tools/directory_iterator.hpp"

using namespace std;
using glm::mat4;
using glm::vec3;
using glm::scale;
using glm::translate;
using glm::ortho;
using gl::camera;
using gles2::mesh;
using gles2::texture2d;
using gles2::shader::program;

char const * texture_path = "/sdcard/test_files";
char const * LOG_TAG = "imagick";

char const * ortho_image_view_shader_source = R"(
	#ifdef _VERTEX_
	attribute vec3 position;
	uniform mat4 local_to_screen;
	varying vec2 st;
	void main() {
		st = position.xy/2.0 + 0.5;
		gl_Position = local_to_screen * vec4(position, 1);
	}
	#endif
	#ifdef _FRAGMENT_
	precision mediump float;
	uniform sampler2D s;
	varying vec2 st;
	void main() {
		gl_FragColor = texture2D(s, st);
	}
	#endif
)";


class scene_window
{
public:
	scene_window(int screen_w, int screen_h);
	void display();
	void update(float dt);
	float aspect_ratio()	const {return (float)_w/(float)_h;}

private:
	void load_textures();

	int _w, _h;
	program _prog;
	vector<texture2d> _textures;
	mesh _quad;
	camera _cam;
	float _time_count = 0.0f;
	unsigned _texture_idx = 0;
};

scene_window::scene_window(int screen_w, int screen_h)
	: _w{screen_w}, _h{screen_h}
{
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window()");
	_prog.from_memory(ortho_image_view_shader_source);
	load_textures();
	_quad = gl::make_quad_xy<mesh>();
	_cam = camera{ortho(0.0f, (float)_w, (float)_h, 0.0f, 0.0f, 100.0f)};
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window():done");
}

void scene_window::display()
{
//	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::display()");
	glClear(GL_COLOR_BUFFER_BIT);

	if (_textures.empty())
		return;

	// zobraz iba jednu texturu
	_prog.use();
	texture2d & t = _textures[_texture_idx];
	t.bind(0);
	_prog.uniform_variable("s", 0);
	mat4 local_to_screen = _cam.world_to_screen() * translate(vec3{_w/2, _h/2, 0}) * scale(vec3{t.width(), t.height(), 1});
	_prog.uniform_variable("local_to_screen", local_to_screen);
	_quad.render();
}

void scene_window::update(float dt)
{
	_time_count += dt;
	if (_time_count > 3.0f)  // new texture after 3s
	{
		_time_count -= 3.0f;
		_texture_idx = (_texture_idx+1) % _textures.size();
	}
}

void scene_window::load_textures()
{
	// ziskaj vsetky jpg, jpeg a png subory s adresara
	vector<string> images;
	for (auto entry : directory_iterator{texture_path})
	{
		char const * from_dot = strchr(entry->d_name, '.');
		if (!from_dot)
			continue;  // not a jpg, jpeg or png file

		if (!strcmp(from_dot, ".jpg") || !strcmp(from_dot, ".jpeg") || !strcmp(from_dot, ".png"))
			images.push_back(entry->d_name);
	}

	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "scene_window::load_textures():%d images found", images.size());

	// loadni ich image-magickom
	using namespace Magick;
	for (auto image_name : images)
	{
		// read and resize
		Image im{texture_path + string{"/"} + image_name};
		im.sample(Geometry{"25x25%"});  // zmensi na 1/5 povodnej velkosti

		// create texture
		Blob pixels;
		im.write(&pixels, "RGBA");
		_textures.emplace_back(im.columns(), im.rows(), gles2::pixel_format::rgba, gles2::pixel_type::ub8, pixels.data());

		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "  image '%s' loaded", image_name.c_str());
	}
}


scene_window * w = nullptr;


extern "C" {

JNIEXPORT void JNICALL Java_org_example_imagemagick_SceneWrapper_init(
	JNIEnv * env, jobject thiz, jint width, jint height)
{
	assert(!w && "already initialized");
	w = new scene_window{width, height};
}

JNIEXPORT void JNICALL Java_org_example_imagemagick_SceneWrapper_render(
	JNIEnv * env, jobject thiz)
{
	assert(w && "not initialized");

	// elapsed time
	using hres_clock = std::chrono::high_resolution_clock;
	static hres_clock::time_point t = hres_clock::now();

	hres_clock::time_point now = hres_clock::now();
	hres_clock::duration d = now - t;
	t = now;
	float dt = std::chrono::duration_cast<std::chrono::milliseconds>(d).count() / 1000.0f;

	// lag detector (TODO: je na nieco dobry ?)
	static float t_sum = 0;
	static int t_count = 0;
	static float dt_mean = 0.0f;
	if ((t_count > 100) && (dt > (2.0f * dt_mean)))
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "lag detected dt=%g (dt_mean=%g)", dt, dt_mean);
	t_sum += dt;
	++t_count;
	dt_mean = t_sum / t_count;

	// update
	w->update(dt);

	// display
	w->display();
}

JNIEXPORT void JNICALL Java_org_example_imagemagick_SceneWrapper_free(
	JNIEnv * env, jobject thiz)
{
	delete w;
	w = nullptr;
}

}  // extern "C"
