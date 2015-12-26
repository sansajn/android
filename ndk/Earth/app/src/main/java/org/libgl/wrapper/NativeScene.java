package org.libgl.wrapper;

import android.view.MotionEvent;

public class NativeScene {

	// volaj pri vytvoreni egl kontextu (napr. GLSurfaceView.Renderer.onSurfaceChanged())
	public static native void init(int width, int height);

	// volaj pri uvolnovani egl kontextu (napr. GLSurfaceView.surfaceDestroyed())
	public static native void free();

	// volaj pri potrebe vykreslit scenu (GLSurfaceView.Renderrer.onDrawFrame())
	public static native void display();

	// volaj pri dotknuti displeya (napr. GLSurfaceView.onTouchEvent())
	public static void touch(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
			for (int idx = 0; idx < event.getPointerCount(); ++idx)
				touch((int)event.getX(idx), (int)event.getY(idx), event.getPointerId(idx), ACTION_MOVE);
			return;  // we are done
		}

		int pointerIdx, nativeAction;

		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				pointerIdx = 0;
				nativeAction = ACTION_DOWN;
				break;
			case MotionEvent.ACTION_UP:
				pointerIdx = 0;
				nativeAction = ACTION_UP;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				nativeAction = ACTION_DOWN;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				nativeAction = ACTION_UP;
				break;
			case MotionEvent.ACTION_CANCEL:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				nativeAction = ACTION_CANCEL;
				break;
			default: return;  // ignore other actions
		}

		touch((int)event.getX(pointerIdx), (int)event.getY(pointerIdx), event.getPointerId(pointerIdx), nativeAction);
	}

	private static native void touch(int x, int y, int pointerId, int action);

	private static int ACTION_DOWN = 0;
	private static int ACTION_UP = 1;
	private static int ACTION_MOVE = 2;
	private static int ACTION_CANCEL = 3;

	static {
		System.loadLibrary("scene");  // libscene.so
	}
}
