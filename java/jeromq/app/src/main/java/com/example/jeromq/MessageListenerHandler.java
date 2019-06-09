package com.example.jeromq;

import android.os.Handler;
import android.os.Message;

public class MessageListenerHandler extends Handler {

	public MessageListenerHandler(MessageListener messageListener, String payloadKey) {
		_messageListener = messageListener;
		_payloadKey = payloadKey;
	}

	@Override
	public void handleMessage(Message msg) {
		_messageListener.messageReceived(msg.getData().getString(_payloadKey));
	}

	private final MessageListener _messageListener;
	private final String _payloadKey;
}
