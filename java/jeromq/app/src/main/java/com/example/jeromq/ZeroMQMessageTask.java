package com.example.jeromq;

import android.os.AsyncTask;
import android.os.Handler;

import org.zeromq.ZMQ;

public class ZeroMQMessageTask extends AsyncTask<String, Void, String> {

	public ZeroMQMessageTask(Handler uiThread) {
		_uiThread = uiThread;
	}

	@Override
	protected String doInBackground(String ... params) {
		ZMQ.Context ctx = ZMQ.context(1);
		ZMQ.Socket req = ctx.socket(ZMQ.REQ);
		req.connect("tcp://127.0.0.1:5555");

		req.send(params[0].getBytes(), 0);
		String result = new String(req.recv(0));

		req.close();
		ctx.term();

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		_uiThread.sendMessage(Util.bundledMessage(_uiThread, result));
	}

	private final Handler _uiThread;
}
