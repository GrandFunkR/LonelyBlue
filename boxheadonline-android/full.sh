ant clean release
adb shell pm uninstall com.roadhouse.boxheadonline
adb install bin/MainActivity-release.apk
adb shell am start -n com.roadhouse.boxheadonline/com.roadhouse.boxheadonline.MainActivity
