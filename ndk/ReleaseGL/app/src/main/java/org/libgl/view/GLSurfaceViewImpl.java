package org.libgl.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

import org.libgl.wrapper.NativeScene;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceViewImpl extends GLSurfaceView {

	public GLSurfaceViewImpl(Context context) {
		super(context);
		Log.d(TAG, "GLSurfaceViewImpl(), width:" + getWidth() + ", height:" + getHeight());

		getHolder().setFormat(PixelFormat.RGBA_8888);
		setEGLContextClientVersion(2);  // force gles2
//		setPreserveEGLContextOnPause(true);

		_renderer = new Renderer();
		setRenderer(_renderer);
	}

	@Override public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		Log.d(TAG, "surfaceCreated(), width:" + getWidth() + ", height: " + getHeight());
		Rect surfaceFrame = holder.getSurfaceFrame();
		_renderer.surfaceGeometry(surfaceFrame.width(), surfaceFrame.height());
	}

	@Override public void surfaceDestroyed(SurfaceHolder holder) {
		super.surfaceDestroyed(holder);
		Log.d(TAG, "surfaceDestroyed()");

		queueEvent(
			new Runnable() {
				public void run() {
					Log.d("Runnable()", "hello from rendering thread!");
					NativeScene.destroy();
				}
			});
	}

	private class Renderer implements GLSurfaceView.Renderer {

		@Override public void onDrawFrame(GL10 gl) {
			NativeScene.display();
		}

		@Override public void onSurfaceChanged(GL10 gl, int width, int height) {
			Log.d(TAG, "onSurfaceChanged()");
			NativeScene.reshape(width, height);
		}

		@Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			Log.d(TAG, "onSurfaceCreated(), w:" + _width + ", h:" + _height);
			NativeScene.create(_width, _height);
		}

		public void surfaceGeometry(int widht, int height) {
			_width = widht;
			_height = height;
		}

		private int _width = 0, _height = 0;
		private String TAG = "GLSurfaceViewImpl.Renderer";
	}

	Renderer _renderer;
	private static String TAG = "GLSurfaceViewImpl";
}


