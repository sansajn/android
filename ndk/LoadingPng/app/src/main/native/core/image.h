#pragma once
#include "platform_gl.h"

typedef struct {
	int const width;
	int const height;
	int const size;
	GLenum const gl_color_format;
	void const * data;
} RawImageData;

// returns the decoded image data, or aborts i there's an error during decoding
RawImageData get_raw_image_data_from_png(void const * png_data, int const png_data_size);
void release_raw_image_data(RawImageData const * data);
