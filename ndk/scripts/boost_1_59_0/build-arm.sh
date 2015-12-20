# build for android arm
ARCH=arm
PATH=~/opt/android19-$ARCH/bin:$PATH
echo "using gcc : android : arm-linux-androideabi-g++ ; " > tools/build/src/user-config.jam
./bootstrap.sh --prefix=/home/ja/opt/android19-$ARCH/sysroot/usr --without-libraries=atomic,chrono,container,context,coroutine,coroutine2,date_time,exception,filesystem,graph,graph_parallel,iostreams,locale,log,math,mpi,program_options,python,random,regex,serialization,signals,system,test,thread,timer,wave
./b2 toolset=gcc-android install
