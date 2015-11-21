package org.example.loadingpng;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class LoadingPng extends ActionBarActivity {

	private GLSurfaceView glSurfaceView;
	private boolean rendererSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

		boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000 || isProbablyEmulator();

		if (!supportsEs2) {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
			return;
		}

		glSurfaceView = new GLSurfaceView(this);
		if (isProbablyEmulator())
			glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);  // avoid crash with some emulator

		final RendererWrapper rendererWrapper = new RendererWrapper(this);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(rendererWrapper);
		rendererSet = true;
		setContentView(glSurfaceView);

		glSurfaceView.setOnTouchListener(
			new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event != null) {
						final float normalizedX = (event.getX() / (float)v.getWidth()) * 2 - 1;
						final float normalizedY = -((event.getY() / (float)v.getHeight()) * 2 - 1);
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							glSurfaceView.queueEvent(
								new Runnable() {
									public void run() {
										rendererWrapper.handleTouchPress(normalizedX, normalizedY);
									}
								});
						}  // down
						else if (event.getAction() == MotionEvent.ACTION_MOVE) {
							glSurfaceView.queueEvent(
								new Runnable() {
									public void run() {
										rendererWrapper.handleTouchDrag(normalizedX, normalizedY);
									}
								});
						}  // move

						return true;
					}
					else
						return false;
				}  // onTouch
			});

	}  // onCreate

	private boolean isProbablyEmulator() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
			&& (Build.FINGERPRINT.startsWith("generic")
				|| Build.FINGERPRINT.startsWith("unknown")
				|| Build.MODEL.contains("google_sdk")
				|| Build.MODEL.contains("Emulator")
				|| Build.MODEL.contains("Android SDK build for x86"));
	}

	protected void onPause() {
		super.onPause();
		if (rendererSet)
			glSurfaceView.onPause();
	}

	protected void onResume() {
		super.onResume();
		if (rendererSet)
			glSurfaceView.onResume();
	}
}
