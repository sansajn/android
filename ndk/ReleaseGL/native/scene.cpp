#include <chrono>
#include <list>
#include <cassert>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/matrix_inverse.hpp>
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
using glm::inverseTranspose;
using glm::normalize;
using gles2::shader::program;
using gles2::mesh;
using gl::camera;
using gl::make_sphere;


namespace ui {

class android_layer : public window_layer
{
public:
	using parameters = window_layer::parameters;

	android_layer(parameters const & params) {}
	~android_layer() {}

	class user_input  //!< keyboard, mouse and touch user input
	{
	public:
		user_input() {}

		void update() {}  //!< for internal use only

		// funkcie informujuce o zmene stavu uzivatelskeho vstupu (vola ich okenna vrstva)
		void mouse_motion(int x, int y) {}
		void mouse_passive_motion(int x, int y) {}
		void mouse_click(event_handler::button b, event_handler::state s, event_handler::modifier m, int x, int y) {}
		void mouse_wheel(event_handler::wheel w, event_handler::modifier m, int x, int y) {}
		void key_typed(unsigned char c, event_handler::modifier m, int x, int y) {}
		void key_released(unsigned char c, event_handler::modifier m, int x, int y) {}
		void touch_performed(int x, int y, int finger_id, event_handler::action a) {}
	};
};

using android_window = window<pool_behaviour, android_layer>;

}  // ui


class scene_window : public ui::android_window
{
public:
	using base = ui::android_window;
	using parameters = base::parameters;

	scene_window(parameters const & params);

	void display() override;
	void reshape(int w, int h) override;

private:
	camera _cam;
	mesh _sphere;
	program _shaded;
	int _flat_position_a, _flat_normal_a;
};

scene_window::scene_window(parameters const & params)
	: base{params}
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
	geometry(w, h);
	_cam = camera{radians(70.0f), aspect_ratio(), 0.01, 1000};
	_cam.position = vec3{1,1,5};
	_cam.look_at(vec3{0,0,0});
	base::reshape(w, h);
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
}


scene_window * w = nullptr;

// scene api
void create(int width, int height);
void destroy();
void display();
void reshape(int width, int height);


void create(int width, int height)  // vola sa vzdi pri vytvoreni egl kontextu
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

void display()
{
	assert(w && "invalid scene");
	w->loop_step();
}

void reshape(int width, int height)
{
	assert(w && "invalid scene");
	static_cast<ui::android_layer *>(w)->reshape(width, height);
}


extern "C" {

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_create(JNIEnv * env, jobject thiz,
	jint width, jint height)
{
	create(width, height);
}

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_destroy(JNIEnv * env, jobject thiz)
{
	destroy();
}

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_reshape(JNIEnv * env, jobject thiz,
	jint width, jint height)
{
	reshape(width, height);
}

JNIEXPORT void JNICALL Java_org_libgl_wrapper_NativeScene_display(JNIEnv * env, jobject thiz)
{
	display();
}

}  // extern "C"
