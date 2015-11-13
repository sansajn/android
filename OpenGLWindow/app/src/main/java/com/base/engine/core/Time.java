package com.base.engine.core;

public class Time {
	public static double getTime() {return (double)System.nanoTime()/SECOND;}

	public static void init() {
		_prevFrameStart = _start = getTime();
	}

	public static void startFrame() {
		_prevFrameStart = _start;
		_start = getTime();
	}

	public static double frameDelta() {return _start - _prevFrameStart;}

	private static double _start;
	private static double _prevFrameStart;

	private static final double SECOND = 1000000000.0;
}
