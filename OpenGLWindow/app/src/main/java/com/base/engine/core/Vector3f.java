package com.base.engine.core;

public class Vector3f {
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {return x;}
	public float getY() {return y;}
	public float getZ() {return z;}
	public float length() {return (float)Math.sqrt(x*x + y*y + z*z);}
	public float max() {return Math.max(x, Math.max(y, z));}
	public float dot(Vector3f r) {return x*r.getX() + y*r.getY() + z*r.getZ();}

	public Vector3f cross(Vector3f r) {
		float x_ = y*r.getZ() - z*r.getY();
		float y_ = z*r.getX() - x*r.getZ();
		float z_ = x*r.getY() - y*r.getX();
		return new Vector3f(x_, y_, z_);
	}

	public Vector3f normalized() {
		float length = length();
		return new Vector3f(x/length, y/length, z/length);
	}

	public Vector3f rotate(Vector3f axis, float angle) {
		return this.rotate(new Quaternion(axis, angle));
	}

	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();
		Quaternion w = rotation.mul(this).mul(conjugate);
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}

	public Vector3f lerp(Vector3f dest, float lerpFactor) {  // linearna interpolacia, lerpFactor = <0,1>
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector3f abs() {return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));}

	public Vector3f add(Vector3f r) {return new Vector3f(x+r.getX(), y+r.getY(), z+r.getZ());}
	public Vector3f add(float r) {return new Vector3f(x+r, y+r, z+r);}
	public Vector3f sub(Vector3f r) {return new Vector3f(x-r.getX(), y-r.getY(), z-r.getZ());}
	public Vector3f sub(float r) {return new Vector3f(x-r, y-r, z-r);}
	public Vector3f mul(Vector3f r) {return new Vector3f(x*r.getX(), y*r.getY(), z*r.getZ());}
	public Vector3f mul(float r) {return new Vector3f(x*r, y*r, z*r);}
	public Vector3f div(Vector3f r) {return new Vector3f(x/r.getX(), y/r.getY(), z/r.getZ());}
	public Vector3f div(float r) {return new Vector3f(x/r, y/r, z/r);}

	public Vector2f getXY() {return new Vector2f(x, y);}
	public Vector2f getXZ() {return new Vector2f(x, z);}
	public Vector2f getYX() {return new Vector2f(y, x);}
	public Vector2f getYZ() {return new Vector2f(y, z);}
	public Vector2f getZX() {return new Vector2f(z, x);}
	public Vector2f getZY() {return new Vector2f(z, y);}

	public boolean equals(Vector3f r) {return x == r.getX() && y == r.getY() && z == r.getZ();}

	public Vector3f set(float x, float y, float z) {this.x = x; this.y = y; this.z = z; return this;}
	public Vector3f set(Vector3f r) {return set(r.getX(), r.getY(), r.getZ());}
	public void setX(float x) {this.x = x;}
	public void setY(float y) {this.y = y;}
	public void setZ(float z) {this.z = z;}

	private float x, y, z;
}

