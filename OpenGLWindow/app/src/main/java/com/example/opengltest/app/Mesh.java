package com.example.opengltest.app;

import com.base.engine.core.Util;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Vertex;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;

import java.util.ArrayList;
import java.util.HashMap;

import static android.opengl.GLES20.*;

public class Mesh {
	public Mesh(String fileName) {
		this.fileName = fileName;
		MeshResource oldResource = loadedModels.get(fileName);
		if (oldResource != null) {
			resource = oldResource;
			resource.addReference();
		}
		else {
			loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}
	}

	public Mesh(Vertex[] vertices, int[] indices) {this(vertices, indices, false);}

	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals) {
		fileName = new String();
		addVertices(vertices, indices, calcNormals);
	}

	@Override protected void finalize() {
		if (resource.removeReference() && !fileName.isEmpty())
			loadedModels.remove(fileName);
	}

	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 3*4);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 5*4);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}

	public void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {
		if (calcNormals)
			calcNormals(vertices, indices);

		resource = new MeshResource(indices.length);

		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Vertex.SIZE * 4, Util.createBuffer(vertices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, Util.createBuffer(indices), GL_STATIC_DRAW);
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

	private void loadMesh(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length-1];

		if (!ext.equals("obj")) {
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}

		OBJModel test = new OBJModel(fileName);
		IndexedModel model = test.toIndexdModel();
		model.calcNormals();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for (int i = 0; i < model.getPositions().size(); ++i) {
			vertices.add(new Vertex(
				model.getPositions().get(i), model.getTexCoords().get(i), model.getNormals().get(i)));
		}

		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);

		addVertices(vertexData, Util.toIntArray(indexData), false);
	}

	private String fileName;
	private MeshResource resource;
	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
}
