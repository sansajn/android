package com.example.jeromq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_textView = (TextView)findViewById(R.id.textView);
		_editText = (EditText)findViewById(R.id.editText);

		new Thread(new ZeroMQServer(_serverMessageHandler)).start();

		findViewById(R.id.button).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new ZeroMQMessageTask(_clientMessageHandler).execute(_editText.getText().toString());
				}
			}
		);
	}

	private static String getTimeString() {
		return DATE_FORMAT.format(new Date());
	}

	private final MessageListenerHandler _serverMessageHandler = new MessageListenerHandler(
		new MessageListener() {
			@Override
			public void messageReceived(String messageBody) {
				_textView.append(getTimeString() + " - server received: " + messageBody + "\n");
			}
		}, Util.MESSAGE_PAYLOAD_KEY
	);

	private final MessageListenerHandler _clientMessageHandler = new MessageListenerHandler(
		new MessageListener() {
			@Override
			public void messageReceived(String messageBody) {
				_textView.append(getTimeString() + " - client received: " + messageBody + "\n");
			}
		}, Util.MESSAGE_PAYLOAD_KEY
	);

	private TextView _textView;
	private EditText _editText;
}
