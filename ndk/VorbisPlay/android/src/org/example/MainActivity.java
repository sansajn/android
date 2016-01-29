package org.example;

import android.app.Activity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		play_sound();
		_sound_update = new Timer("sound_update_timer");
		_sound_update.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() {
				update();
			}
		}, 0, 50);  // update every 50ms
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		free();
	}

	private static native void play_sound();
	private static native void update();
	private static native void free();

	private Timer _sound_update;

	static {
		System.loadLibrary("sound");
	}
}
