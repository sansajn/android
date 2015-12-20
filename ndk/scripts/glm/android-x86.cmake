include(CMakeForceCompiler)
set(CMAKE_SYSTEM_NAME Linux)
set(ARCH x86)  # x86, arm, ...
CMAKE_FORCE_C_COMPILER(i686-linux-android-gcc GNU)
CMAKE_FORCE_CXX_COMPILER(i686-linux-android-g++ GNU)
