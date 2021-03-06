# zavolaj ako
#	$ mkdir build && cd build
#	$ cmake \
#		-DCMAKE_TOOLCHAIN_FILE=../android-x86.cmake \
#		-DCMAKE_INSTALL_PREFIX=/tmp/android19-x86/sysroot/usr \
#		..
cmake_minimum_required(VERSION 3.0)

project(loadjpeg)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++11")
set(CMAKE_SHARED_LINKER_FLAGS 
	"${CMAKE_SHARED_LINKER_FLAGS} -llog -lGLESv2 -landroid -lz")
set(LIBRARY_OUTPUT_PATH "${PROJECT_SOURCE_DIR}/../app/src/main/jniLibs/${ARCH}")

include_directories(libs)

set(loadjpeg_source
	scene.cpp
	libs/gl/gles2/default_shader_gles2.cpp
	libs/gl/gles2/mesh_gles2.cpp
	libs/gl/gles2/program_gles2.cpp
	libs/gl/gles2/texture_gles2.cpp
	libs/gl/gles2/texture_loader_gles2.cpp
	libs/pix/pix.cpp
	libs/pix/pix_jpeg.cpp
)

add_library(loadjpeg SHARED ${loadjpeg_source})
target_link_libraries(loadjpeg jpeg.a)
