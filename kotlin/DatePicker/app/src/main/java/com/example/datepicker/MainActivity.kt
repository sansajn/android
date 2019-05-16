package com.example.datepicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val today = Calendar.getInstance()
		datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) {
			view, year, monthOfYear, dayOfMonth ->
				val month = monthOfYear + 1
				val msg = "Selected Date is $dayOfMonth/$month/$year"
				Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()

				textView.text = msg
		}

	}
}
