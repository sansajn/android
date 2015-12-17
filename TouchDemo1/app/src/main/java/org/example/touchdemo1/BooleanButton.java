package org.example.touchdemo1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class BooleanButton extends Button {
	protected boolean myValue() {
		return false;
	}

	public BooleanButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		String tag = getTag().toString();
		Log.v(tag, "--------------------------------------");
		Log.v(tag, TouchDemo1Activity.describeEvent(this, event));
		Log.v(tag, "super onTouchEvent() returns " + super.onTouchEvent(event));
		Log.v(tag, "and I'm returning " + myValue());
		return myValue();
	}
}
