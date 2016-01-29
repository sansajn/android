package org.example.openal;

import android.app.Activity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		play_sound();
		_update_timer = new Timer("sound_update_timer");
		_update_timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				update();
			}
		}, 0, 50);  // update every 50ms
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		free();
	}

	private native void play_sound();
	private native void update();
	private native void free();

	private Timer _update_timer;

	static {
		System.loadLibrary("sound");  // loads libsound
	}
}
