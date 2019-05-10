package com.example.gridview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		gridview.adapter = ImageListAdapter(this, R.layout.gridview_item, items)

		gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
			Toast.makeText(this@MainActivity, " Clicked Position: " + (position + 1), Toast.LENGTH_SHORT).show()
		}
	}

	private val items: Array<String>
		get() = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
			"eleven", "twelve", "thirteen", "fourteen", "fiveteen", "sixteen")
}
