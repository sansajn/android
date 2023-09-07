package com.example.location

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
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
import com.example.location.ui.theme.LocationTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			LocationTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Greeting("Android")
				}
			}
		}

		if (haveLocationPermissions())
			requestLocations()
		else
			askLocationPermissions()
	}

	private fun requestLocations() {
		locationClient = LocationServices.getFusedLocationProviderClient(this)
		requestLastLocation()
		startLocationUpdates()  // regular location updates
	}

	private fun askLocationPermissions() {
		// ask for location permissions
		val locationPermissionRequest = registerForActivityResult(
			ActivityResultContracts.RequestMultiplePermissions()
		) { permissions ->
			when {
				permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
					// Precise location access granted.
					Log.d("Location", "ACCESS_FINE_LOCATION granted")
					requestLocations()
				}
				permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
					// Only approximate location access granted.
					Log.d("Location", "ACCESS_COARSE_LOCATION granted")
					requestLocations()
				}
				permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
					Log.d("Location", "ACCESS_BACKGROUND_LOCATION granted")
					requestLocations()
				}
				else -> {
					Log.d(TAG, "Location permission not granted (dam user :()")
				}
			}
		}

		locationPermissionRequest.launch(arrayOf(
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION))
	}

// regular location updates

	override fun onPause() {
		super.onPause()
		stopLocationUpdates()
	}

	private val locationCallback = object : LocationCallback() {
		override fun onLocationResult(locationResult: LocationResult) {
			for ((idx, location) in locationResult.locations.withIndex()) {
				Log.d(TAG, "Location ${idx+1}. is\n" + "lat : ${location.latitude}\n" +
					"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
			}
		}
	}

	private fun startLocationUpdates() {
		locationClient.requestLocationUpdates(createLocationRequest(), locationCallback , Looper.getMainLooper())
	}

	private fun stopLocationUpdates() {
		locationClient.removeLocationUpdates(locationCallback)
	}

	private fun createLocationRequest(): LocationRequest {
		return LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 10000)
			.setMinUpdateIntervalMillis(5000)
			.build()
	}

// request location for a single time
	private fun requestLastLocation() {
		locationClient.lastLocation
			.addOnSuccessListener { location : Location? ->
				if (location == null) {
					Log.d(TAG, "No last known location. Fetching the current location ...")
					requestCurrentLocation()
				} else {
					Log.d(TAG, "Current (last) location is \n" + "lat : ${location.latitude}\n" +
							"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
				}
			}
	}

	private fun requestCurrentLocation() {
		val locationResult = locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
		locationResult.addOnSuccessListener { location: Location? ->
			if (location != null) {
				Log.d(TAG, "Current location is \n" + "lat : ${location.latitude}\n" +
					"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
			} else
				Log.d(TAG, "No current/last known location.")
		}
	}

	private fun haveLocationPermissions(): Boolean = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
			|| ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

	companion object {
		val TAG = "Location"
	}

	private lateinit var locationClient:  FusedLocationProviderClient
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
	LocationTheme {
		Greeting("Android")
	}
}