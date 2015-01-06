package com.base.engine.rendering;

import com.base.engine.core.Vector3f;

public class Attenuation {
	public Attenuation(float constant, float linear, float exponent) {
		coefs = new Vector3f(constant, linear, exponent);
	}

	public float getConstant() {return coefs.getX();}
	public float getLinear() {return coefs.getY();}
	public float getExponent() {return coefs.getZ();}

	private Vector3f coefs;
}
