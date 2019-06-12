package example.kotlin.jeromq

import android.os.AsyncTask
import android.os.Handler
import org.zeromq.ZMQ
import org.zeromq.ZContext

class ZeroMQMessageTask(private val uiThread: Handler) : AsyncTask<String, Void, String>() {

	override fun doInBackground(vararg params: String?): String {
		val ctx = ZContext(1)
		val req = ctx.createSocket(ZMQ.REQ)
		req.connect("tcp://127.0.0.1:5555")

		req.send(params[0]!!.toByteArray(), 0)
		val result = String(req.recv(0))

		req.close()
		ctx.close()

		return result
	}

	override fun onPostExecute(result: String?) {
		super.onPostExecute(result)
		if (result != null)
			uiThread.sendMessage(Util.bundledMessage(uiThread, result))
	}
}