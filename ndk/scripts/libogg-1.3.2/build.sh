ARCH=x86
HOST=i686-linux-android
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
	--disable-shared

NPROC=`nproc`
make -j$NPROC

make install