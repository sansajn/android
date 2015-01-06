package com.example.opengltest.app;

public class Window {
	public static int getWidth() {return _width;}
	public static int getHeight() {return _height;}
	public static float getAspect() {return (float)_width/(float)_height;}

	public static void size(int w, int h) {
		_width = w;
		_height = h;
	}

	private static int _width;
	private static int _height;
}
