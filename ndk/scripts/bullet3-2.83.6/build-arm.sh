#!/bin/bash

if [ ! -d "build/arm" ]; then
	mkdir -p build/arm
fi

PATH=~/opt/android19-arm/bin:$PATH
cd build/arm

if [ ! -f "Makefile" ]; then
	cmake \
		-DCMAKE_TOOLCHAIN_FILE=../../android-arm.cmake \
		-DCMAKE_INSTALL_PREFIX=~/opt/android19-arm/sysroot/usr \
		-DBUILD_BULLET3=off \
		-DBUILD_EXTRAS=off \
		-DBUILD_UNIT_TESTS=off \
		-DBUILD_BULLET2_DEMOS=off \
		-DBUILD_OPENGL3_DEMOS=off ../..
fi

make -j4
