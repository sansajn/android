package example.kotlin.viewmodel

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
		viewModel.getUsername().observe(this, Observer { text ->
			text_view.text = text
			Toast.makeText(this, "$text", Toast.LENGTH_LONG).show()
		})

		viewModel.initNetworkRequest()  // call expensive operation
	}
}
