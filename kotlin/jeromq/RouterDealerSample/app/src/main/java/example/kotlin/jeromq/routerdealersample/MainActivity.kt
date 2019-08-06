package example.kotlin.jeromq.routerdealersample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.zeromq.ZMQ
import org.zeromq.ZContext
import java.util.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		_router.bind("tcp://127.0.0.1:55555")
		_dealer.connect("tcp://127.0.0.1:55555")

		val routerTask = object : TimerTask() {
			override fun run() {
				runOnUiThread { ZMQRouterTask(this@MainActivity::answerQuestion).execute(_router) }
			}
		}

		_scheduler.schedule(routerTask, 10, 100)

		val question = "Hello welcome!"
		val dealerAskTask = object : TimerTask() {
			override fun run() {
				runOnUiThread { ZMQDealerAskTask().execute(_dealer, question) }
			}
		}

		val dealerRecvTask = object : TimerTask() {
			override fun run() {
				runOnUiThread { ZMQDealerRecvTask(this@MainActivity::receiveAnswer).execute(_dealer) }
			}
		}

		_scheduler.schedule(dealerAskTask, 0, 1000)
		_scheduler.schedule(dealerRecvTask, 10, 100)
	}

	private fun answerQuestion(question: String): String {
		return question.reversed()
	}

	private fun receiveAnswer(answer: String) {
		text_view.text = answer
	}

	private val _ctx = ZContext()
	private val _router = _ctx.createSocket(ZMQ.ROUTER)
	private val _dealer = _ctx.createSocket(ZMQ.DEALER)
	private val _scheduler = Timer()
}
