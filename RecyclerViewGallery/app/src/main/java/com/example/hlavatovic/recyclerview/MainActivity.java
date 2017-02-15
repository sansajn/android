package com.example.hlavatovic.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recycler = (RecyclerView)findViewById(R.id.recyclerView);
		recycler.setAdapter(new CustomAdapter());
		recycler.setLayoutManager(new LinearLayoutManager(this));
	}
}
