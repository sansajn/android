package org.example.multitouchdemo1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MultiTouchDemo1Activity extends Activity implements View.OnTouchListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_touch_demo1);

		RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.layout1);
		layout1.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		String tag = v.getTag().toString();
		Log.v(tag, "--------------------------------------");
		Log.v(tag, "Got view " + tag + " in onTouch");
		Log.v(tag, describeEvent(event));
		logAction(event);
		if ("true".equals(tag.substring(0,4)))
			return true;
		else
			return false;
	}

	protected static String describeEvent(MotionEvent event) {
		StringBuilder result = new StringBuilder(500);

		int pointerIdx;
		String actionName;
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: actionName = "ACTION_DOWN"; break;
			case MotionEvent.ACTION_UP: actionName = "ACTION_UP";	break;
			case MotionEvent.ACTION_MOVE: actionName = "ACTION_MOVE"; break;
			case MotionEvent.ACTION_POINTER_DOWN:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				actionName = "ACTION_POINTER_DOWN(id:" + event.getPointerId(pointerIdx) + ")"; break;
			case MotionEvent.ACTION_POINTER_UP:
				pointerIdx = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				actionName = "ACTION_POINTER_UP(id:" + event.getPointerId(pointerIdx) + ")"; break;
			default: actionName = "other";
		}

		result.append("Action: ").append(event.getAction()).append(" (" + actionName + ")").append("\n");
		int numPointers = event.getPointerCount();
		result.append("Number of pointers: ");
		result.append(numPointers).append("\n");
		int ptrIdx = 0;
		while (ptrIdx < numPointers) {
			int ptrId = event.getPointerId(ptrIdx);
			result.append("Pointer index:").append(ptrIdx);
			result.append(", Pointer id: ").append(ptrId).append("\n");
			result.append("   Location: ").append(event.getX(ptrIdx)).append(" x ").append(event.getY(ptrIdx)).append("\n");
			result.append("   Pressure: ").append(event.getPressure(ptrIdx));
			result.append("   Size: ").append(event.getSize(ptrIdx));
			result.append("\n");
			ptrIdx++;
		}

		result.append("Down time: ").append(event.getDownTime()).append("ms\n");
		result.append("Event time: ").append(event.getEventTime()).append("ms").append(" Elapsed: ");
		result.append(event.getEventTime() - event.getDownTime());
		result.append(" ms\n");
		return result.toString();
	}

	private void logAction(MotionEvent event) {
		int action = event.getActionMasked();
		int ptrIndex = event.getActionIndex();
		int ptrId = event.getPointerId(ptrIndex);
		if (action == 5 || action == 6)
			action = action - 5;
		Log.v("Action", "Pointer index: " + ptrIndex);
		Log.v("Action", "Pointer id: " + ptrId);
		Log.v("Action", "True action value: " + action);
	}
}
