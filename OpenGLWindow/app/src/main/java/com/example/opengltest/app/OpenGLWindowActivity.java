package com.example.opengltest.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class OpenGLWindowActivity extends Activity {
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Android.mainActivity = this;

		_glview = new MyGLSurface(this);
		setContentView(_glview);
	}

	@Override protected void onResume() {
		super.onResume();
		_glview.onResume();
	}

	@Override protected void onPause() {
		super.onPause();
		_glview.onPause();
	}

	private MyGLSurface _glview;
}
