# arm build script

ARCH=arm

if [ ! -d "build/$ARCH" ]; then
	mkdir -p build/$ARCH
fi

PATH=~/opt/android19-$ARCH/bin:$PATH
cd build/$ARCH

if [ ! -f "Makefile" ]; then
	cmake -DCMAKE_TOOLCHAIN_FILE=../../android-$ARCH.cmake -DCMAKE_INSTALL_PREFIX=~/opt/android19-$ARCH/sysroot/usr ../..
fi

make -j4
