package com.base.engine.rendering;

import com.base.engine.core.Vector3f;

import java.util.HashMap;

public class Material {
	public Material() {
		textureHash = new HashMap<String, Texture>();
		vector3fHash = new HashMap<String, Vector3f>();
		floatHash = new HashMap<String, Float>();
	}

	public void addTexture(String name, Texture texture) {textureHash.put(name, texture);}
	public void addVector3f(String name, Vector3f vector3f) {vector3fHash.put(name, vector3f);}
	public void addFloat(String name, float floatValue) {floatHash.put(name, floatValue);}

	public Texture getTexture(String name) {
		Texture result = textureHash.get(name);
		if (result != null)
			return result;
		return new Texture("test.png");
	}

	public Vector3f getVector3f(String name) {
		Vector3f result = vector3fHash.get(name);
		if (result != null)
			return result;
		return new Vector3f(0,0,0);
	}

	public float getFloat(String name) {
		Float result = floatHash.get(name);
		if (result != null)
			return result;
		return 0;
	}

	private HashMap<String, Texture> textureHash;
	private HashMap<String, Vector3f> vector3fHash;
	private HashMap<String, Float> floatHash;
}
