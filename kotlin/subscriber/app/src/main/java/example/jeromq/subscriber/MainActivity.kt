package example.jeromq.subscriber

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*


class ZMQSubscriberTask : AsyncTask<Void, Void, Void?> {

	constructor(sub: ZMQ.Socket, out: TextView) : super() {
		_sub = sub
		_out = out
	}

	override fun doInBackground(vararg params: Void?): Void? {
		val d = _sub.recvStr(ZMQ.NOBLOCK)
		if (d != null)
			_commands.add(d)
		return null
	}

	override fun onPostExecute(result: Void?) {
		super.onPostExecute(result)

		if (_commands.isNotEmpty()) {
			_out.text = _commands[0]
			_commands.clear()
		}
	}

	private val _sub: ZMQ.Socket
	private val _out: TextView
	private var _commands = mutableListOf<String>()
}

class ZMQPublisherServer : Runnable {

	override fun run() {
		val ctx = ZContext(1)
		val pub = ctx.createSocket(ZMQ.PUB)
		pub.bind("tcp://127.0.0.1:5555")

		while (!Thread.currentThread().isInterrupted) {
			pub.send("greetings from publisher")
			Thread.sleep(1000)
		}

		pub.close()
		ctx.close()
	}
}

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		Thread(ZMQPublisherServer()).start()

		_sub.subscribe(ZMQ.SUBSCRIPTION_ALL)
		_sub.connect("tcp://127.0.0.1:5555")

		val timerTask = object : TimerTask() {
			override fun run() {
				runOnUiThread { ZMQSubscriberTask(_sub, text).execute(null) }
			}
		}

		timer = Timer()
		timer.schedule(timerTask, 0, 100)
	}

	val _ctx = ZContext()
	val _sub = _ctx.createSocket(ZMQ.SUB)
	lateinit var timer: Timer
}
