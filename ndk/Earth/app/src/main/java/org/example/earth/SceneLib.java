package org.example.earth;

import android.view.MotionEvent;

// wrapper pre nativnu kniznicu scene TODO: premenuj to (sgl_wrapper, window_wrapper, android_native_window_wrapper)
public class SceneLib {
	public static native void init(int width, int height);
	public static native void free();
	public static native void render();

	public static void touch(MotionEvent event) {
		int eventId;
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			eventId = 0;
		else if (event.getAction() == MotionEvent.ACTION_UP)
			eventId = 1;
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
			eventId = 2;
		else
			return;  // ignore other actions
		touch(event.getX(), event.getY(), eventId);
	}

	private static native void touch(float x, float y, int event);

	static {
		System.loadLibrary("scene");
	}
}
