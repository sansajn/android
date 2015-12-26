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
#include "gl/gles2/mesh_gles2.hpp"
#include "gl/gles2/program_gles2.hpp"
#include "gl/gles2/texture_loader_gles2.hpp"
#include "gl/gles2/touch_joystick_gles2.hpp"
#include "gl/gles2/default_shader_gles2.hpp"
#include "gl/window.hpp"

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


namespace ui {

int touch_list_finger_cast(event_handler::action finger_action);

struct touch_event
{
	ivec2 position;
	event_handler::action finger_action;
	int finger_id;
};

class touch_list
{
public:
	struct finger
	{
		enum {down = 1, move = 2, up = 4, canceled = 8};  // finger states
		ivec2 position;
		int id;
		int state;  //!< \note kombinacia hodnot down, move, up a canceled
	};

	using iterator = std::list<finger>::iterator;

	void insert(touch_event const & te);
	iterator begin() {return _fingers.begin();}
	iterator end() {return _fingers.end();}

private:
	std::list<finger> _fingers;
};

void touch_list::insert(touch_event const & te)
{
	auto it = find_if(_fingers.begin(), _fingers.end(),
		[&te](finger const & f){return f.id == te.finger_id;});

	if (it == _fingers.end())  // unikatny event
	{
		finger f;
		f.position = te.position;
		f.id = te.finger_id;
		f.state = touch_list_finger_cast(te.finger_action);
		_fingers.push_back(f);
	}
	else  // uz existujuci event
	{
		if (te.finger_action == event_handler::action::up)  // ak je action up, zmaz event
			_fingers.erase(it);
		else
			it->state |= touch_list_finger_cast(te.finger_action);  // inak uprav stav
	}
}

class touch_input
{
public:
	touch_list & fingers() {return _touches;}

	void touch_performed(int x, int y, int finger_id, event_handler::action a);  //!< internl use only (vola okenna vrstva)

private:
	touch_list _touches;
};

void touch_input::touch_performed(int x, int y, int finger_id, event_handler::action a)
{
	touch_event te;
	te.position = ivec2{x,y};
	te.finger_action = a;
	te.finger_id = finger_id;
	_touches.insert(te);
}

int touch_list_finger_cast(event_handler::action finger_action)
{
	switch (finger_action)
	{
		case event_handler::action::down: return touch_list::finger::down;
		case event_handler::action::move: return touch_list::finger::move;
		case event_handler::action::up: return touch_list::finger::up;
		case event_handler::action::canceled: return touch_list::finger::canceled;
		default:
			throw std::logic_error{"unknown touch_event"};
	}
}


class android_layer : public window_layer
{
public:
	using parameters = window_layer::parameters;

	android_layer(parameters const & params);
	~android_layer();

	class user_input  //!< keyboard, mouse and touch user input
	{
	public:
		user_input() {}

		// verejne rozhranie
		bool key(unsigned char c) const;
		bool key_up(unsigned char c) const;
		bool any_of_key(char const * s) const;
		bool any_of_key_up(char const * s) const;
		bool mouse(button b) const;
		bool mouse_up(button b) const;
		bool mouse_wheel(wheel w) const;
		glm::ivec2 const & mouse_position() const;

		touch_input touch;  // touch api

		// TODO: special keys support

		void update() {}  //!< for internal use only

		// funkcie informujuce o zmene stavu uzivatelskeho vstupu (vola ich okenna vrstva)
		void mouse_motion(int x, int y) {}
		void mouse_passive_motion(int x, int y) {}
		void mouse_click(event_handler::button b, event_handler::state s, event_handler::modifier m, int x, int y) {}
		void mouse_wheel(event_handler::wheel w, event_handler::modifier m, int x, int y) {}
		void key_typed(unsigned char c, event_handler::modifier m, int x, int y) {}
		void key_released(unsigned char c, event_handler::modifier m, int x, int y) {}
		void touch_performed(int x, int y, int finger_id, event_handler::action a);
	};
};  // android_layer

void android_layer::user_input::touch_performed(int x, int y, int finger_id, event_handler::action a)
{
	touch.touch_performed(x, y, finger_id, a);
}

using android_window = window<pool_behaviour, android_layer>;

namespace android_private {

event_handler::key tospecial(int k);
event_handler::modifier tomodifier(int m);

void display_func();
void reshape_func(int w, int h);
void idle_func();
void close_func();
void mouse_func(int button, int state, int x, int y);
void motion_func(int x, int y);
void passive_motion_func(int x, int y);
void keyboard_func(unsigned char key, int x, int y);
void keyboard_up_func(unsigned char key, int x, int y);
void special_func(int key, int x, int y);
void special_up_func(int key, int x, int y);
void touch_func(int x, int y, int finger_id, int action);

android_layer * __window = nullptr;

}  // android_private


android_layer::android_layer(parameters const & params)
{
	android_private::__window = this;
}

android_layer::~android_layer()
{
	assert(android_private::__window == this);
	android_private::__window = nullptr;
}


namespace android_private {

//void display_func()
//{
//	assert(__window && "invalid window");
//	__window->display();
//}

void reshape_func(int w, int h)
{
	assert(__window && "invalid window");
	__window->reshape(w, h);
}

//void touch_func(int x, int y, int finger_id, int action)
//{
//	assert(__window && "invalid window");
//	__window->touch_performed(x, y, finger_id, (event_handler::action)action);
//}

}  // android_private

}  // ui


class scene_window : public ui::android_window
{
public:
	using base = ui::android_window;
	using parameters = base::parameters;

	scene_window(parameters const & params);

	void display() override;
	void update(float dt) override;
	void input(float dt) override;

private:
	camera _cam;
	mesh _sphere;
	texture2d _earth_tex, _moon_tex;
	program _phong, _textured, _flat;
	float _earth_w, _moon_w, _sun_w;
	float _earth_ang, _moon_ang, _sun_ang;  //!< \note angles in radians
	int _phong_position_a, _phong_texcoord_a, _phong_normal_a;
	int _flat_position_a;

public:
	joystick _move, _look;
};


scene_window::scene_window(parameters const & params)
	: base{params}
	, _cam{radians(70.0f), aspect_ratio(), 0.01, 1000}
	, _earth_w{radians(5.0f)}
	, _moon_w{radians(12.5f)}
	, _sun_w{radians(20.0f)}
	, _move{ivec2{joystick_size+50, height()-joystick_size-50}, joystick_size, width(), height()}
	, _look{ivec2{width()-joystick_size-50, height()-joystick_size-50}, joystick_size, width(), height()}
{
	_cam.position = vec3{0,0,95};
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
	_sun_ang = fmod(_sun_ang + _sun_w * dt, two_pi);
	_moon_ang = fmod(_moon_ang + _moon_w * dt, two_pi);
	_earth_ang = fmod(_earth_ang + _earth_w * dt, two_pi);
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

/* jni scene mini wrapper, v init-e potrebujem vytvorit instanciu okna, v display-i
potrebujem pristupovat do behviour casti okna a v touch potrebujem pristupovat do window-layer casti okna. */
void init(int width, int height);
void free();
void display();
void touch(float x, float y, int finger_id, int action);


/*! funkcia sa zavola vzdy po vytvoreni egl kontextu, system moze egl kontext
uvolnit pricom vsetky (opengl) zdroje budu automaticky uvolnene. */
void init(int width, int height)
{
	delete w;  // ak uz existuje scena, vytvor ju znova
	w = new scene_window{scene_window::parameters{}.geometry(width, height)};

	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "init()");
}

void free()  //!< scena uz nie je potrebna
{
	delete w;
	w = nullptr;

	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "free()");
}

void display()
{
	assert(w && "invalid scene");
	w->loop_step();
}

ui::event_handler::action to_touch_action(int a)
{
	switch (a)
	{
		case 0: return ui::event_handler::action::down;
		case 1: return ui::event_handler::action::up;
		case 2: return ui::event_handler::action::move;
		default:
			throw std::logic_error{"unknown touch action"};
	}
}

void touch(float x, float y, int finger_id, int action)  //!< \param action 0 for down, 1 for up, 2 for move and 3 for canceled
{
//	joystick::touch_event te;

//	switch (action)
//	{
//		case 0:
//			te = joystick::touch_event::down;
//			break;

//		case 1:
//			te = joystick::touch_event::up;
//			break;

//		case 2:
//			te = joystick::touch_event::move;
//			break;

//		default: return;  // ignore all other actions ...
//	}

//	//	update joysticks
//	assert(w && "window not initialized");
//	ivec2 touch_pos(x, y);
//	w->_move.touch(touch_pos, te);
//	w->_look.touch(touch_pos, te);

	static_cast<ui::android_layer *>(w)->touch_performed(x, y, finger_id, to_touch_action(action));
}


extern "C" {

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_init(JNIEnv * env, jobject thiz,
	jint width, jint height)
{
	init(width, height);
}

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_free(JNIEnv * env, jobject thiz)
{
	free();
}

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_display(JNIEnv * env, jobject thiz)
{
	display();
}

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_touch(JNIEnv * env, jobject thiz, jint x, jint y, jint finger_id, jint action)
{
	touch(x, y, finger_id, action);
}

}  // extern "C"
