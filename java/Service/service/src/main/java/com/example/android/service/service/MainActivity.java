package com.example.android.service.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onStartClick(View view) {
		startService();
	}

	public void onStopClick(View view) {
		stopService();
	}

	private void startService() {
		Log.v(TAG, "Starting service... counter=" + counter);
		Intent intent = new Intent(MainActivity.this, BackgroundService.class);
		intent.putExtra("counter", counter++);
		startService(intent);
	}

	private void stopService() {
		Log.v(TAG, "Stoping service...");
		Intent intent = new Intent(MainActivity.this, BackgroundService.class);
		if (stopService(intent))
			Log.v(TAG, "stopService was successful");
		else
			Log.v(TAG, "stopService was unsuccessful");
	}

	@Override
	public void onDestroy() {
		stopService();
		super.onDestroy();
	}

	private static final String TAG = "MainActivity";
	private int counter = -1;
}
