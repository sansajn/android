#include <chrono>
#include <list>
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
#include "gl/window.hpp"
#include "gles2/mesh_gles2.hpp"
#include "gles2/program_gles2.hpp"
#include "gles2/texture_loader_gles2.hpp"
#include "gles2/touch_joystick_gles2.hpp"
#include "gles2/default_shader_gles2.hpp"
#include "androidgl/android_window.hpp"

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

constexpr float two_pi = 2.0f*M_PI;
constexpr unsigned joystick_size = 75;


class scene_window : public ui::android_window
{
public:
	using base = ui::android_window;
	using parameters = base::parameters;

	scene_window(parameters const & params);

	void display() override;
	void update(float dt) override;
	void input(float dt) override;
	void reshape(int w, int h) override;

private:
	camera _cam;
	mesh _sphere;
	texture2d _earth_tex, _moon_tex;
	program _phong, _textured, _flat;
	float _earth_w, _moon_w, _sun_w;
	float _earth_ang, _moon_ang, _sun_ang;  //!< \note angles in radians
	int _phong_position_a, _phong_texcoord_a, _phong_normal_a;
	int _flat_position_a;
	joystick _move, _look;
};


scene_window::scene_window(parameters const & params)
	: base{params}
	, _earth_w{radians(5.0f)}
	, _moon_w{radians(12.5f)}
	, _sun_w{radians(20.0f)}
	, _move{ivec2{joystick_size+50, height()-joystick_size-50}, joystick_size, width(), height()}
	, _look{ivec2{width()-joystick_size-50, height()-joystick_size-50}, joystick_size, width(), height()}
{
	_sphere = make_sphere<mesh>(1.0f, 120, 90);

	try {
		auto default_tex_params = texture2d::parameters{}.min(gles2::texture_filter::linear);
		_earth_tex = texture_from_file(earth_texture_path, default_tex_params);
		_moon_tex = texture_from_file(moon_texture_path, default_tex_params);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "textures loaded");

		_phong.from_memory(gles2::textured_phong_shader_source);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "  '%s' loaded", "gles2::textured_phong_shader_source");
		_phong_position_a = _phong.attribute_location("position");
		_phong_texcoord_a = _phong.attribute_location("texcoord");
		_phong_normal_a = _phong.attribute_location("normal");

		_textured.from_memory(gles2::textured_shader_source);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "  '%s' loaded", "gles2::textured_shaded_shader_source");
		// TODO: attributes ...

		_flat.from_memory(gles2::flat_shader_source);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "  '%s' loaded", "gles2::flat_shader_source");
		_flat_position_a = _flat.attribute_location("position");
	} catch (std::exception & e) {
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "exception in scene_window{int, int} detected: %s", e.what());
	}
}

void scene_window::reshape(int w, int h)
{
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "scene_window::reshape(w:%d, h:%d)", w, h);
	base::reshape(w, h);
	_cam = camera{radians(70.0f), aspect_ratio(), 0.01, 1000};
	_cam.position = vec3{0,0,95};
	_move.screen_geometry(w, h);
	_look.screen_geometry(w, h);
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

	// sun
	auto & sun_prog = _flat;
	sun_prog.use();
	mat4 local_to_world = rotate(_sun_ang, vec3{0,-1,0}) * translate(vec3{90, 0, 0});
	mat4 local_to_screen = world_to_screen * local_to_world;
	sun_prog.uniform_variable("local_to_screen", local_to_screen);
	sun_prog.uniform_variable("color", rgb::yellow);
	_sphere.attribute_location({_flat_position_a});
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
	_sphere.attribute_location({_phong_position_a, _phong_texcoord_a, _phong_normal_a});
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
	base::update(dt);
	_sun_ang = fmod(_sun_ang + _sun_w * dt, two_pi);
	_moon_ang = fmod(_moon_ang + _moon_w * dt, two_pi);
	_earth_ang = fmod(_earth_ang + _earth_w * dt, two_pi);
}

void scene_window::input(float dt)
{
	// update joystick input (move to joystick.input(dt, user_input))
	for (ui::touch_list::finger & f : in.touch.fingers())
	{
		joystick::touch_event te;
		if (f.state & ui::touch_list::finger::move)
			te = joystick::touch_event::move;
		else if (f.state & ui::touch_list::finger::down)
			te = joystick::touch_event::down;
		else
			te = joystick::touch_event::up;

		_move.touch(f.position, te);
		_look.touch(f.position, te);
	}

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

	base::input(dt);
}

scene_window * w = nullptr;


void create(int width, int height)
{
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "create(%d, %d)", width, height);
	assert(!w && "scene already created");
	w = new scene_window{scene_window::parameters{}.geometry(width, height)};
}

void destroy()
{
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "destroy()");
	delete w;
	w = nullptr;
}
