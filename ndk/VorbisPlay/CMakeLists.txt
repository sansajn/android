cmake_minimum_required(VERSION 3.0)

project(sound)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++11")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -llog -landroid -lOpenSLES")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/android/libs/${ARCH}")

include_directories(libs)

add_subdirectory(libs/al)

set(srcs
	main.cpp
)

add_library(sound SHARED ${srcs})
target_link_libraries(sound al openal.a)
