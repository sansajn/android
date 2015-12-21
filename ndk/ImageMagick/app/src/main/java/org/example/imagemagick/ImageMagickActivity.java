package org.example.imagemagick;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

public class ImageMagickActivity extends ActionBarActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		_view = new GLSurfaceViewImpl(getApplication());
		setContentView(_view);
	}

	@Override protected void onPause() {
		Log.v(TAG, "onPause()");
		super.onPause();
		_view.onPause();
	}

	@Override protected void onResume() {
		Log.v(TAG, "onResume()");
		super.onResume();
		_view.onResume();
	}

	GLSurfaceViewImpl _view;
	String TAG = "ImageMagickActivity";
}
