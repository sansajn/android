package example.kotlin.jeromq

import android.os.Handler
import android.os.Message

class MessageListenerHandler(
	private val listener: MessageListener,
	private val payloadKey: String
) : Handler() {

	override fun handleMessage(msg: Message?) {
		listener.messageReceived(msg!!.data.getString(payloadKey))
	}
}