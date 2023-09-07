package com.example.locationcoro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.example.locationcoro.ui.theme.LocationCoroTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
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

		if (haveLocationPermissions())
			requestLocations()
		else
			askLocationPermissions()
	}

	override fun onPause() {
		super.onPause()
		stopLocationUpdates()
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

	private val _locationCallback = object : LocationCallback() {
		override fun onLocationResult(locationResult: LocationResult) {
			onLocationUpdate(locationResult)
		}
	}

	// We expect that permissions are already granted.
	private fun requestLocations() {
		// TODO: Can we access _locationClient from multiple threads? Because now we do it!!!

		assert(haveLocationPermissions()) {"we expect location permissions granted"}

		_locationClient = LocationServices.getFusedLocationProviderClient(this)

		// ask for location
		lifecycleScope.launch(Dispatchers.IO) {
			requestLastLocation(_locationClient)
			// We can not update UI from there ...
		}

		// ask for periodic location updates
		startLocationUpdates()
	}

	// Ask for periodic location updates.
	private fun startLocationUpdates() {
		_locationClient.requestLocationUpdates(createLocationRequest(), _locationCallback , Looper.getMainLooper())
	}

	// Quit periodic location updates.
	private fun stopLocationUpdates() {
		_locationClient.removeLocationUpdates(_locationCallback)
	}

	private fun onLocationUpdate(locationResult: LocationResult) {
		for ((idx, location) in locationResult.locations.withIndex()) {
			Log.d(TAG, "Location is \n" + "lat : ${location.latitude}\n" +
				"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
		}
	}

	private fun createLocationRequest(): LocationRequest {
		return LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 10000)
			.setMinUpdateIntervalMillis(5000)
			.build()
	}

	private suspend fun requestLastLocation(locationClient: FusedLocationProviderClient) {
		val location = locationClient.lastLocation.await()
		if (location != null) {
			Log.d(TAG, "Current (last) location is \n" + "lat : ${location.latitude}\n" +
				"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
		} else {
			Log.d(TAG, "No last known location. Fetching the current location ...")
			requestCurrentLocation(locationClient)
		}
	}
	private suspend fun requestCurrentLocation(locationClient: FusedLocationProviderClient) {
		val location = locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
			.await()
		if (location != null) {
			Log.d(TAG, "Current location is \n" + "lat : ${location.latitude}\n" +
				"long : ${location.longitude}\n" + "fetched at ${System.currentTimeMillis()}")
		} else
			Log.d(TAG, "No current/last known location.")
	}

	private fun haveLocationPermissions(): Boolean = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
		|| ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

	companion object {
		const val TAG = "LocationCoro"
	}

	private lateinit var _locationClient:  FusedLocationProviderClient
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