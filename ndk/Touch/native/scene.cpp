#include <chrono>
#include <list>
#include <cassert>
#include <string>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/matrix_inverse.hpp>
#include <glm/gtx/transform.hpp>
#include <GL/glew.h>
#include <android/log.h>
#include "gl/shapes.hpp"
#include "gl/camera.hpp"
#include "gl/colors.hpp"
#include "gl/window.hpp"
#include "gles2/mesh_gles2.hpp"
#include "gles2/program_gles2.hpp"
#include "gles2/default_shader_gles2.hpp"
#include "gles2/touch_joystick_gles2.hpp"
#include "android/android_window.hpp"

char const * LOG_TAG = "scene";

using std::string;
using glm::vec4;
using glm::mat4;
using glm::vec3;
using glm::mat3;
using glm::ivec2;
using glm::radians;
using glm::translate;
using glm::rotate;
using glm::scale;
using glm::inverseTranspose;
using glm::normalize;
using gles2::shader::program;
using gles2::mesh;
using gles2::ui::touch::joystick;
using gl::camera;
using gl::make_sphere;


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
	program _shaded;
	int _flat_position_a, _flat_normal_a;
	joystick _look;
};

scene_window::scene_window(parameters const & params)
	: base{params}
	, _look{ivec2{100, height() - 100}, 75, width(), height()}
{
	_sphere = make_sphere<mesh>(1.0f, 120, 90);

	try {
		_shaded.from_memory(gles2::flat_shaded_shader_source);
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "  '%s' loaded", "gles2::flat_shaded_shader_source");
		_flat_position_a = _shaded.attribute_location("position");
		_flat_normal_a = _shaded.attribute_location("normal");
	} catch (std::exception & e) {
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "exception in scene_window() detected: %s", e.what());
	}

	_sphere.attribute_location({_flat_position_a, _flat_normal_a});
}

void scene_window::reshape(int w, int h)
{
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "scene_window::reshape(w:%d, h:%d)", w, h);
	base::reshape(w, h);

	_cam = camera{radians(70.0f), aspect_ratio(), 0.01, 1000};
	_cam.position = vec3{1,1,5};
	_cam.look_at(vec3{0,0,0});

	_look.screen_geometry(w, h);
}

void scene_window::display()
{
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

	mat4 M{1};
	mat4 local_to_screen = _cam.world_to_screen() * M;

	_shaded.use();
	_shaded.uniform_variable("local_to_screen", local_to_screen);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::teal);
	_shaded.uniform_variable("light_dir", normalize(vec3{1,2,3}));

	_sphere.render();

	_look.render();
}

void scene_window::update(float dt)
{
	base::update(dt);
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

		_look.touch(f.position, te);
	}

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
