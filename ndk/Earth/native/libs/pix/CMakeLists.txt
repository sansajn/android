cmake_minimum_required(VERSION 3.0)

project(pix)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")

set(pix_srcs 
	pix_jpeg.cpp
	pix_png.cpp
	pix.cpp)

add_library(pix STATIC ${pix_srcs})
