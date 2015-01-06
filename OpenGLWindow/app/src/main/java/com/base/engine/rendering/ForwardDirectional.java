package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.core.*;

public class ForwardDirectional extends Shader {
	public static ForwardDirectional getInstance() {return instance;}

	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
		material.getTexture("diffuse").bind();

		setUniform("model", worldMatrix);
		setUniform("MVP", projectedMatrix);

		setUniformf("specularIntensity", material.getFloat("specularIntensity"));
		setUniformf("specularPower", material.getFloat("specularPower"));

		setUniform("eyePos", renderingEngine.getMainCamera().getTransform().getTransformedPos());
		setUniformDirectionalLight("directionalLight", (DirectionalLight)renderingEngine.getActiveLight());
	}

	private ForwardDirectional() {
		super();

		addVertexShaderFromFile("forward_directional_vertex.glsl");
		addFragmentShaderFromFile("forward_directional_fragment.glsl");
		compileShader();

		addUniform("model");
		addUniform("MVP");

		addUniform("specularIntensity");
		addUniform("specularPower");
		addUniform("eyePos");

		addUniform("directionalLight.base.color");
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");

		addAttribute("position", 0);
		addAttribute("texCoord", 1);
		addAttribute("normal", 2);
	}

	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	private static final ForwardDirectional instance = new ForwardDirectional();
}
