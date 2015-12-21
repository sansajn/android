ARCH=arm
TOOLCHAIN=/home/ja/opt/android19-$ARCH
SYSROOT=$TOOLCHAIN/sysroot
HOST=arm-linux-androideabi

if [ -d "build/$ARCH" ]; then
	rm -rf build/$ARCH
fi
mkdir -p build/$ARCH
cd build/$ARCH
PATH=$TOOLCHAIN/bin:$PATH

../../configure \
	--host=$HOST \
	--prefix=$SYSROOT/usr \
	--with-sysroot=$SYSROOT \
	--disable-largefile \
	--disable-hdri \
	--disable-opencl \
	--disable-openmp \
	--disable-shared \
	--disable-docs \
	--without-threads \
	--without-modules \
	--without-gvc \
	--without-x \
	--without-perl \
	--without-dps \
	--without-fftw \
	--without-fpx \
	--without-djvu \
	--without-fontconfig \
	--without-freetype \
	--without-gslib \
	--without-jbig \
	--without-lcms \
	--with-jpeg \
	--without-openjp2 \
	--without-lqr \
	--without-lzma \
	--without-openexr \
	--without-pango \
	--with-png \
	--without-rsvg \
	--without-tiff \
	--without-webp \
	--without-bzlib \
	--without-zlib \
	--without-wmf \
	--without-xml

CPU_COUNT=`nproc`
make -j$CPU_COUNT V=1

make install
