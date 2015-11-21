package org.example.loadres;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoadResActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_assets = getResources().getAssets();
		TextView tv = new TextView(this);
		tv.setText(load_text(_assets, "raw/test.txt"));
		setContentView(tv);
	}

	private AssetManager _assets;

	private static native String load_text(AssetManager assetManager, String path);

	static {
		System.loadLibrary("load");
	}
}
