package example.kotlin.recyclerviewupdate

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		list.layoutManager = LinearLayoutManager(this)
		list.hasFixedSize()
		list.adapter = CustomListAdapter(this, _listOfLanguages.subList(0, _upperLanguageBoundaryIdx))

		update_button.setOnClickListener {
			_upperLanguageBoundaryIdx = min(_upperLanguageBoundaryIdx+1, _listOfLanguages.size)
			list.adapter = CustomListAdapter(this, _listOfLanguages.subList(0, _upperLanguageBoundaryIdx))
			Toast.makeText(this@MainActivity, "update button clicked", Toast.LENGTH_LONG).show()
		}
	}

	private val _list = mutableListOf<String>()

	private val _listOfLanguages = listOf("Kotlin", "C++", "Python", "Lua", "C", "Pascal", "Go", "Lisp",
		"Fortran", "Ruby", "JavaScript", "F#", "C#")

	private var _upperLanguageBoundaryIdx = 5
}

class CustomListAdapter(var c: Context, var list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		var v = LayoutInflater.from(c).inflate(R.layout.list_item, parent, false)
		return Item(v)
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		(holder as Item).bindData(list[position])
	}

	class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bindData(item: String) {
			itemView.title.text = item
			itemView.setOnClickListener { Toast.makeText(itemView.context, "'$item' item clicked", Toast.LENGTH_LONG).show() }
		}
	}
}
