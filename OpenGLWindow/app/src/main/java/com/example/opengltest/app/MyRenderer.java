package com.example.opengltest.app;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.Log;
import com.base.engine.core.CoreEngine;

import static android.opengl.GLES20.*;

public class MyRenderer implements GLSurfaceView.Renderer {
	@Override public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		_engine = new CoreEngine(60, new TestGame());
		_engine.createWindow("Game-engine");
	}

	@Override public void onDrawFrame(GL10 unused) {
		_engine.loopStep();
	}

	@Override public void onSurfaceChanged(GL10 unused, int width, int height) {
		glViewport(0, 0, width, height);
		Window.size(width, height);
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = glGetError()) != GL_NO_ERROR) {
			Log.e("MyRenderer", glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	private CoreEngine _engine;
}