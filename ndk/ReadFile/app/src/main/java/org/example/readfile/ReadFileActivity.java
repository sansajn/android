package org.example.readfile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReadFileActivity extends ActionBarActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		String path = "/sdcard/test_files/test.txt";
		tv.setText(read_text_c(path) + "\n" + read_text_cpp(path));
		setContentView(tv);
	}

	private native String read_text_c(String path);
	private native String read_text_cpp(String path);

	static {
		System.loadLibrary("load");
	}
}
