package com.base.engine.rendering.resourceManagement;

import static android.opengl.GLES20.*;

public class TextureResource {
	public TextureResource() {
		ids = new int[1];
		glGenTextures(1, ids, 0);
		refCount = 1;
	}

	@Override protected void finalize() {glDeleteTextures(1, ids, 0);}

	public int getId() {return ids[0];}

	public void addReference() {refCount += 1;}
	public boolean removeReference() {return refCount-- == 0;}

	private int[] ids;
	private int refCount;
}
