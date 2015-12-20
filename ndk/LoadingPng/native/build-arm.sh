ARCH=arm
mkdir -p build/$ARCH
cd build/$ARCH
PATH=~/opt/android19-$ARCH/bin:$PATH
if [ ! -f "Makefile" ]; then
	cmake -DCMAKE_TOOLCHAIN_FILE=../../android-$ARCH.cmake -DCMAKE_INSTALL_PREFIX=~/opt/android19-$ARCH/sysroot/usr ../..
fi
make -j4
