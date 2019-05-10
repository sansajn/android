package org.example.minimalgl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceViewImpl extends GLSurfaceView {
	public GLSurfaceViewImpl(Context context) {
		super(context);
		setRenderer(new Renderer());
	}

	private static class Renderer implements GLSurfaceView.Renderer {
		@Override public void onDrawFrame(GL10 gl) {
			GLES20.glClearColor(0, 1, 1, 1);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
		}

		@Override public void onSurfaceChanged(GL10 gl, int width, int height) {
			Log.d(TAG, "onSurfaceChanged()");
		}

		@Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			Log.d(TAG, "onSurfaceCreated()");
		}

		private static String TAG = "Renderer";
	}
}
