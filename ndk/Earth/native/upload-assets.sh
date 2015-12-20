DEST=/sdcard/.earth

ADB=$ANDROID_HOME/platform-tools/adb
$ADB shell mkdir -p $DEST
$ADB push -p assets $DEST
$ADB shell ls -la $DEST
