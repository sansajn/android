# vytvori android-armv7a toolchain (co je zhodne s arm toolchain-om)
API_LEVEL=19
INSTALL_DIR=/home/ja/opt/android$API_LEVEL-armv7a
NDK=~/opt/android-ndk-r10e

$NDK/build/tools/make-standalone-toolchain.sh --arch=arm --platform=android-$API_LEVEL --install-dir=$INSTALL_DIR
