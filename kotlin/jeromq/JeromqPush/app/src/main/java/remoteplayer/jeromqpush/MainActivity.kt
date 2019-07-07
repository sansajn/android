package remoteplayer.jeromqpush

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

class MainActivity : AppCompatActivity(), ZMQPullTaskListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val ctx = ZContext(1)

		val pull = ctx.createSocket(ZMQ.PULL)
		pull.bind("tcp://127.0.0.1:55555")

		val pullTask = object : TimerTask() {
			override fun run() {
				runOnUiThread { ZMQPullTask(pull, this@MainActivity).execute() }
			}
		}

		_scheduler.schedule(pullTask, 0, 100)


		val push = ctx.createSocket(ZMQ.PUSH)
		push.connect("tcp://127.0.0.1:55555")

		val pushTask = object : TimerTask() {
			override fun run() {
				_counter += 1
				runOnUiThread { ZMQPushTask(push, listOf("hello from MainActivity #$_counter")).execute() }
			}

			private var _counter = 0L
		}

		_scheduler.schedule(pushTask, 0, 1000)
	}

	override fun received(message: String) {
		text.text = message
	}

	private val _scheduler = Timer()
}
