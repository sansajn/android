package com.base.engine.components;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.ForwardSpot;

public class SpotLight extends PointLight {
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float cuttof) {
		super(color, intensity, attenuation);
		this.cuttof = cuttof;
		setShader(ForwardSpot.getInstance());
	}

	public Vector3f getDirection() {return getTransform().getTransformedRot().getForward();}
	public float getCuttof() {return cuttof;}

	private float cuttof;
}
