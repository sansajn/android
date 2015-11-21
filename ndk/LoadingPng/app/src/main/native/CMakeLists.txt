# zavolaj ako
#	$ mkdir build && cd build
#	$ cmake \
#		-DCMAKE_TOOLCHAIN_FILE=../x86_android.cmake \
#		-DCMAKE_INSTALL_PREFIX=/tmp/x86-android19/sysroot/usr \
#		..
cmake_minimum_required(VERSION 3.0)

project(loadpng) 

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -std=c99")

# libpng
set(libpng_path libs/libpng-1.6.18)
include_directories(${libpng_path} ${CMAKE_BINARY_DIR}/${libpng_path})
add_subdirectory(${libpng_path})

include_directories(${PROJECT_SOURCE_DIR} core platform)

set(CMAKE_SHARED_LINKER_FLAGS 
	"${CMAKE_SHARED_LINKER_FLAGS} -llog -lGLESv2 -landroid")

set(LIBRARY_OUTPUT_PATH "${PROJECT_SOURCE_DIR}/../jniLibs/${ARCH}")

set(loadpng_source
	renderer_wrapper.c
	core/asset_utils.c
	core/buffer.c
	core/game.c
	core/image.c
	core/shader.c
	core/texture.c
	platform/platform_asset_utils.c
	platform/platform_log.c
)

add_library(loadpng SHARED ${loadpng_source})
target_link_libraries(loadpng png16_static)

