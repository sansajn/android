package org.example.earth;

import android.view.MotionEvent;

// wrapper pre nativnu kniznicu scene TODO: premenuj to (sgl_wrapper, window_wrapper, android_native_window_wrapper)
public class SceneLib {
	public static native void init(int width, int height);
	public static native void free();
	public static native void render();

	public static void touch(MotionEvent event) {

		// native-action 0:down, 1:up, 2:move
		if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			for (int idx = 0; idx < event.getPointerCount(); ++idx)
				touch(event.getX(idx), event.getY(idx), 2);  // 2 for move action
			return;  // we are done
		}

		int pointerIdx, nativeActionId;

		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				pointerIdx = 0;
				nativeActionId = 0;
				break;
			case MotionEvent.ACTION_UP:
				pointerIdx = 0;
				nativeActionId = 1;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				nativeActionId = 0;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				nativeActionId = 1;
				break;
			default: return;  // ignore other actions
		}

		touch(event.getX(pointerIdx), event.getY(pointerIdx), nativeActionId);
	}

	private static native void touch(float x, float y, int action);

	static {
		System.loadLibrary("scene");
	}
}
