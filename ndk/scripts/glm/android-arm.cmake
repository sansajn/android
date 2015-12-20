include(CMakeForceCompiler)
set(CMAKE_SYSTEM_NAME Linux)
set(ARCH arm)  # x86, arm, ...
CMAKE_FORCE_C_COMPILER(arm-linux-androideabi-gcc GNU)
CMAKE_FORCE_CXX_COMPILER(arm-linux-androideabi-g++ GNU)
