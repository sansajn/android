project(cpp11) 
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -llog")
set(LIBRARY_OUTPUT_PATH "${PROJECT_SOURCE_DIR}/../jniLibs/${ARCH}")
add_library(cpp11 SHARED cpp11.cpp)

