# spusti prikazom
# 	$ cmake -DCMAKE_TOOLCHAIN_FILE=../x86_android.cmake -DCMAKE_INSTALL_PREFIX=/tmp/x86-android19/sysroot/usr ..
include(CMakeForceCompiler)
set(CMAKE_SYSTEM_NAME Linux)
set(ARCH x86)  # x86, arm, ...
CMAKE_FORCE_C_COMPILER(i686-linux-android-gcc GNU)
CMAKE_FORCE_CXX_COMPILER(i686-linux-android-g++ GNU)
