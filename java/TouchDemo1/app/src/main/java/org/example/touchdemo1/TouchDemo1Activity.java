package org.example.touchdemo1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TouchDemo1Activity extends Activity implements View.OnTouchListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_demo1);

		RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.layout1);
		layout1.setOnTouchListener(this);
		Button trueBtn1 = (Button)findViewById(R.id.trueBtn1);
		trueBtn1.setOnTouchListener(this);
		Button falseBtn1 = (Button)findViewById(R.id.falseBtn1);
		falseBtn1.setOnTouchListener(this);

		RelativeLayout layout2 = (RelativeLayout)findViewById(R.id.layout2);
		layout2.setOnTouchListener(this);
		Button trueBtn2 = (Button)findViewById(R.id.trueBtn2);
		trueBtn2.setOnTouchListener(this);
		Button falseBtn2 = (Button)findViewById(R.id.falseBtn2);
		falseBtn2.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		String tag = v.getTag().toString();
		Log.v(tag, "--------------------------------------");
		Log.v(tag, "Got view " + tag + " in onTouch");
		Log.v(tag, describeEvent(v, event));
		if ("true".equals(tag.substring(0,4))) {
			Log.v(tag, "and I'm returning true");
			return true;
		}
		else {
			Log.v(tag, "and I'm returning false");
			return false;
		}
	}

	protected static String describeEvent(View view, MotionEvent event) {
		StringBuilder result = new StringBuilder(300);

		String actionName;
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			actionName = "ACTION_DOWN";
		else if (event.getAction() == MotionEvent.ACTION_UP)
			actionName = "ACTION_UP";
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
			actionName = "ACTION_MOVE";
		else
			actionName = "other";

		result.append("Action: ").append(event.getAction()).append(" (" + actionName + ")").append("\n");
		result.append("Location: ").append(event.getX()).append(" x ").append(event.getY()).append("\n");
		if (event.getX() < 0 || event.getX() > view.getWidth() || event.getY() < 0 || event.getY() > view.getHeight())
			result.append(">>> Touch has left the view <<<\n");
		result.append("edge flags: ").append(event.getEdgeFlags());
		result.append("\n");
		result.append("Pressure: ").append(event.getPressure());
		result.append("   ").append("Size: ").append(event.getSize());
		result.append("\n").append("Down time: ").append(event.getDownTime()).append("ms\n");
		result.append("Event time: ").append(event.getEventTime()).append("ms").append(" Elapsed: ");
		result.append(event.getEventTime() - event.getDownTime());
		result.append(" ms\n");
		return result.toString();
	}
}
