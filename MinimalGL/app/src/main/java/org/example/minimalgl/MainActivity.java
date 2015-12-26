package org.example.minimalgl;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_view = new GLSurfaceViewImpl(getApplication());
		setContentView(_view);
	}

	private GLSurfaceViewImpl _view;
}
