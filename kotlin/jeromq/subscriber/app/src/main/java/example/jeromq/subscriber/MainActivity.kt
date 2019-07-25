package example.jeromq.subscriber

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*


class ZMQSubscriberTask(private val delegate: (String) -> Unit) : AsyncTask<Any, Void, MutableList<String>>() {

	override fun doInBackground(vararg params: Any): MutableList<String> {
		val sub = params[0] as ZMQ.Socket
		val commands = mutableListOf<String>()

		while (true) {
			val d = sub.recvStr(ZMQ.NOBLOCK)
			if (d != null)
				commands.add(d)
			else
				break
		}

		return commands
	}

	override fun onPostExecute(commands: MutableList<String>) {
		super.onPostExecute(commands)

		if (commands.isNotEmpty()) {
			delegate(commands[0])
		}
	}
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
				runOnUiThread { ZMQSubscriberTask(this@MainActivity::setText).execute(_ctx) }
			}
		}

		_scheduler.schedule(timerTask, 0, 100)
	}

	fun setText(s: String) {
		text.text = s
	}

	val _ctx = ZContext()
	val _sub = _ctx.createSocket(ZMQ.SUB)
	val _scheduler = Timer()
}
