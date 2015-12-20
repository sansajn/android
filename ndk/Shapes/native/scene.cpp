#include <cassert>
#include <glm/gtc/matrix_inverse.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtx/transform.hpp>
#include <GL/glew.h>
#include <jni.h>
#include <android/log.h>
#include "gl/shapes.hpp"
#include "gl/camera.hpp"
#include "gl/colors.hpp"
#include "gl/gles2/program_gles2.hpp"
#include "gl/gles2/mesh_gles2.hpp"
#include "gl/gles2/default_shader_gles2.hpp"

#define LOG_TAG "scene"

using namespace std;
using glm::mat4;
using glm::vec3;
using glm::mat3;
using glm::radians;
using glm::normalize;
using glm::inverseTranspose;
using gl::camera;
using gl::shape_generator;
using gles2::mesh;
using gles2::shader::program;


class scene_window
{
public:
	scene_window(int width, int height);
	void display();
	float aspect_ratio() const {return (float)_w/(float)_h;}

private:
	int _w, _h;
	mesh _cube;
	mesh _box;
	mesh _disk;
	mesh _cylinder;
	mesh _open_cylinder;
	mesh _cone;
	mesh _sphere;
	mesh _circle;
	mesh _ring;
	program _shaded;
	camera _cam;
};

scene_window::scene_window(int width, int height)
	: _w{width}
	, _h{height}
	, _cam{radians(70.0f), aspect_ratio(), 0.01, 1000}
{
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window{int,int}");
	shape_generator<mesh> shape;
	_cube = shape.cube();
	_box = shape.box(vec3{.5, 1, 0.5});
	_disk = shape.disk(.5);
	_cylinder = shape.cylinder(.5, .5, 30);
	_open_cylinder = shape.open_cylinder(.5, 1, 20);
	_cone = shape.cone(.5, 1);
	_sphere = shape.sphere(.5);
	_circle = shape.circle(.5);
	_ring = shape.ring(.25, .5, 30);
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window{int,int}:before_shader");
	_shaded.from_memory(gles2::flat_shaded_shader_source);
	_cam.position = vec3{2,2,5};
	_cam.look_at(vec3{0,0,0});
	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "scene_window{int,int}:end");
}

void scene_window::display()
{
//	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", "display()");
	vec3 light_pos = vec3{10, 20, 30};

	_shaded.use();

	GLint position_a = glGetAttribLocation(_shaded.id(), "position");
	GLint normal_a = glGetAttribLocation(_shaded.id(), "normal");
//	__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "position:%i, normal:%i", position_a, normal_a);

	// cube
	mat4 VP = _cam.view_projection();
	mat4 M = mat4{1};
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::gray);
	_shaded.uniform_variable("light_dir", normalize(light_pos));

	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	_cube.attribute_location({position_a, normal_a});
	_cube.render();

	// box
	M = translate(vec3{2, 0, -2});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::teal);
	_box.attribute_location({position_a, normal_a});
	_box.render();

	// disk
	M = translate(vec3{1, 0, 2});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::yellow);
	_disk.attribute_location({position_a, normal_a});
	_disk.render();

	// cylinder
	M = translate(vec3{-1.5, 0, -.4});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::olive);
	_cylinder.attribute_location({position_a, normal_a});
	_cylinder.render();

	// open cylinder
	M = translate(vec3{-.2, 0, -2});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::maroon);
	_open_cylinder.attribute_location({position_a, normal_a});
	_open_cylinder.render();

	// cone
	M = translate(vec3{-2, 0, 1.5});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::purple);
	_cone.attribute_location({position_a, normal_a});
	_cone.render();

	// sphere
	M = translate(vec3{-.7, 0, 1.8});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::blue_shades::cornflower_blue);
	_sphere.attribute_location({position_a, normal_a});
	_sphere.render();

	// circle
	M = translate(vec3{2, 0, 1});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::white);
	_circle.attribute_location({position_a, normal_a});
	_circle.render();

	// ring
	M = translate(vec3{-3, 0, .5});
	_shaded.uniform_variable("local_to_screen", VP*M);
	_shaded.uniform_variable("normal_to_world", mat3{inverseTranspose(M)});
	_shaded.uniform_variable("color", rgb::lime);
	_ring.attribute_location({position_a, normal_a});
	_ring.render();
}

scene_window * w = nullptr;


extern "C" {

JNIEXPORT void JNICALL Java_org_example_shapes_SceneWrapper_init(
	JNIEnv * env, jobject thiz, jint width, jint height)
{
	assert(!w && "already initialized");
	w = new scene_window{width, height};
}

JNIEXPORT void JNICALL Java_org_example_shapes_SceneWrapper_render(
	JNIEnv * env, jobject thiz)
{
	w->display();
}

JNIEXPORT void JNICALL Java_org_example_shapes_SceneWrapper_free(
	JNIEnv * env, jobject thiz)
{
	delete w;
	w = nullptr;
}

}  // extern "C"
