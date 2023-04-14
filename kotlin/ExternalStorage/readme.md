# About

External storage access sample in Kotlin language for Android 13+. The sample lists all photos from inside DCIM folder (usually /storage/emulated/0/DCIM) for which require runtime permission.

> should work for Android 11+, but not tested

in `AndroidManifest.xml` add

```xml
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```

to allow external storage permission. 

Then in a fragment implementation we need to ask for a permission before listing gallery directory with `requestPermission()` call. Returning to the application from granting permission activity results to  `registerForActivityResult() {}` handler and final gallery directory listing.

In case external storage access already granted `checkPermission()` call returns true and we can continue with gallery directory listing.

> TODO: where are camera pictures stored in a `DCIM/Camera` or `Pictures`?
