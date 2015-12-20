package org.example.loadingjpeg;

public class SceneWrapper {
	public static native void init(int width, int height);
	public static native void render();
	public static native void free();

	static {
		System.loadLibrary("loadjpeg");
	}
}

