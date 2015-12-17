# popis skriptu ...

if [ ! -d "build" ]; then
	mkdir build
fi

PATH=~/opt/android19-x86/bin:$PATH
cd build

if [ ! -f "Makefile" ]; then
	cmake -DCMAKE_TOOLCHAIN_FILE=../android-x86.cmake -DCMAKE_INSTALL_PREFIX=~/opt/android19-x86 ..
fi

make -j4
