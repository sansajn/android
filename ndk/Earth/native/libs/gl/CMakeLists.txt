cmake_minimum_required(VERSION 3.0)

project(gl)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -g")

set(gl_srcs 
	camera.cpp 
	gles2/default_shader_gles2.cpp
	gles2/mesh_gles2.cpp 
	gles2/program_gles2.cpp 
	gles2/texture_gles2.cpp 
	gles2/texture_loader_gles2.cpp
	gles2/touch_joystick_gles2.cpp)

add_library(gl STATIC ${gl_srcs})
