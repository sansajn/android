package com.base.engine.rendering;

import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class Vertex {
	public static final int SIZE = 8;

	public Vertex(Vector3f pos) {this(pos, new Vector2f(0, 0));}
	public Vertex(Vector3f pos, Vector2f texCoord) {this(pos, texCoord, new Vector3f(0, 0, 0));}

	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal) {
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
	}

	public Vector3f getPos() {return pos;}
	public Vector2f getTexCoord() {return texCoord;}
	public Vector3f getNormal() {return normal;}
	public void setPos(Vector3f p) {pos = p;}
	public void setTexCoord(Vector2f uv) {texCoord = uv;}
	public void setNormal(Vector3f n) {normal = n;}

	private Vector3f pos;
	private Vector2f texCoord;
	private Vector3f normal;
}
