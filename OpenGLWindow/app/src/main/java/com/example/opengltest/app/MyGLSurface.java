package com.example.opengltest.app;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class MyGLSurface extends GLSurfaceView {
	public MyGLSurface(Context context) {
		super(context);
		setEGLContextClientVersion(2);  // opengl es 2 context
		_renderer = new MyRenderer();
		setRenderer(_renderer);
	}

	private MyRenderer _renderer;
}
