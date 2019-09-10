package example.kotlin.recyclerviewdraganddrop

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_layout.view.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		_itemTouchHelper.attachToRecyclerView(list)

		list.layoutManager = LinearLayoutManager(this)
		list.hasFixedSize()
		list.adapter = CustomListAdapter(this, mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
	}

	// in a case of recycler item handle
	fun startDragging(viewHolder: RecyclerView.ViewHolder) {
		_itemTouchHelper.startDrag(viewHolder)
	}

	private val _itemTouchHelper by lazy {  // lazy is not good in case of activity

		val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

			override fun onMove(recycler: RecyclerView, holder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
				val adapter = recycler.adapter as CustomListAdapter
				val from = holder.adapterPosition
				val to = target.adapterPosition
				adapter.moveItem(from, to)
				adapter.notifyItemMoved(from, to)
				return true
			}

			override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
				// do nothing ...
			}

			override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
				super.onSelectedChanged(viewHolder, actionState)
				if (actionState == ACTION_STATE_DRAG)
					viewHolder?.itemView?.alpha = 0.5f
			}

			override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
				super.clearView(recyclerView, viewHolder)
				viewHolder?.itemView?.alpha = 1.0f
			}
		}


		ItemTouchHelper(simpleItemTouchCallback)
	}
}

class CustomListAdapter(val activity: MainActivity, var list: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		var v = LayoutInflater.from(activity).inflate(R.layout.list_layout, parent, false)
		val item = Item(v)

		// in a case of item handle dragging
		item.itemView.handleView.setOnTouchListener { v, event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN)
				activity.startDragging(item)
			return@setOnTouchListener true
		}

		return item
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		(holder as Item).bindData(list[position])
	}

	fun moveItem(from: Int, to: Int) {
		val fromItem = list[from]
		list.removeAt(from)
		if (to < from)
			list.add(to, fromItem)
		else
			list.add(to-1, fromItem)
	}

	class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bindData(item: String) {
			itemView.textView.text = item
			itemView.setOnClickListener { Toast.makeText(itemView.context, "item $item clicked", Toast.LENGTH_LONG).show() }
		}
	}
}
