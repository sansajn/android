package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.Camera;
import com.base.engine.core.GameObject;
import com.base.engine.core.Vector3f;

import java.util.ArrayList;

import static android.opengl.GLES20.*;


public class RenderingEngine {
	public RenderingEngine() {
		lights = new ArrayList<BaseLight>();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
//		glEnable(GL_DEPTH_CLAMP);  // todo: GL_DEPTH_CLAMP in opengl es 2.0
		glEnable(GL_TEXTURE_2D);

		ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
	}

	public void render(GameObject object) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Shader forwardAmbient = ForwardAmbient.getInstance();

		object.render(forwardAmbient, this);


		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);

		// todo: blending is not working ...
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
//		glDepthMask(false);
//		glDepthFunc(GL_EQUAL);

		for (BaseLight light : lights) {
			activeLight = light;
			object.render(light.getShader(), this);
		}

//		glDepthFunc(GL_LESS);
//		glDepthMask(true);
		glDisable(GL_BLEND);

		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
	}

	public static String getOpenGLVersion() {return glGetString(GL_VERSION);}
	public Camera getMainCamera() {return mainCamera;}
	public Vector3f getAmbientLight() {return ambientLight;}
	public BaseLight getActiveLight() {return activeLight;}

	public void addLight(BaseLight light) {lights.add(light);}
	public void addCamera(Camera camera) {mainCamera = camera;}

	private Camera mainCamera;
	private Vector3f ambientLight;

	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;
}
