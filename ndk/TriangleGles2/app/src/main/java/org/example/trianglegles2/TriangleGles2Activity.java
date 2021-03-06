package org.example.trianglegles2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class TriangleGles2Activity extends ActionBarActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_view = new TriangleView(getApplication());
		setContentView(_view);
	}

	@Override protected void onPause() {
		super.onPause();
		_view.onPause();
	}

	@Override protected void onResume() {
		super.onResume();
		_view.onResume();
	}

	TriangleView _view;
}
