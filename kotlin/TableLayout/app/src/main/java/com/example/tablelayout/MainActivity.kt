package com.example.tablelayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		textView.text = "ROWS: $ROWS COLUMNS: $COLUMNS"

		val params = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
		tableLayout.apply {
			layoutParams = params
			isShrinkAllColumns = true
		}

		createTable(ROWS, COLUMNS)
	}

	fun createTable(rows: Int, cols: Int) {
		for (i in 0 until rows) {
			val row = TableRow(this)
			row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT)

			for (j in 0 until cols) {
				val button = Button(this)
				button.apply {
					layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
					text = "R $i C $j"
				}
				row.addView(button)
			}

			tableLayout.addView(row)
		}

		linearLayout.addView(tableLayout)
	}

	val ROWS = 10
	val COLUMNS = 5
	val tableLayout by lazy { TableLayout(this) }
}
