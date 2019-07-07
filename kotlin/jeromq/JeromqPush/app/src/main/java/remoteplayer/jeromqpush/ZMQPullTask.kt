package remoteplayer.jeromqpush

import android.os.AsyncTask
import org.zeromq.ZMQ

interface ZMQPullTaskListener {
	fun received(message: String)
}

class ZMQPullTask(private val _pull: ZMQ.Socket, private val _delegate: ZMQPullTaskListener) : AsyncTask<Void, Void, Void?>() {

	override fun doInBackground(vararg params: Void?): Void? {
		val msg = _pull.recvStr(ZMQ.NOBLOCK)
		if (msg != null)
			_messages.add(msg)
		return null
	}

	override fun onPostExecute(result: Void?) {
		super.onPostExecute(result)

		for (msg in _messages)
			_delegate.received(msg)
	}

	private val _messages = mutableListOf<String>()
}