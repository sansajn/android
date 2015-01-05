package com.base.engine.rendering;

import com.base.engine.core.*;
import com.example.opengltest.app.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import static android.opengl.GLES20.*;

public class Shader {
	public Shader() {
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();

		if (program == 0) {
			System.err.println("shader program creation failed");
			System.exit(1);
		}
	}

	public int getProgramId() {return program;}

	public void bind() {glUseProgram(program);}
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {}

	public void compileShader() {
		glLinkProgram(program);

		int[] linkStatus = new int[1];
		glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);
		if (linkStatus[0] == 0) {
			System.err.println(glGetShaderInfoLog(program));
			System.exit(1);
		}

		glValidateProgram(program);

		int[] validateStatus = new int[1];
		glGetProgramiv(program, GL_VALIDATE_STATUS, validateStatus, 0);
		if (validateStatus[0] == 0) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
	}

	public void addVertexShader(String text) {addProgram(text, GL_VERTEX_SHADER);}
	public void addFragmentShader(String text) {addProgram(text, GL_FRAGMENT_SHADER);}
	public void addVertexShaderFromFile(String fileName) {addProgram(loadShader(fileName), GL_VERTEX_SHADER);}
	public void addFragmentShaderFromFile(String fileName) {addProgram(loadShader(fileName), GL_FRAGMENT_SHADER);}

	public void addUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(program, uniformName);
		if (uniformLocation == -1) {
			System.err.println("Error: Could not find uniform '" + uniformName + "'");
			new Exception().printStackTrace();
			System.exit(1);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void addAttribute(String name, int desiredLocation) {
		glBindAttribLocation(program, desiredLocation, name);
	}

	public void setUniformi(String uniformName, int value) {glUniform1i(uniforms.get(uniformName), value);}
	public void setUniformf(String uniformName, float value) {glUniform1f(uniforms.get(uniformName), value);}
	public void setUniform(String uniformName, Vector3f value) {glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());}

	public void setUniform(String uniformName, Matrix4f value) {
//		glUniformMatrix4fv(uniforms.get(uniformName), 1, false, Util.createFlippedBuffer(value));
		glUniformMatrix4fv(uniforms.get(uniformName), 1, false, value.getAsArray(), 0);
	}

	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		if (shader == 0) {
			System.err.println("shader creation failed");
			System.exit(1);
		}

		glShaderSource(shader, text);
		glCompileShader(shader);

		int[] compileStatus = new int[1];
		glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatus, 0);
		if (compileStatus[0] == 0) {
			System.err.println(glGetShaderInfoLog(shader));
			System.exit(1);
		}

		glAttachShader(program, shader);
	}

	private static String loadShader(String filename) {
		StringBuilder shaderSource = new StringBuilder();
		final String INCLUDE_DIRECTIVE = "#include";

		try {
			BufferedReader shaderReader = new BufferedReader(new FileReader("./res/shaders/" + filename));

			String line;
			while ((line = shaderReader.readLine()) != null) {
				if (line.startsWith(INCLUDE_DIRECTIVE)) {
					shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length()+2, line.length() - 1)));
				}
				else
					shaderSource.append(line).append("\n");
			}
			shaderReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return shaderSource.toString();
	}

	private int program;
	private HashMap<String, Integer> uniforms;
}
