#pragma once
#include "platform_gl.h"

GLuint load_png_asset_into_texture(char const * relative_path);
GLuint build_program_from_assets(char const * vertex_shader_path, char const * fragment_shader_path);
