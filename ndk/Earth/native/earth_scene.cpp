#include <chrono>
#include <cassert>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtx/transform.hpp>
#include <GL/glew.h>
#include <jni.h>
#include <android/log.h>
#include <android/input.h>
#include "gl/shapes.hpp"
#include "gl/camera.hpp"
#include "gl/colors.hpp"
#include "gl/gles2/mesh_gles2.hpp"
#include "gl/gles2/program_gles2.hpp"
#include "gl/gles2/texture_loader_gles2.hpp"
#include "gl/gles2/touch_joystick_gles2.hpp"
#include "gl/gles2/default_shader_gles2.hpp"

char const * LOG_TAG = "scene";

using glm::vec4;
using glm::mat4;
using glm::vec3;
using glm::mat3;
using glm::ivec2;
using glm::radians;
using glm::translate;
using glm::rotate;
using glm::scale;
using gles2::shader::program;
using gles2::mesh;
using gles2::texture2d;
using gles2::texture_from_file;
using gles2::ui::touch::joystick;
using gl::camera;
using gl::make_sphere;


char const * earth_texture_path = "/sdcard/.earth/1_earth_1k.png";
//char const * earth_texture_path = "/sdcard/.earth/1_earth_8k.jpg";
//char const * earth_texture_path = "/sdcard/.earth/1_earth_16k.jpg";
char const * moon_texture_path = "/sdcard/.earth/moonmap1k.png";
//char const * moon_texture_path = "/sdcard/.earth/1_earth_1k.png";
//char const * moon_texture_path = "/sdcard/.earth/moonmap4k.jpg";


class scene_window
{
public:
	scene_window(int screen_w, int screen_h);
	void display();
	void update(float dt);
	void input(float dt);
	float aspect_ratio() const {return _width/(float)_height;}
	unsigned width() const {return _width;}
	unsigned height() const {return _height;}

private:
	int _width, _height;
	camera _cam;
	mesh _sphere;
	texture2d _earth_tex, _moon_tex;
	program _phong, _textured,_solid;
	float _earth_w, _moon_w, _sun_w;
	float _earth_ang, _moon_ang, _sun_ang;  // angles in radians
	float const _2pi;

public:
	joystick _move, _look;
};

scene_window::scene_window(int screen_w, int screen_h)
	: _width{screen_w}
	, _height{screen_h}
	, _cam{radians(70.0f), aspect_ratio(), 0.01, 1000}
	, _earth_w{radians(5.0f)}
	, _moon_w{radians(12.5f)}
	, _sun_w{radians(20.0f)}
	, _2pi{2*M_PI}
	, _move{ivec2{100, height()-100}, 50, width(), height()}
	, _look{ivec2{width()-100, height()-100}, 50, width(), height()}
{
	_cam.position = vec3{0,0,95};
	_sphere = make_sphere<mesh>(1.0f, 120, 90);

	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window(int, int):begin");

	try {
		auto default_tex_params = texture2d::parameters{}.min(gles2::texture_filter::linear);
		_earth_tex = texture_from_file(earth_texture_path, default_tex_params);
		_moon_tex = texture_from_file(moon_texture_path, default_tex_params);

		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window(int, int):textures");

		_phong.from_memory(gles2::textured_phong_shader_source);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window(int, int):phong_shader loaded");
//		_textured.from_memory(gles2::textured_shader_source);
		_solid.from_memory(gles2::flat_shader_source);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window(int, int):solid shader loaded");

		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window::scene_window(int, int):programs");

	} catch (std::exception & e) {
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", e.what());
	}
}

void scene_window::display()
{
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

	vec4 const white{1};
	vec4 const black{0};
	vec4 const ambient{.05f, .05f, .05f, 1.0f};

	mat4 world_to_screen = _cam.world_to_screen();

	GLint flat_position_a = glGetAttribLocation(_solid.id(), "position");
	GLint phong_position_a = glGetAttribLocation(_phong.id(), "position");
	GLint phong_texcoord_a = glGetAttribLocation(_phong.id(), "texcoord");
	GLint phong_normal_a = glGetAttribLocation(_phong.id(), "normal");

	// sun
	auto & sun_prog = _solid;
	sun_prog.use();
	mat4 local_to_world = rotate(_sun_ang, vec3{0,-1,0}) * translate(vec3{90, 0, 0});
	mat4 local_to_screen = world_to_screen * local_to_world;
	sun_prog.uniform_variable("local_to_screen", local_to_screen);
	sun_prog.uniform_variable("color", rgb::yellow);
	_sphere.attribute_location({flat_position_a});
	_sphere.render();
	vec4 sun_pos = local_to_world[3];  // <-- toto je dobra finta local_to_world[3] vrati posunutie transformacie

	// earth
	auto & earth_prog = _phong;
//	auto & earth_prog = _textured;
//	auto & earth_prog = _solid;
	earth_prog.use();
	local_to_world = rotate(_earth_ang, vec3{0,1,0}) * scale(vec3{12.756});
	local_to_screen = world_to_screen * local_to_world;
	earth_prog.uniform_variable("local_to_screen", local_to_screen);
	earth_prog.uniform_variable("local_to_world", local_to_world);
	earth_prog.uniform_variable("world_eye_pos", vec4{_cam.position, 1});
	earth_prog.uniform_variable("world_light_pos", sun_pos);
	earth_prog.uniform_variable("light_color", white);
	earth_prog.uniform_variable("material_emissive", black);
	earth_prog.uniform_variable("material_diffuse", white);
	earth_prog.uniform_variable("material_specular", white);
	earth_prog.uniform_variable("material_shininess", 50.0f);
	earth_prog.uniform_variable("ambient", ambient);
	_earth_tex.bind(0);
	earth_prog.uniform_variable("s", 0);
//	earth_prog.uniform_variable("color", rgb::blue);
	_sphere.attribute_location({phong_position_a, phong_texcoord_a, phong_normal_a});
	_sphere.render();

	// moon
	auto & moon_prog = _phong;
//	auto & moon_prog = _textured;
//	auto & moon_prog = _solid;
	moon_prog.use();
	local_to_world = rotate(_moon_ang, vec3{0,1,0}) * translate(vec3{60, 0, 0}) * scale(vec3{3.476});
	local_to_screen = world_to_screen * local_to_world;
	moon_prog.uniform_variable("local_to_screen", local_to_screen);
	moon_prog.uniform_variable("local_to_world", local_to_world);
	_moon_tex.bind(0);
	moon_prog.uniform_variable("s", 0);
//	moon_prog.uniform_variable("color", rgb::gray);
	_sphere.render();

	_move.render();
	_look.render();
}

void scene_window::update(float dt)
{
	_sun_ang = fmod(_sun_ang + _sun_w * dt, _2pi);
	_moon_ang = fmod(_moon_ang + _moon_w * dt, _2pi);
	_earth_ang = fmod(_earth_ang + _earth_w * dt, _2pi);
}

void scene_window::input(float dt)
{
	// move-joystick use
	float linear_velocity = 10.0;
	if (_move.up())
		_cam.position -= linear_velocity*dt * _cam.forward();
	if (_move.down())
		_cam.position += linear_velocity*dt * _cam.forward();
	if (_move.left())
		_cam.position -= linear_velocity*dt * _cam.right();
	if (_move.right())
		_cam.position += linear_velocity*dt * _cam.right();

	// look-joystick use
	float angular_velocity = .5;
	if (_look.up())
		_cam.rotation = normalize(angleAxis(angular_velocity*dt, _cam.right()) * _cam.rotation);
	if (_look.down())
		_cam.rotation = normalize(angleAxis(-angular_velocity*dt, _cam.right()) * _cam.rotation);
	if (_look.left())
		_cam.rotation = normalize(angleAxis(angular_velocity*dt, _cam.up()) * _cam.rotation);
	if (_look.right())
		_cam.rotation = normalize(angleAxis(-angular_velocity*dt, _cam.up()) * _cam.rotation);
}

scene_window * w = nullptr;

using hres_clock = std::chrono::high_resolution_clock;
hres_clock::time_point t;


extern "C" {

JNIEXPORT void JNICALL Java_org_example_earth_SceneLib_init(JNIEnv * env, jobject thiz,
	jint width, jint height)
{
	assert(!w && "already initialized");
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "init():begin");

	w = new scene_window{width, height};
	t = hres_clock::now();

	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "init():end");
}

JNIEXPORT void JNICALL Java_org_example_earth_SceneLib_free(JNIEnv * env, jobject thiz)
{
	delete w;
	w = nullptr;
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "free()");
}

JNIEXPORT void JNICALL Java_org_example_earth_SceneLib_render(JNIEnv * env, jobject thiz)
{
	assert(w && "not initialized");

	// elapsed time
	hres_clock::time_point now = hres_clock::now();
	hres_clock::duration d = now - t;
	t = now;
	float dt = std::chrono::duration_cast<std::chrono::milliseconds>(d).count() / 1000.0f;

	// input
	w->input(dt);

	// update
	w->update(dt);

	// display
	w->display();

//	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "render()");
}

//! \param event 0 for down, 1 for up, 2 for motion
JNIEXPORT void JNICALL Java_org_example_earth_SceneLib_touch(JNIEnv * env, jobject thiz, jfloat x, jfloat y, jint action)
{
	joystick::touch_event te;

	switch (action)
	{
		case 0:
			te = joystick::touch_event::down;
			break;

		case 1:
			te = joystick::touch_event::up;
			break;

		case 2:
			te = joystick::touch_event::move;
			break;

		default: return;  // ignore all other actions ...
	}

	//	update joysticks
	assert(w && "window not initialized");
	ivec2 touch_pos(x, y);
	w->_move.touch(touch_pos, te);
	w->_look.touch(touch_pos, te);
}

}  // extern "C"
