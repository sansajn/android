ARCH=x86
HOST=i686-linux-android
PREFIX=~/opt/android19-$ARCH/sysroot/usr

if [ -d "build/$ARCH" ]; then
	rm -rf build/$ARCH
fi
mkdir -p build/$ARCH
cd build/$ARCH

export PATH=~/opt/android19-$ARCH/bin:$PATH
../../configure --host=$HOST --prefix=$PREFIX --with-sysroot=$PREFIX --disable-shared

CPU_COUNT=`nproc`
make -j$CPU_COUNT
# type 'make install' for install (modify path if needed)
