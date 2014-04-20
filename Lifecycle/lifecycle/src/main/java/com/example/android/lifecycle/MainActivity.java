package com.example.android.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//@ Vyzualizuje zivotny cyklus aplykacie.
public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_out = (TextView)findViewById(R.id.textOut);
		appendEvent("onCreate()");
	}

	protected void onStart() {
		appendEvent("onStart()");
		super.onStart();
	}

	protected void onRestart() {
		appendEvent("onRestart()");
		super.onRestart();
	}

	protected void onResume() {
		appendEvent("onResume()");
		super.onResume();
	}

	protected void onPause() {
		appendEvent("onPause()");
		super.onPause();
	}

	protected void onStop() {
		appendEvent("onStop()");
		super.onStop();
	}

	protected void onDestroy() {
		appendEvent("onDestroy()");
		super.onDestroy();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		appendEvent("onCreateOptionsMenu()");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		appendEvent("onOptionsItemSelected()");
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void appendEvent(String e) {
		_events.add(e);
		dumpEvents();
	}

	private void dumpEvents() {
		String text = "";
		for (String e : _events)
			text += e + "\n";
		_out.setText(text);
	}

	private List<String> _events = new ArrayList<String>();
	private TextView _out;
}
