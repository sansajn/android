package org.example.hellojni;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloJni extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText(stringFromJNI());
		setContentView(tv);
	}

	public native String stringFromJNI();
	public native String unimplementedStringFromJNI();

	static {
		System.loadLibrary("hello-jni");
	}
}
