package org.example.usegl;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends ActionBarActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_view = new GLSurfaceViewImpl(getApplication());
		setContentView(_view);
		Log.d(TAG, "onCreate()");
	}

	@Override protected void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
		_view.onResume();
	}

	@Override protected void onPause() {
		Log.d(TAG, "onPause()");
		super.onPause();
		_view.onPause();
	}

	private GLSurfaceViewImpl _view;
	private static String TAG = "MainActivity";
}
