package com.example.marker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

val praguePos = Point.fromLngLat(14.434564115508454, 50.08353884895491)

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mapView = findViewById(R.id.mapView)
		mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS,
			object : Style.OnStyleLoaded {
				override fun onStyleLoaded(style: Style) {
					Log.d("marker", "map loaded")
					addAnotationToMap()
				}
			})
	}

	private fun addAnotationToMap() {
		// Create an instance of the Annotation API and get the PointAnnotationManager.
		bitmapFromDrawableRes(this@MainActivity, R.drawable.red_marker)?.let {
			val annotationApi = mapView?.annotations
			val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)

			// Set options for the resulting symbol layer.
			val pointAnnotationOptions: PointAnnotationOptions =
				PointAnnotationOptions()
					.withPoint(praguePos)  // Define a geographic coordinate.
					.withIconImage(it)  // Specify the bitmap you assigned to the point annotation

			// Add the resulting pointAnnotation to the map.
			pointAnnotationManager?.create(pointAnnotationOptions)
		}
	}

	private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) = convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

	private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
		if (sourceDrawable == null)
			return null

		return if (sourceDrawable is BitmapDrawable) {
			sourceDrawable.bitmap
		} else {
			// copying drawable object to not manipulate on the same reference
			val constantState = sourceDrawable.constantState ?: return null
			val drawable = constantState.newDrawable().mutate()
			val bitmap: Bitmap = Bitmap.createBitmap(
				drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
			val canvas = Canvas(bitmap)
			drawable.setBounds(0, 0, canvas.width, canvas.height)
			drawable.draw(canvas)
			bitmap
		}
	}

	override fun onStart() {
		super.onStart()
		mapView?.onStart()
	}

	override fun onStop() {
		super.onStop()
		mapView?.onStop()
	}

	override fun onLowMemory() {
		super.onLowMemory()
		mapView?.onLowMemory()
	}

	override fun onDestroy() {
		super.onDestroy()
		mapView?.onDestroy()
	}

	private var mapView: MapView? = null
}
