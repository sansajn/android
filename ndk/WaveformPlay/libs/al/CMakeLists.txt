cmake_minimum_required(VERSION 3.0)

project(al)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -g")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS}")

set(srcs 
	audio.cpp
	blob_wave.cpp
	waveform.cpp
)

add_library(al STATIC ${srcs})
