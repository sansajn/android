package example.kotlin.jeromq

import android.os.Bundle
import android.os.Handler
import android.os.Message
import java.lang.StringBuilder

class Util {

	companion object {

		fun bundledMessage(uiThread: Handler, msg: String): Message {
			val result = uiThread.obtainMessage()
			val b = Bundle()
			b.putString(MESSAGE_PAYLOAD_KEY, msg)
			result.data = b
			return result
		}

		fun reverse(str: String): String {
			val sb = StringBuilder(str)
			return sb.reverse().toString()
		}

		val MESSAGE_PAYLOAD_KEY = "jeromq-service-payload"
	}
}