package com.base.engine.core;

import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;

public class CoreEngine {
	public CoreEngine(double framerate, Game game) {
		isRunning = false;
		this.game = game;
		this.frameTime = 1.0/framerate;
		isInitialised = false;
		game.setEngine(this);
	}

	public void createWindow(String title) {
//		Window.createWindow(width, height, title);
		this.renderingEngine = new RenderingEngine();
	}

//	public void start() {
//		if (isRunning)
//			return;
//		run();
//	}

//	public void stop() {
//		if (!isRunning)
//			return;
//		isRunning = false;
//	}

	public RenderingEngine getRenderingEngine() {return renderingEngine;}

	public void loopStep() {  // todo: co takto startTrame/newFrame/oneFrame/frame/gameFrame/loopStep
		if (!isInitialised) {
			isRunning = true;
			game.init();
			Time.init();
			isInitialised = true;
		}

		Time.startFrame();

//		if (Window.isCloseRequested())
//			stop();

		game.input((float)frameTime);
//		Input.update();
		game.update((float)frameTime);

		game.render(renderingEngine);
//		Window.render();

		try {
			Thread.sleep(1);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	private void run() {
//		while (isRunning)
//			loopStep();
//		cleanUp();
//	}

	private void cleanUp() {
//		Window.dispose();
	}

	private boolean isRunning;
	private Game game;
	private RenderingEngine renderingEngine;
	private double frameTime;
	private boolean isInitialised;
}
