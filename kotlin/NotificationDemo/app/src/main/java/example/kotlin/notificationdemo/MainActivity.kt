package example.kotlin.notificationdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		createNotificationChannel("example.kotlin.notifydemo.news",
			"NotifyDemo News",
			"Example News Channel")
	}

	fun sendNotification(view: View) {
		val notificationID = 101
		val resultIntent = Intent(this, ResultActivity::class.java)

		val pendingIntent = PendingIntent.getActivity(
			this,
			0,
			resultIntent,
			PendingIntent.FLAG_UPDATE_CURRENT)

		val channelID = "example.kotlin.notifydemo.news"

		val icon: Icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)

		val action: Notification.Action = Notification.Action.Builder(
			icon, "Open", pendingIntent).build()

		val notification = Notification.Builder(this@MainActivity, channelID)
			.setContentTitle("Example Notification")
			.setContentText("This is an example notification.")
			.setSmallIcon(android.R.drawable.ic_dialog_info)
			.setChannelId(channelID)
			.setContentIntent(pendingIntent)
			.setActions(action)
			.build()
		notificationManager?.notify(notificationID, notification)
	}

	private fun createNotificationChannel(id: String, name: String, description: String) {
		val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
		channel.description = description
		channel.enableLights(true)
		channel.lightColor = Color.RED
		channel.enableVibration(true)
		channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
		notificationManager?.createNotificationChannel(channel)
	}

	private var notificationManager: NotificationManager? = null
}
