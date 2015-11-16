package org.example.cpp11;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Cpp11Activity extends ActionBarActivity {

	private native int sumArray(int arr[]);

	static {
		System.loadLibrary("cpp11");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int arr[] = new int[10];
		arr[0] = 3;
		arr[1] = 7;
		arr[2] = 45;
		arr[3] = 9;
		arr[4] = 32;
		arr[5] = 23;
		arr[6] = 44;
		arr[7] = 12;
		arr[8] = 34;
		arr[9] = 56;
		int sum = sumArray(arr);

		TextView tv = new TextView(this);
		tv.setText(String.format("C++11 Sum: %d", sum));
		setContentView(tv);
	}
}
