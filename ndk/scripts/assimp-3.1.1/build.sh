#!/bin/bash

ARCH=x86
TOOLCHAIN=/home/ja/opt/android19-$ARCH
SYSROOT=$TOOLCHAIN/sysroot

if [ ! -d "build/$ARCH" ]; then
	mkdir -p build/$ARCH
fi

PATH=$TOOLCHAIN/bin:$PATH
cd build/$ARCH

if [ ! -f "Makefile" ]; then
	cmake \
		-DCMAKE_TOOLCHAIN_FILE=../../android-$ARCH.cmake \
		-DCMAKE_INSTALL_PREFIX=$SYSROOT/usr \
		-DASSIMP_BUILD_ASSIMP_TOOLS=off \
		-DASSIMP_BUILD_SAMPLES=off \
		-DASSIMP_BUILD_STATIC_LIB=on \
		-DBUILD_SHARED_LIBS=off \
		../..
fi

CPU_COUNT=`nproc`
make -j$CPU_COUNT

make install
