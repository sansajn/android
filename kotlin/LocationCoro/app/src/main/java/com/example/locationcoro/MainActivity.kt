package com.example.locationcoro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.locationcoro.ui.theme.LocationCoroTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			LocationCoroTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Greeting("Android")
				}
			}
		}

		// ask for location permissions
		val locationPermissionRequest = registerForActivityResult(
			ActivityResultContracts.RequestMultiplePermissions()  // TODO: is this blocking or async?
		) { permissions ->
			when {
				permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
					// Precise location access granted.
					Log.d("Location", "ACCESS_FINE_LOCATION granted")
				}
				permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
					// Only approximate location access granted.
					Log.d("Location", "ACCESS_COARSE_LOCATION granted")
				}
				permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
					Log.d("Location", "ACCESS_BACKGROUND_LOCATION granted")
				}
				else -> {
					// No location access granted.
				}
			}
		}

		// request for permissions if not already have them
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_COARSE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			locationPermissionRequest.launch(arrayOf(
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION))
			return
		}
		else
			Log.d(TAG,"already have required permissions")

		lifecycleScope.launch {
			performBackgroundTask()
		}

		val locationClient = LocationServices.getFusedLocationProviderClient(this)

		lifecycleScope.launch(Dispatchers.IO) {
			val result = locationClient.lastLocation.await()
			if (result == null) {
				Log.d(TAG, "No last known location. Try fetching the current location first")
			} else {
				Log.d(TAG,"Current location is \n" + "lat : ${result.latitude}\n" +
					"long : ${result.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
			}
		}
	}

	private fun performBackgroundTask() {
		Log.d(TAG, "hello from coroutine!")
	}

	companion object {
		const val TAG = "LocationCoro"
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
	LocationCoroTheme {
		Greeting("Android")
	}
}