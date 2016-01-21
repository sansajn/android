#!/bin/bash 

ARCH=x86
TOOLCHAIN=~/opt/android19-$ARCH

if [ ! -d "build/$ARCH" ]; then
	mkdir -p build/$ARCH
fi

PATH=$TOOLCHAIN/bin:$PATH
cd build/$ARCH

if [ ! -f "Makefile" ]; then
	cmake \
		-DCMAKE_TOOLCHAIN_FILE=../../android-$ARCH.cmake \
		-DCMAKE_INSTALL_PREFIX=$TOOLCHAIN/sysroot/usr \
		../..
fi

NPROC=`nproc`
make -j$NPROC

make install
