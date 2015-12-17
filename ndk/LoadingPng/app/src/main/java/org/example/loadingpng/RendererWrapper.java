package org.example.loadingpng;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RendererWrapper implements Renderer {

	private final Context context;

	public RendererWrapper(Context context) {
		this.context = context;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		init(width, height);
	}

	public void onDrawFrame(GL10 gl) {
		render();
	}

	private native void init(int width, int height);
	private native void free();
	private native void render();

	static {
		System.loadLibrary("loadpng");
	}
};
