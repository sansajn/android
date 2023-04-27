package com.example.sdcarddcim

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sdcarddcim.ui.theme.SDCardDCIMTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SDCardDCIMTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }

        // print out DCIM folder location from internal storage (e.g. /storage/emulated)
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath  //= /storage/emulated/0
        val directoryDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)  //= /storage/emulated/0/DCIM
        val directoryPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)  //= /storage/emulated/0/Pictures
        Log.d("main", "external-storage=$externalStorage")
        Log.d("main", "DIRECTORY_DCIM=$directoryDCIM")
        Log.d("main", "DIRECTORY_PICTURES=$directoryPictures")

        // print out DCIM folder location from SD Card storage (e.g. /storage/9C33-6BBD)
        Log.d("main", "MediaStore.VOLUME_EXTERNAL_PRIMARY=${MediaStore.VOLUME_EXTERNAL_PRIMARY}")  //= external_primary
        Log.d("main", "MediaStore.VOLUME_EXTERNAL=${MediaStore.VOLUME_EXTERNAL}")  //= external
        Log.d("main", "MediaStore.Images.Media.EXTERNAL_CONTENT_URI=${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}")  //= content://media/external/images/media

        val externalVolumes = MediaStore.getExternalVolumeNames(baseContext)  //= [external_primary, 12fd-0e09]

        Log.d("main", "external volumes:")
        externalVolumes.forEach {
            Log.d("main", "  $it")
        }

        val sdCards = externalVolumes.filter {
            it != MediaStore.VOLUME_EXTERNAL_PRIMARY && it != MediaStore.VOLUME_EXTERNAL
        }

        assert(sdCards.isNotEmpty())  // we expect some SD Card present

        val sdcardImagesUri = MediaStore.Images.Media.getContentUri(sdCards[0])  //= content://media/1004-2d1b/images/media
        Log.d("main", "sdcardImagesUri=$sdcardImagesUri")

        // acquire permission to access SD Card
        if (checkPermission()) {
            Log.d("main", "external storage permission already granted")
            listExternalDcim()
        }
        else {
            Log.d("main", "external storage permission not granted, request")
            requestPermission()
        }
    }

    private fun listExternalDcim() {
        // figure out DCIM path e.g. /storage/9C33-6BBD/DCIM
        val externalVolumes = MediaStore.getExternalVolumeNames(baseContext)
        val sdCardVolumes = externalVolumes.filter {
            it != MediaStore.VOLUME_EXTERNAL_PRIMARY && it != MediaStore.VOLUME_EXTERNAL
        }
        assert(sdCardVolumes.isNotEmpty())  // we expect some SD Card present

        val sdCardDcimPath = "/storage/${sdCardVolumes[0].uppercase()}/DCIM"
        Log.d("main", "sdCardDcimPath=$sdCardDcimPath")

        // list files
        Log.d("main", "photo-list:")
        File(sdCardDcimPath).walk().forEach {
            Log.d("main", "  ${it.absolutePath}")
        }
    }

    private fun requestPermission() {
        Log.d(TAG, "requestPermission(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)")
        with(Intent()) {
            action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            storageActivityResultLauncher.launch(this)
        }
    }

    // this is called after external storage access granted
    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (checkPermission()) {
            Log.d(TAG, "external storage permission granted")
            listExternalDcim()
        }
        else {
            Log.d(TAG, "external storage permission denied")
        }
    }

    private fun checkPermission(): Boolean {
        return Environment.isExternalStorageManager()
    }

    companion object {
        val TAG = "main"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SDCardDCIMTheme {
        Greeting("Android")
    }
}