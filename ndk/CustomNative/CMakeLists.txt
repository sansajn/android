cmake_minimum_required(VERSION 3.0)

project(native)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++11")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -llog -landroid")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/android/libs/${ARCH}")

set(srcs
	main.cpp
)

add_library(native SHARED ${srcs})
