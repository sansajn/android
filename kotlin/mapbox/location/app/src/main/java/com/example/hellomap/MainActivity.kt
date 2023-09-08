package com.example.hellomap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.hellomap.databinding.ActivityMainBinding
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		_mapView = binding.mapView
		//_mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

		_locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
		_locationPermissionHelper.checkPermissions {
			onMapReady()
		}
	}

	private fun onMapReady() {
		_mapView.getMapboxMap().setCamera(
			CameraOptions.Builder()
				.zoom(14.0)
				.build()
		)
		_mapView.getMapboxMap().loadStyleUri(
			Style.MAPBOX_STREETS
		) {
			initLocationComponent()
			setupGesturesListener()
		}
	}

	private val onMoveListener = object : OnMoveListener {
		override fun onMoveBegin(detector: MoveGestureDetector) {
			onCameraTrackingDismissed()
		}

		override fun onMove(detector: MoveGestureDetector): Boolean {
			return false
		}

		override fun onMoveEnd(detector: MoveGestureDetector) {}
	}

	private fun setupGesturesListener() {
		_mapView.gestures.addOnMoveListener(onMoveListener)
	}

	private fun initLocationComponent() {
		val locationComponentPlugin = _mapView.location
		locationComponentPlugin.updateSettings {
			this.enabled = true
			this.locationPuck = LocationPuck2D(
				bearingImage = AppCompatResources.getDrawable(
					this@MainActivity,
					R.drawable.mapbox_user_puck_icon,
				),
				shadowImage = AppCompatResources.getDrawable(
					this@MainActivity,
					R.drawable.mapbox_user_icon_shadow,
				),
				scaleExpression = interpolate {
					linear()
					zoom()
					stop {
						literal(0.0)
						literal(0.6)
					}
					stop {
						literal(20.0)
						literal(1.0)
					}
				}.toJson()
			)
		}
		locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
		locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
	}

	// TODO: is this callback?
	private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
		_mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
	}

	// TODO: is this callback?
	private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
		_mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
		_mapView.gestures.focalPoint = _mapView.getMapboxMap().pixelForCoordinate(it)
	}

	private fun onCameraTrackingDismissed() {
		Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
		_mapView.location
			.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
		_mapView.location
			.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
		_mapView.gestures.removeOnMoveListener(onMoveListener)
	}

	override fun onStart() {
		super.onStart()
		_mapView.onStart()
	}

	override fun onStop() {
		super.onStop()
		_mapView.onStop()
	}

	override fun onLowMemory() {
		super.onLowMemory()
		_mapView.onLowMemory()
	}

	override fun onDestroy() {
		super.onDestroy()
		_mapView.location
			.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
		_mapView.location
			.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
		_mapView.gestures.removeOnMoveListener(onMoveListener)
		_mapView.onDestroy()
	}

	private lateinit var _mapView: MapView
	private lateinit var _locationPermissionHelper: LocationPermissionHelper
	private lateinit var binding: ActivityMainBinding
}
