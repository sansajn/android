package com.example.opengltest.app;

import com.base.engine.components.*;
import com.base.engine.core.*;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Vertex;

public class TestGame extends Game {
	public void init() {
		float fieldDepth = 10f;
		float fieldWidth = 10f;
		Vertex[] vertices = new Vertex[] {new Vertex(new Vector3f(-fieldWidth, 0, -fieldDepth), new Vector2f(0, 0)),
													 new Vertex(new Vector3f(-fieldWidth, 0, fieldDepth*3), new Vector2f(0, 1)),
													 new Vertex(new Vector3f(fieldWidth*3, 0, -fieldDepth), new Vector2f(1, 0)),
													 new Vertex(new Vector3f(fieldWidth*3, 0, fieldDepth*3), new Vector2f(1, 1))};

		int[] indices = new int[] {0, 1, 2,
											2, 1, 3};

		Mesh mesh = new Mesh(vertices, indices, true);

		Material material = new Material();
		material.addTexture("diffuse", new Texture("checker.png"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 8);

		GameObject planeObject = new GameObject();
		planeObject.addComponent(new MeshRenderer(mesh, material));
		planeObject.getTransform().getPos().set(0, -1, 5);

		GameObject cameraObject = new GameObject().addComponent(new FreeLook()).addComponent(
			new Camera((float)Math.toRadians(70), (float)Window.getWidth()/(float)Window.getHeight(), 0.1f, 1000.f));
		cameraObject.getTransform().setPos(new Vector3f(0,2,0));

		GameObject monkeyObject = new GameObject();
		monkeyObject.addComponent(new MeshRenderer(new Mesh("monkey3.obj"), material));
		monkeyObject.getTransform().setPos(new Vector3f(2, 1, 7));
		monkeyObject.getTransform().setRot(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(180)));

		GameObject directionalLightObject = new GameObject();
		directionalLightObject.addComponent(new DirectionalLight(new Vector3f(0, 0, 1), 0.8f));
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));

		GameObject pointLightObject = new GameObject();
		directionalLightObject.addComponent(new PointLight(new Vector3f(0,1,0), 0.4f, new Attenuation(0,0,0.1f)));

		GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(new SpotLight(new Vector3f(0,1,1), 0.4f, new Attenuation(0,0,0.1f), 0.7f));
		spotLightObject.getTransform().getPos().set(5, 0, 5);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(90)));

		addObject(planeObject);
		addObject(monkeyObject);
		addObject(cameraObject);
		addObject(directionalLightObject);
		addObject(pointLightObject);
		addObject(spotLightObject);
	}
}
