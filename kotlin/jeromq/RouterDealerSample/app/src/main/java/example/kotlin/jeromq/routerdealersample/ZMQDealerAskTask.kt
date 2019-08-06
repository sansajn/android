package example.kotlin.jeromq.routerdealersample

import android.os.AsyncTask
import org.zeromq.ZMQ

// router <- dealer connection dealer ask part task implementation
class ZMQDealerAskTask() : AsyncTask<Any, Void, Unit>() {

	override fun doInBackground(vararg params: Any?) {
		val dealer = params[0] as ZMQ.Socket
		val question = params[1] as String
		dealer.send(question)
	}
}
