package org.libgl.wrapper;

public class NativeScene {

	public static native void create(int width, int  height);
	public static native void destroy();
	public static native void reshape(int width, int  height);
	public static native void display();

	static {
		System.loadLibrary("scene");  // loads libscene.so
	}
}
