package example.kotlin.jeromq

import android.os.Handler
import org.zeromq.ZContext
import org.zeromq.ZMQ

class ZeroMQServer(private val uiThread: Handler) : Runnable {

	override fun run() {
		val ctx = ZContext(1)
		val rep = ctx.createSocket(ZMQ.REP)
		rep.bind("tcp://127.0.0.1:5555")

		while (!Thread.currentThread().isInterrupted) {
			val msg = rep.recv(0)
			uiThread.sendMessage(Util.bundledMessage(uiThread, String(msg)))
			rep.send(Util.reverse(String(msg)), 0)
		}

		rep.close()
		ctx.close()
	}
}