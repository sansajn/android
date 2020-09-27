package example.kotlin.notificationprogress

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		createNotificationChannel(NOTIFICATION_CHANNEL_ID,
			"NotifyDemo News",
			"Example News Channel")
	}

	fun sendNotification(view: View) {
		val notificationID = 101
		val secondNotificationID = 102

		var notification = createNotification("Example Notification")
		var secondNotification = createNotification("Second Example Notification")

		notificationManager?.notify(notificationID, notification.build())
		notificationManager?.notify(secondNotificationID, secondNotification.build())

		val handler = Handler()
		var progress = 0

		Thread(Runnable {
			Log.d("tag", "thread running ...")
			while (progress < 100) {
				progress += 4
				try {
					Thread.sleep(1000)
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}

				handler.post(Runnable {
					updateNotificationProgress(notificationID, notification, progress)
					updateNotificationProgress(secondNotificationID, secondNotification, progress)
				})
			}
		}).start()
	}

	private fun updateNotificationProgress(notificationID: Int, notification: Notification.Builder, progress: Int) {
		if (progress < 100) {
			notification.setContentText("$progress% complete")
			notification.setProgress(100, progress, false)
		} else {
			notification.setContentText("Download complete.")
			notification.setProgress(0, 0, false)
		}
		notificationManager?.notify(notificationID, notification.build())
	}

	private fun createNotification(title: String): Notification.Builder {
		return Notification.Builder(this@MainActivity, NOTIFICATION_CHANNEL_ID)
			.setContentTitle(title)
			.setSmallIcon(android.R.drawable.ic_dialog_info)
			.setChannelId(NOTIFICATION_CHANNEL_ID)
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
	private val NOTIFICATION_CHANNEL_ID = "example.kotlin.notifydemo.news"
}
