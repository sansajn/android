#pragma once
#include "platform_gl.h"

GLuint compile_shader(GLenum const type, GLchar const * source, GLint const length);
GLuint link_program(GLuint const vertex_shader, GLuint const fragment_shader);
GLuint build_program(GLchar const * vertex_shader_source, GLint const vertex_shader_source_length, GLchar const * fragment_shader_source, GLint const fragment_shader_source_length);
GLint validate_program(GLuint const program);
