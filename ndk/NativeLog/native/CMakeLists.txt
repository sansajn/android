cmake_minimum_required(VERSION 3.0)

project(scene)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -g")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -llog -landroid")
set(LIBRARY_OUTPUT_PATH "${PROJECT_SOURCE_DIR}/../app/src/main/jniLibs/${ARCH}")

add_library(scene SHARED android_log_streambuf.cpp)
