package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;

public class BaseLight extends GameComponent {
	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}

	public Vector3f getColor() {return color;}
	public float getIntensity() {return intensity;}
	public Shader getShader() {return shader;}

	public void setShader(Shader shader) {this.shader = shader;}

	@Override public void addToEngine(CoreEngine engine) {engine.getRenderingEngine().addLight(this);}

	private Vector3f color;
	private float intensity;
	private Shader shader;
}
