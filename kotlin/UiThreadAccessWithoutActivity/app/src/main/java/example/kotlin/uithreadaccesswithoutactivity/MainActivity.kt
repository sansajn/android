package example.kotlin.uithreadaccesswithoutactivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val t = object : TimerTask() {
			override fun run() {
				Handler(Looper.getMainLooper()).post {
					_conunter += 1
					text_view.text = _conunter.toString()
				}
			}
		}

		_scheduler.schedule(t, 0, 1000)
	}

	private var _conunter = 1
	private val _scheduler = Timer()
}
