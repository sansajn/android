/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.opengltest.app;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Vertex;

import static android.opengl.GLES20.*;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Triangle {

	private final String vertexShaderCode =
			"uniform mat4 uMVPMatrix;" +
					"attribute vec3 vPosition;" +
					"void main() {" +
					"  gl_Position = uMVPMatrix * vec4(vPosition,1);" +
					"}";

	private final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec3 vColor;" +
					"void main() {" +
					"  gl_FragColor = vec4(vColor,1);" +
					"}";

	private final Vector3f color = new Vector3f(0.63671875f, 0.76953125f, 0.22265625f);

	private Shader _shader;
	private Mesh _mesh;

	public Triangle() {
		_shader = new Shader();
		_shader.addVertexShader(vertexShaderCode);
		_shader.addFragmentShader(fragmentShaderCode);
		_shader.compileShader();
		_shader.addUniform("uMVPMatrix");
		_shader.addUniform("vColor");
		_shader.addAttribute("vPosition", 0);


		Vertex[] vertices = new Vertex[] {
			new Vertex(new Vector3f(0.0f, 0.622008459f, 0.0f), new Vector2f(0,0)),
			new Vertex(new Vector3f(-0.5f, -0.311004243f, 0.0f), new Vector2f(0,0)),
			new Vertex(new Vector3f(0.5f, -0.311004243f, 0.0f), new Vector2f(0,0))
		};

		int[] indices = new int[] {0,1,2};

		_mesh = new Mesh();
		_mesh.addVertices(vertices, indices, false);
	}

	public void draw(Matrix4f matrix) {
		_shader.bind();

		_shader.setUniform("vColor", color);
		_shader.setUniform("uMVPMatrix", matrix);

		_mesh.draw();
	}

}
