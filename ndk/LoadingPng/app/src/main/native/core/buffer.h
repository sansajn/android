#pragma once
#include "platform_gl.h"

#define BUFFER_OFFSET(i) ((void *)(i))

GLuint create_vbo(GLsizeiptr const size, GLvoid const * data, GLenum const usage);
