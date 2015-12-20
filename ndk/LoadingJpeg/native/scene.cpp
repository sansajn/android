// vyrenderuje png obrazok
#include <cassert>
#include <jni.h>
#include <android/log.h>
#include "gl/gles2/mesh_gles2.hpp"
#include "gl/gles2/program_gles2.hpp"
#include "gl/gles2/texture_loader_gles2.hpp"

#define LOG_TAG "loadingjpeg"

std::string texture_path = "/sdcard/test_files/lena_full_half_align4.jpg";

char const * textured_shader_source = R"(
	#ifdef _VERTEX_
	attribute vec3 position;
	varying vec2 st;
	void main() {
		st = position.xy/2.0 + 0.5;
		gl_Position = vec4(position, 1);
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

gles2::mesh * quad = nullptr;
gles2::shader::program * prog = nullptr;
gles2::texture2d * tex = nullptr;

gles2::mesh * make_quad()
{
	GLfloat vertices[] = {
		-1, -1, 0,
		 1, -1, 0,
		 1,  1, 0, 
		-1,  1, 0 
	};
	
	GLuint indices[] = {0, 1, 2,  2, 3, 0};
	
	GLint position_loc = glGetAttribLocation(prog->id(), "position");
	assert(position_loc != -1 && "unknown attribute");
	
	gles2::mesh * result = new gles2::mesh{vertices, sizeof(vertices), indices, 6};
	result->append_attribute(gles2::attribute(position_loc, 3, GL_FLOAT, 3*sizeof(GLfloat)));
	return result;
}

void setup_scene()
{
	prog = new gles2::shader::program{};
	try {
		prog->from_memory(textured_shader_source);
	}
	catch (std::exception & e) {
		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", e.what());
		assert(0);
	}

	quad = make_quad();
	tex = new gles2::texture2d{gles2::texture_from_file(texture_path)};
}

void render()
{
	glClear(GL_COLOR_BUFFER_BIT);
	prog->use();
	tex->bind(0);
	prog->uniform_variable("s", 0);
	quad->render();
}

void cleanup_scene()
{
	delete tex;
	delete prog;
	delete quad;
}

extern "C" {

JNIEXPORT void JNICALL Java_org_example_loadingjpeg_SceneWrapper_init(
	JNIEnv * env, jobject obj, jint width, jint height)
{
	setup_scene();
}

JNIEXPORT void JNICALL Java_org_example_loadingjpeg_SceneWrapper_free(
	JNIEnv * env, jobject obj)
{
	cleanup_scene();
}

JNIEXPORT void JNICALL Java_org_example_loadingjpeg_SceneWrapper_render(
	JNIEnv* env, jobject obj)
{
	render();
}

}  // extern "C"
