cmake_minimum_required(VERSION 3.0)

project(tools)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -g")

set(tools_srcs 
	directory_iterator.cpp)

add_library(tools STATIC ${tools_srcs})
