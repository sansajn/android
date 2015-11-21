#include "texture.h"
#include "platform_gl.h"
#include <assert.h>

GLuint load_texture(GLsizei const width, GLsizei const height, GLenum const type, GLvoid const * pixels)
{
	GLuint texture_object_id;
	glGenTextures(1, &texture_object_id);
	assert(texture_object_id != 0);
	
	glBindTexture(GL_TEXTURE_2D, texture_object_id);

	// TODO: zaujimave, pri zmenseni pouziva mip-mapy a pri zvecseni linearnu interpolaciu
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, type, width, height, 0, type, GL_UNSIGNED_BYTE, pixels);
	glGenerateMipmap(GL_TEXTURE_2D);

	glBindTexture(GL_TEXTURE_2D, 0);
	return texture_object_id;
}
