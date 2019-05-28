package com.example.listview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		var items = listOf("one", "two", "three")
		listview.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
		listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			Toast.makeText(this, "clicked on $position item", Toast.LENGTH_LONG).show()
		}
	}
}
