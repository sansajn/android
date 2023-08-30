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

		// this will only run in case of permission granted TODO: needs to be refactored (not working for a first time when we do not have permissions yet)
		val locationClient = LocationServices.getFusedLocationProviderClient(this)
		requestLastLocation(locationClient)

		startLocationUpdates(locationClient)  // regular location updates
	}

// regular location updates

	private fun startLocationUpdates(locationClient: FusedLocationProviderClient) {
		val locationCallback = object : LocationCallback() {
			override fun onLocationResult(locationResult: LocationResult) {
				for (location in locationResult.locations) {
					// TODO: add location counter
					Log.d(TAG, "Location is \n" + "lat : ${location.latitude}\n" +
							"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
				}
			}
		}

		locationClient.requestLocationUpdates(createLocationRequest(), locationCallback , Looper.getMainLooper())
	}

	// TODO: stopLocationUpdates()

	private fun createLocationRequest(): LocationRequest {
		return LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 10000)
			.setMinUpdateIntervalMillis(5000)
			.build()
	}

// request location for a single time
	private fun requestLastLocation(locationClient: FusedLocationProviderClient) {
		locationClient.lastLocation
			.addOnSuccessListener { location : Location? ->
				if (location == null) {
					Log.d(TAG, "No last known location. Fetching the current location ...")
					requestCurrentLocation(locationClient)
				} else {
					Log.d(TAG, "Current (last) location is \n" + "lat : ${location.latitude}\n" +
							"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
				}
			}
	}

	private fun requestCurrentLocation(locationClient: FusedLocationProviderClient) {
		val locationResult = locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
		locationResult.addOnSuccessListener { location: Location? ->
			if (location != null) {
				Log.d(TAG, "Current location is \n" + "lat : ${location.latitude}\n" +
					"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
			} else
				Log.d(TAG, "No current/last known location.")
		}
	}

	companion object {
		val TAG = "Location"
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
	LocationTheme {
		Greeting("Android")
	}
}