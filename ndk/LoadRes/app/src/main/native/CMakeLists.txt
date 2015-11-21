cmake_minimum_required(VERSION 3.0)

project(load)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -llog -landroid")
set(LIBRARY_OUTPUT_PATH "${PROJECT_SOURCE_DIR}/../jniLibs/${ARCH}")

add_library(load SHARED load.cpp)
