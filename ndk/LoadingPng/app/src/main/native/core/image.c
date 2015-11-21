#include "image.h"
#include "platform/platform_log.h"
#include <assert.h>
#include <png.h>
#include <string.h>
#include <stdlib.h>

typedef struct {
	png_byte const * data;
	png_size_t const size;
}  DataHandle;

typedef struct {
	DataHandle const data;
	png_size_t offset;
} ReadDataHandle;

typedef struct {
	png_uint_32 const width;
	png_uint_32 const height;
	int const color_type;
}  PngInfo;

static void read_png_data_callback(png_structp png_ptr, png_byte * png_data, png_size_t read_length);
static PngInfo read_and_update_info(png_structp const png_ptr, png_infop const info_ptr);
static DataHandle read_entire_png_image(png_structp const png_ptr, png_infop const info_ptr, png_uint_32 const height);
static GLenum get_gl_color_format(int const png_color_format);

RawImageData get_raw_image_data_from_png(void const * png_data, int const png_data_size)
{
	assert(png_data != NULL && png_data_size > 8);
	assert(png_check_sig((void *)png_data, 8));
	
	png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	assert(png_ptr != NULL);
	png_infop info_ptr = png_create_info_struct(png_ptr);
	assert(info_ptr != NULL);
	
	ReadDataHandle png_data_handle = (ReadDataHandle){{png_data, png_data_size}, 0};
	png_set_read_fn(png_ptr, &png_data_handle, read_png_data_callback);
	
	if (setjmp(png_jmpbuf(png_ptr)))
		CRASH("Error reading PNG file!");
	
	PngInfo const png_info = read_and_update_info(png_ptr, info_ptr);
	DataHandle const raw_image = read_entire_png_image(png_ptr, info_ptr, png_info.height);
	
	png_read_end(png_ptr, info_ptr);
	png_destroy_read_struct(&png_ptr, &info_ptr, NULL);
	
	return (RawImageData){png_info.width, png_info.height, raw_image.size, 
		get_gl_color_format(png_info.color_type), raw_image.data};
}

void release_raw_image_data(RawImageData const * data)
{
	assert(data != NULL);
	free((void *)data->data);
}

void read_png_data_callback(png_structp png_ptr, png_byte * raw_data, png_size_t read_length)
{
	ReadDataHandle * handle = png_get_io_ptr(png_ptr);
	png_byte const * png_src = handle->data.data + handle->offset;
	memcpy(raw_data, png_src, read_length);
	handle->offset += read_length;
}

PngInfo read_and_update_info(png_structp const png_ptr, png_infop const info_ptr)
{
	png_uint_32 width, height;
	int bit_depth, color_type;
	
	png_read_info(png_ptr, info_ptr);
	png_get_IHDR(png_ptr, info_ptr, &width, &height, &bit_depth, &color_type, NULL, NULL, NULL);
	
	// convert transparency to full alpha
	if (png_get_valid(png_ptr, info_ptr, PNG_INFO_tRNS))
		png_set_tRNS_to_alpha(png_ptr);
	
	// convert grayscale, if needed
	if (color_type == PNG_COLOR_TYPE_GRAY && bit_depth < 8)
		png_set_expand_gray_1_2_4_to_8(png_ptr);
	
	// convert paletted images, if needed
	if (color_type == PNG_COLOR_TYPE_PALETTE)
		png_set_palette_to_rgb(png_ptr);
	
	// add alpha channel, if there is none
	if (color_type == PNG_COLOR_TYPE_PALETTE || color_type == PNG_COLOR_TYPE_RGB)
		png_set_add_alpha(png_ptr, 0xff, PNG_FILLER_AFTER);
	
	// ensure 8-bit packing
	if (bit_depth < 8)
		png_set_packing(png_ptr);
	else if (bit_depth == 16)
		png_set_scale_16(png_ptr);
	
	png_read_update_info(png_ptr, info_ptr);
	
	//read the newcolor type fter updates have been made
	color_type = png_get_color_type(png_ptr, info_ptr);
	
	return (PngInfo){width, height, color_type};
}

DataHandle read_entire_png_image(png_structp const png_ptr, png_infop const info_ptr, png_uint_32 const height)
{
	png_size_t const row_size = png_get_rowbytes(png_ptr, info_ptr);
	int const data_length = row_size * height;
	assert(row_size > 0);
	
	png_byte * raw_image = malloc(data_length);
	assert(raw_image != NULL);
	
	png_byte * row_ptrs[height];  // VAL (variable array length)
	
	png_uint_32 i;
	for (i = 0; i < height; ++i)
		row_ptrs[i] = raw_image + i * row_size;
	
	png_read_image(png_ptr, &row_ptrs[0]);
	
	return (DataHandle){raw_image, data_length};
}

GLenum get_gl_color_format(int const png_color_format)
{
	assert(png_color_format == PNG_COLOR_TYPE_GRAY
		|| png_color_format == PNG_COLOR_TYPE_RGB_ALPHA
		|| png_color_format == PNG_COLOR_TYPE_GRAY_ALPHA);
	
	switch (png_color_format)
	{
		case PNG_COLOR_TYPE_GRAY:
			return GL_LUMINANCE;
		case PNG_COLOR_TYPE_RGB_ALPHA:
			return GL_RGBA;
		case PNG_COLOR_TYPE_GRAY_ALPHA:
			return GL_LUMINANCE_ALPHA;
	}
	
	return 0;
}
