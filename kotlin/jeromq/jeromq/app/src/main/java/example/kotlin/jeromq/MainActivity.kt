package example.kotlin.jeromq

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		Thread(ZeroMQServer(_serverMessageHandler)).start()

		button.setOnClickListener {
			ZeroMQMessageTask(_clientMessageHandler).execute(editText.text.toString())
		}
	}

	private fun getTimeString(): String {
		return DATE_FORMAT.format(Date())
	}

	private val _clientMessageHandler = MessageListenerHandler(
		object : MessageListener {
			override fun messageReceived(msg: String) {
				viewText.append("${getTimeString()} - client received: $msg\n")
			}
		}, Util.MESSAGE_PAYLOAD_KEY)

	private val _serverMessageHandler = MessageListenerHandler(
		object : MessageListener {
			override fun messageReceived(msg: String) {
				viewText.append("${getTimeString()} - server received: $msg\n")
			}
		}, Util.MESSAGE_PAYLOAD_KEY
	)

	private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ")
}
