package com.base.engine.rendering.resourceManagement;

import static android.opengl.GLES20.*;

public class MeshResource {
	public MeshResource(int size) {
		_buffers = new int[2];
		glGenBuffers(2, _buffers, 0);
		this.size = size;
		this.refCount = 1;
	}

	@Override protected void finalize() {glDeleteBuffers(2, _buffers, 0);}

	public int getVbo() {return _buffers[0];}
	public int getIbo() {return _buffers[1];}
	public int getSize() {return size;}

	public void addReference() {refCount += 1;}
	public boolean removeReference() {return refCount-- == 0;}

	private int[] _buffers;  // vbo, ibo
	private int size;
	private int refCount;
}
