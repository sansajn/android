cmake_minimum_required(VERSION 3.0)

project(imagick)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -llog -landroid -lGLESv2 -lz")
set(LIBRARY_OUTPUT_PATH "${PROJECT_SOURCE_DIR}/../app/src/main/jniLibs/${ARCH}")

# image magick settings
set(SYSROOT /home/ja/opt/android19-x86/sysroot/usr)
include_directories(../ ${SYSROOT}/include/ImageMagick-6)
add_definitions(-DMAGICKCORE_QUANTUM_DEPTH=16 -DMAGICKCORE_HDRI_ENABLE=0)

include_directories(libs)
add_subdirectory(libs/gl)
add_subdirectory(libs/tools)

add_library(imagick SHARED imagick.cpp)
target_link_libraries(imagick Magick++-6.Q16 MagickWand-6.Q16 MagickCore-6.Q16 jpeg.a png12.a gl tools)
