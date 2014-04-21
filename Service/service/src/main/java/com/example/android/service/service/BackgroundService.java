package com.example.android.service.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@ Implementacia servisu.
public class BackgroundService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate()");
		_notificationMan = (NotificationManager)getSystemService(
			NOTIFICATION_SERVICE);
		displayNotificationMessage("Background service is running");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		int counter = intent.getExtras().getInt("counter");
		Log.v(TAG,
			"onStartCommad(), counter=" + counter + ", startId=" + startId);

		_pool.submit(new ServiceWorker(counter));

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.v(TAG,
			"onDestroy() Interrupting threads and cancelling notifications");
		_pool.shutdownNow();
		_notificationMan.cancelAll();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, " onBind()");
		return null;
	}

	private void displayNotificationMessage(String message) {
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
			new Intent(this, MainActivity.class), 0);

		Notification notification = new Notification.Builder(this)
			.setContentTitle(TAG)
			.setContentText(message)
			.setWhen(System.currentTimeMillis())
			.setContentIntent(contentIntent)
			.setSmallIcon(R.drawable.emo_im_winking)
			.build();

		_notificationMan.notify(0, notification);
	}

	class ServiceWorker implements Runnable {

		public ServiceWorker(int counter) {
			_counter = counter;
		}

		@Override
		public void run() {
			final String TAG2 =
				"ServiceWorker:" + Thread.currentThread().getId();
			try {
				Log.v(TAG2,
					"sleeping for 10 seconds, counter=" + _counter);
				Thread.sleep(10000);
				Log.v(TAG2, "... waking up, counter=" + _counter);
			} catch (InterruptedException e) {
				Log.v(TAG2, "... sleep interrupted, counter=" + _counter);
			}
		}

		private int _counter = -1;
	}

	private static final String TAG = "BackgroundService";
	private NotificationManager _notificationMan;
	private ExecutorService _pool = Executors.newSingleThreadExecutor();
}
