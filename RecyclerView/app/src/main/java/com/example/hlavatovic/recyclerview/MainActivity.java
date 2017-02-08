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
		recycler.setAdapter(new CustomAdapter(generateDataSet()));
		recycler.setLayoutManager(new LinearLayoutManager(this));
	}

	private String [] generateDataSet() {
		String [] result = new String[ITEM_COUNT];
		for (int i = 0; i < ITEM_COUNT; ++i)
			result[i] = "this is element #" + i;
		return result;
	}

	private static final int ITEM_COUNT = 60;
}
