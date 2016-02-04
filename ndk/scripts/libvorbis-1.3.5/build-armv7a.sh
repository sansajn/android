#!/bin/bash 

ARCH=armv7a
HOST=arm-linux-androideabi
TOOLCHAIN=~/opt/android19-$ARCH
SYSROOT=$TOOLCHAIN/sysroot

if [ -d build/$ARCH ]; then
	rm -rf build/$ARCH
fi

mkdir -p build/$ARCH
cd build/$ARCH

PATH=$TOOLCHAIN/bin:$PATH

../../configure \
	--host=$HOST \
	--prefix=$SYSROOT/usr \
	--with-sysroot=$SYSROOT \
	--disable-shared \
	CFLAGS='-march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16'

NPROC=`nproc`
make -j$NPROC

make install
