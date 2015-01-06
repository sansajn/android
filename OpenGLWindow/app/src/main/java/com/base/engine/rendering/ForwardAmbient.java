package com.base.engine.rendering;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.Transform;

public class ForwardAmbient extends Shader {
	public static ForwardAmbient getInstance() {return instance;}

	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		material.getTexture("diffuse").bind();

		setUniform("MVP", projectedMatrix);
		setUniform("ambientIntensity", renderingEngine.getAmbientLight());
	}

	private ForwardAmbient() {
		super();

		addVertexShaderFromFile("forward_ambient_vertex.glsl");
		addFragmentShaderFromFile("forward_ambient_fragment.glsl");
		compileShader();

		addUniform("MVP");
		addUniform("ambientIntensity");

		addAttribute("position", 0);
		addAttribute("texCoord", 1);
	}

	private static final ForwardAmbient instance = new ForwardAmbient();
}
