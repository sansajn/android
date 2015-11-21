#include <cassert>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "scene"

using namespace std;

char const * vertex_shader_source = R"(
	attribute vec3 position;
	attribute vec3 color;
	varying vec3 vert_color;
	void main() {
		vert_color = color;
		gl_Position = vec4(position, 1.0);
	}
)";

char const * fragment_shader_source = R"(
	precision mediump float;
	varying vec3 vert_color;
	void main() {
		gl_FragColor = vec4(vert_color,1);
	}
)";

GLuint vso = 0, fso = 0, prog = 0, vbo = 0;
GLint position_attr = 0, color_attr = 0;

void setup_scene()
{
	vso = glCreateShader(GL_VERTEX_SHADER);
	assert(vso);
	glShaderSource(vso, 1, &vertex_shader_source, nullptr);
	glCompileShader(vso);
	assert(glGetError() == GL_NO_ERROR);
	
	fso = glCreateShader(GL_FRAGMENT_SHADER);
	assert(fso);
	glShaderSource(fso, 1, &fragment_shader_source, nullptr);
	glCompileShader(fso);
	assert(glGetError() == GL_NO_ERROR);
	
	prog = glCreateProgram();
	glAttachShader(prog, vso);
	glAttachShader(prog, fso);
	glLinkProgram(prog);
	assert(glGetError() == GL_NO_ERROR);
	
	GLfloat verts[] = {  // position:3, color:3
		-.5f, -.5f, .0f,   1.0f,  .0f,  .0f,
		 .5f, -.5f, .0f,    .0f, 1.0f,  .0f,
		 .0f,  .5f, .0f,    .0f,  .0f, 1.0f};
	
	glGenBuffers(1, &vbo);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glBufferData(GL_ARRAY_BUFFER, (3+3)*3*sizeof(GLfloat), verts, GL_STATIC_DRAW);
	
	position_attr = glGetAttribLocation(prog, "position");
	color_attr = glGetAttribLocation(prog, "color");
}

void cleanup_scene()
{
	glDeleteBuffers(1, &vbo);
	glDeleteShader(vso);
	glDeleteShader(fso);
	glDeleteProgram(prog);
}

void render()
{
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glVertexAttribPointer(position_attr, 3, GL_FLOAT, GL_FALSE, (3+3)*sizeof(GLfloat), 0);
	glEnableVertexAttribArray(position_attr);
	
	glVertexAttribPointer(color_attr, 3, GL_FLOAT, GL_FALSE, (3+3)*sizeof(GLfloat), (void *)(3*sizeof(GLfloat)));
	glEnableVertexAttribArray(color_attr);
	
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glUseProgram(prog);
	glDrawArrays(GL_TRIANGLES, 0, 3);
}

extern "C" {

JNIEXPORT void JNICALL Java_org_example_trianglegles2_SceneLib_init(
	JNIEnv * env, jobject obj, jint width, jint height)
{
	setup_scene();
}

JNIEXPORT void JNICALL Java_org_example_trianglegles2_SceneLib_free(
	JNIEnv * env, jobject obj)
{
	cleanup_scene();
}

JNIEXPORT void JNICALL Java_org_example_trianglegles2_SceneLib_render(
	JNIEnv * env, jobject obj)
{
	render();
}

}  // extern "C"
