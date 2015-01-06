package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Matrix4f;

public class DummyCamera extends Camera {
	public DummyCamera() {super(1, 1, 1, 1);}
	public Matrix4f getViewProjection() {return new Matrix4f().initIdentity();}
	@Override public void addToEngine(CoreEngine engine) {engine.getRenderingEngine().addCamera(this);}
}
