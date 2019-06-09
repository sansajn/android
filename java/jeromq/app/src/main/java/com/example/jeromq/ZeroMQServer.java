package com.example.jeromq;

import android.os.Handler;

import org.zeromq.ZMQ;

public class ZeroMQServer implements Runnable {

	public ZeroMQServer(Handler uiThread) {
		_uiThread = uiThread;
	}

	@Override
	public void run() {
		ZMQ.Context ctx = ZMQ.context(1);
		ZMQ.Socket rep = ctx.socket(ZMQ.REP);
		rep.bind("tcp://127.0.0.1:5555");

		while (!Thread.currentThread().isInterrupted()) {
			byte[] msg = rep.recv(0);
			_uiThread.sendMessage(
				Util.bundledMessage(_uiThread, new String(msg)));
			rep.send(new String(Util.reverseInPlace(msg)), 0);
		}

		rep.close();
		ctx.term();
	}

	private final Handler _uiThread;
}
