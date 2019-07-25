package example.kotlin.emptylistplaceholder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		populateList()

		list_view.emptyView = empty

		list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			Toast.makeText(this, "clicked on $position item", Toast.LENGTH_LONG).show()
		}

		clear_button.setOnClickListener { clearList() }
		populate_button.setOnClickListener { populateList() }
	}

	private fun populateList() {
		var items = mutableListOf("one", "two", "three")
		list_view.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
	}

	private fun clearList() {
		val adapter = list_view.adapter as ArrayAdapter<*>
		adapter.clear()
	}
}
