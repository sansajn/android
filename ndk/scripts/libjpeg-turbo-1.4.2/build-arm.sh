# build libjpeg-turbo-1.4.2 kniznice pre android (arm)
ARCH=arm
HOST=arm-linux-androideabi
TOOLCHAIN=~/opt/android19-$ARCH
SYSROOT=$TOOLCHAIN/sysroot
ANDROID_INCLUDES="-I$SYSROOT/usr/include -I$TOOLCHAIN/include"
ANDROID_CFLAGS="--sysroot=$SYSROOT"

CPP=$TOOLCHAIN/bin/$HOST-cpp
AR=$TOOLCHAIN/bin/$HOST-ar
AS=$TOOLCHAIN/bin/$HOST-as
NM=$TOOLCHAIN/bin/$HOST-nm
CC=$TOOLCHAIN/bin/$HOST-gcc
LD=$TOOLCHAIN/bin/$HOST-ld
RANLIB=$TOOLCHAIN/bin/$HOST-ranlib
OBJDUMP=$TOOLCHAIN/bin/$HOST-objdump
STRIP=$TOOLCHAIN/bin/$HOST-strip

autoreconf -fiv

if [ -d buid/$ARCH ]; then
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
	CFLAGS="$ANDROID_INCLUDES $ANDROID_CFLAGS -O2 -g" \
	CPPFLAGS="$ANDROID_INCLUDES $ANDROID_CFLAGS" \
	LDFLAGS="$ANDROID_CFLAGS"

CPU_COUNT=`nproc`
make -j$CPU_COUNT

make install
 
