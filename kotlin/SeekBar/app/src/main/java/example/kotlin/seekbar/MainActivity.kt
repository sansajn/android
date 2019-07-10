package example.kotlin.seekbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
			override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
			}

			override fun onStartTrackingTouch(seekBar: SeekBar?) {
			}

			override fun onStopTrackingTouch(seekBar: SeekBar?) {
				Toast.makeText(this@MainActivity, "Progress is ${seekBar?.progress}%", Toast.LENGTH_SHORT).show()
			}
		})
	}
}
