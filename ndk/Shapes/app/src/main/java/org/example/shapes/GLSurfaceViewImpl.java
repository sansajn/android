package org.example.shapes;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceViewImpl extends GLSurfaceView {

	public GLSurfaceViewImpl(Context context) {
		super(context);
		init();
	}

	private void init() {
		getHolder().setFormat(PixelFormat.RGBA_8888);  // by default RGB_565 surface, we want 32bit one
		setEGLContextFactory(new ContextFactory());
		setEGLConfigChooser(new ConfigChooser(8, 8, 8, 8, 8, 0));   // r, b, g, a, dept, stencil
		setRenderer(new Renderer());
	}

	private static class ContextFactory implements GLSurfaceView.EGLContextFactory {

		public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
			Log.w(TAG, "creating OpenGL ES 2.0 context");
			checkEglError("Before eglCreateContext", egl);
			int [] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
			EGLContext context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, attrib_list);
			checkEglError("After eglCreateContext", egl);
			return context;
		}

		public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
			egl.eglDestroyContext(display, context);
		}

		private static void checkEglError(String prompt, EGL10 egl) {
			int error;
			while ((error = egl.eglGetError()) != EGL10.EGL_SUCCESS)
				Log.e(TAG, String.format("%s: EGL error: 0x%x", prompt, error));
		}

		private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
	}  // ContextFactory

	private static class ConfigChooser implements GLSurfaceView.EGLConfigChooser {

		public ConfigChooser(int r, int g, int b, int a, int depth, int stencil) {
			_red_size = r;
			_green_size = g;
			_blue_size = b;
			_alpha_size = a;
			_depth_size = depth;
			_stencil_size = stencil;
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
			int[] num_config = new int[1];  // number of matching EGL configurations
			egl.eglChooseConfig(display, configAttribs2, null, 0, num_config);

			int numConfigs = num_config[0];

			if (numConfigs <= 0)
				throw new IllegalArgumentException("No configs match configSpec");

			EGLConfig[] configs = new EGLConfig[numConfigs];
			egl.eglChooseConfig(display, configAttribs2, configs, numConfigs, num_config);

			return chooseConfig(egl, display, configs);  // return the best one
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
			for (EGLConfig config : configs) {
				int d = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);
				int s = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0);
				if (d < _depth_size || s < _stencil_size)
					continue;

				int r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0);
				int g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
				int b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
				int a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);
				if (r == _red_size && g == _green_size && b == _blue_size && a == _alpha_size)
					return config;
			}
			return null;
		}

		private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute,	int defaultValue) {
			if (egl.eglGetConfigAttrib(display, config, attribute, _value))
				return _value[0];
			else
				return defaultValue;
		}

		private static int EGL_OPENGL_ES2_BIT = 4;

		private static int[] configAttribs2 = {
			EGL10.EGL_RED_SIZE, 4,
			EGL10.EGL_GREEN_SIZE, 4,
			EGL10.EGL_BLUE_SIZE, 4,
			EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
			EGL10.EGL_NONE
		};

		protected int _red_size;
		protected int _green_size;
		protected int _blue_size;
		protected int _alpha_size;
		protected int _depth_size;
		protected int _stencil_size;
		private int[] _value = new int[1];
	}  // ConfigChooser

	private static class Renderer implements GLSurfaceView.Renderer {

		@Override public void onDrawFrame(GL10 gl) {
			SceneWrapper.render();
		}

		@Override public void onSurfaceChanged(GL10 gl, int width, int height) {
			SceneWrapper.init(width, height);
		}

		@Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {}
	}

	private static String TAG = "GLSurfaceViewImpl";
}  // GLSurfaceViewImpl
