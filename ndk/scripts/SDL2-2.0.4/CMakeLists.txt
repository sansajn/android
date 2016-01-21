# SDL2-2.0.4 build script
cmake_minimum_required(VERSION 3.0) 
project(SDL2 C)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -DGL_GLEXT_PROTOTYPES")

include_directories("include")

file(GLOB SOURCE_FILES 
	"src/*.c"
	"src/audio/*.c"
	"src/audio/android/*.c"
	"src/audio/dummy/*.c"
	"src/atomic/dummy/SDL_atomic.c"
	"src/atomic/dummy/SDL_spinlock.c.arm"
	"src/core/android/*.c"
	"src/cpuinfo/*.c"
	"src/dynapi/*.c"
	"src/events/*.c"
	"src/file/*.c"
	"src/haptic/*.c"
	"src/haptic/dummy/*.c"
	"src/joystick/*.c"
	"src/joystick/android/*.c"
	"src/loadso/dlopen/*.c"
	"src/power/*.c"
	"src/power/android/*.c"
	"src/filesystem/android/*.c"
	"src/render/*.c"
	"src/render/*/*.c"
	"src/stdlib/*.c"
	"src/thread/*.c"
	"src/thread/pthread/*.c"
	"src/timer/*.c"
	"src/timer/unix/*.c"
	"src/video/*.c"
	"src/video/android/*.c"
	"src/test/*.c"
)

add_library(SDL2 STATIC ${SOURCE_FILES})

install(TARGETS SDL2 ARCHIVE DESTINATION lib)

file(GLOB INCLUDE_FILES "include/*.h")
install(FILES ${INCLUDE_FILES} DESTINATION include/SDL2)
