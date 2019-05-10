package com.example.imageview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val imgResId = R.drawable.ic_launcher_background
		imageView.setImageResource(imgResId)

		var resId = imgResId
		button.setOnClickListener {
			resId = if (resId == R.drawable.ic_launcher_background) R.mipmap.ic_launcher else R.drawable.ic_launcher_background
			imageView.setImageResource(resId)
		}
	}
}
