package com.example.opengltest.app;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Vertex;

import static android.opengl.GLES20.*;

public class Mesh {

	public void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {
		if (calcNormals)
			calcNormals(vertices, indices);

		createBufferObjects();
		_size = indices.length;

		glBindBuffer(GL_ARRAY_BUFFER, _buffers[0]);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Vertex.SIZE * 4, Util.createBuffer(vertices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _buffers[1]);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, Util.createBuffer(indices), GL_STATIC_DRAW);
	}

	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, _buffers[0]);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 3*4);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 5*4);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _buffers[1]);
		glDrawElements(GL_TRIANGLES, _size, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}

	private void calcNormals(Vertex[] vertices, int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i+1];
			int i2 = indices[i+2];

			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());

			Vector3f normal = v1.cross(v2).normalized();

			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}

		for (int i = 0; i < vertices.length; ++i)
			vertices[i].setNormal(vertices[i].getNormal().normalized());
	}

	private void createBufferObjects() {
		_buffers = new int[2];
		glGenBuffers(_buffers.length, _buffers, 0);
		if (_buffers[0] == 0) {
			throw new RuntimeException("Could not create a new buffer objects (vbo, ibo).");
		}
	}

	private int[] _buffers;  // vbo, ibo
	private int _size;
}
