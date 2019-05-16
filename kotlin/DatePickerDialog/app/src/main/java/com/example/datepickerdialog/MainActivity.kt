package com.example.datepickerdialog

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val today = Calendar.getInstance()
		val d = DatePickerDialog(this,
			DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
				Toast.makeText(this, "$dayOfMonth/$month/$year", Toast.LENGTH_SHORT).show()
			}, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
		d.show()
	}
}
