package example.kotlin.jeromq

interface MessageListener {
	fun messageReceived(msg: String)
}
