package com.example.opengltest.app;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import com.base.engine.core.Transform;
import com.base.engine.core.Vector3f;

public class MyGLSurface extends GLSurfaceView {
	public MyGLSurface(Context context) {
		super(context);
		setEGLContextClientVersion(2);  // opengl es 2 context

		_renderer = new MyRenderer();
		setRenderer(_renderer);

		_transform = new Transform();
	}

	@Override public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - _prevX;
				float dy = y - _prevY;

				boolean rotY = dx != 0.0;
				boolean rotX = dy != 0.0;

				if (rotY)
					_transform.rotate(new Vector3f(0,1,0), (float)Math.toRadians(dx));
				if (rotX)
					_transform.rotate(new Vector3f(1,0,0), (float)Math.toRadians(dy));

				if (rotY || rotX) {
					Input.transform = _transform;
					Input.dirty = true;
				}

//				Log.d("GLSurface", "dx:" + dx + ", dy:" + dy);
		}

		_prevX = x;
		_prevY = y;

		return true;
	}

	private MyRenderer _renderer;
	private float _prevX, _prevY;
	private Transform _transform;
}
