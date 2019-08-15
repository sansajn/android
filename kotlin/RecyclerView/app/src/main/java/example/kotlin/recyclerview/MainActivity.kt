package example.kotlin.recyclerview

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_layout.view.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		list.layoutManager = LinearLayoutManager(this)
		list.hasFixedSize()
		list.adapter = CustomListAdapter(this, getList())
	}

	private fun getList(): List<String> {
		return listOf("Android", "Kotlin", "C++", "Python", "Lua")
	}
}

class CustomListAdapter(var c: Context, var list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		var v = LayoutInflater.from(c).inflate(R.layout.list_layout, parent, false)
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
			itemView.textView.text = item
		}
	}
}
