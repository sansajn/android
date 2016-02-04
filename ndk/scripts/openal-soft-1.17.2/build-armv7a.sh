#!/bin/bash 

ARCH=armv7a
TOOLCHAIN=~/opt/android19-$ARCH
HOST=arm-linux-androideabi

if [ ! -d "build/$ARCH" ]; then
	mkdir -p build/$ARCH
fi

PATH=$TOOLCHAIN/bin:$PATH
cd build/$ARCH

if [ ! -f "Makefile" ]; then
	cmake \
		-DCMAKE_TOOLCHAIN_FILE=../../XCompile-Android.txt \
		-DCMAKE_INSTALL_PREFIX=$TOOLCHAIN/sysroot/usr \
		-DHOST=$HOST \
		-DCMAKE_FIND_ROOT_PATH=$TOOLCHAIN \
		-DLIBTYPE=STATIC \
		-DALSOFT_UTILS=OFF \
		-DALSOFT_EXAMPLES=OFF \
		-DCMAKE_C_FLAGS='-march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16' \
		../..
fi

NPROC=`nproc`
make -j$NPROC

make install
