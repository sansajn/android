#include "buffer.h"
#include "platform_gl.h"
#include <assert.h>
#include <stdlib.h>

GLuint create_vbo(GLsizeiptr const size, GLvoid const * data, GLenum const usage)
{
	assert(data != NULL);
	GLuint vbo_object;
	glGenBuffers(1, &vbo_object);
	assert(vbo_object != 0);
	
	glBindBuffer(GL_ARRAY_BUFFER, vbo_object);
	glBufferData(GL_ARRAY_BUFFER, size, data, usage);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	
	return vbo_object;
}
