package com.base.engine.core;

public class Vector2f {
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {return x;}
	public float getY() {return y;}
	public float length() {return (float)Math.sqrt(x*x + y*y);}
	public float max() {return Math.max(x, y);}
	public float dot(Vector2f r) {return x*r.getX() + y*r.getY();}


	public Vector2f set(float x, float y) {this.x = x; this.y = y; return this;}
	public Vector2f set(Vector2f r) {return set(r.getX(), r.getY());}
	public void setX(float x) {this.x = x;}
	public void setY(float y) {this.y = y;}

	public Vector2f normalized() {
		float length = length();
		return new Vector2f(x/length, y/length);
	}

	public float cross(Vector2f r) {return x*r.getY() - y*r.getX();}

	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		return new Vector2f((float)(x*cos - y*sin), (float)(x*sin + y*cos));
	}

	public Vector2f lerp(Vector2f dest, float lerpFactor) {  // linearna interpolacia, lerpFactor = <0,1>
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector2f add(Vector2f r) {return new Vector2f(x+r.getX(), y+r.getY());}
	public Vector2f add(float r) {return new Vector2f(x+r, y+r);}
	public Vector2f sub(Vector2f r) {return new Vector2f(x-r.getX(), y-r.getY());}
	public Vector2f sub(float r) {return new Vector2f(x-r, y-r);}
	public Vector2f mul(Vector2f r) {return new Vector2f(x*r.getX(), y*r.getY());}
	public Vector2f mul(float r) {return new Vector2f(x*r, y*r);}
	public Vector2f div(Vector2f r) {return new Vector2f(x/r.getX(), y/r.getY());}
	public Vector2f div(float r) {return new Vector2f(x/r, y/r);}

	public boolean equals(Vector2f r) {return x == r.getX() && y == r.getY();}

	public String toString() {return "(" + x + ", " + y + ")";}

	private float x, y;
}
