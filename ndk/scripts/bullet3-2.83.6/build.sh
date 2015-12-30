#!/bin/bash

if [ ! -d "build/x86" ]; then
	mkdir -p build/x86
fi

PATH=~/opt/android19-x86/bin:$PATH
cd build/x86

if [ ! -f "Makefile" ]; then
	cmake \
		-DCMAKE_TOOLCHAIN_FILE=../../android-x86.cmake \
		-DCMAKE_INSTALL_PREFIX=~/opt/android19-x86/sysroot/usr \
		-DBUILD_BULLET3=off \
		-DBUILD_EXTRAS=off \
		-DBUILD_UNIT_TESTS=off \
		-DBUILD_BULLET2_DEMOS=off \
		-DBUILD_OPENGL3_DEMOS=off ../..
fi

make -j4
