package org.example.trianglegles2;

// wrapper pre nativnu kniznicu scene
public class SceneLib {
	public static native void init(int width, int height);
	public static native void free();
	public static native void render();

	static {
		System.loadLibrary("scene");
	}
}
