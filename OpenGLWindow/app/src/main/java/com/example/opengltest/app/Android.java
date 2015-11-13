package com.example.opengltest.app;

import android.app.Activity;
import android.content.res.AssetManager;

public class Android {
	public static Activity mainActivity;

	public static AssetManager getAssets() {return mainActivity.getAssets();}
}
