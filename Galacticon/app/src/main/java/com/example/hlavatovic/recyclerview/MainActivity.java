package com.example.hlavatovic.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements ImageRequester.ImageRequesterResponse {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_photos = new ArrayList<>();
		_imageRequester = new ImageRequester(this);
		_photoAdapter = new PhotoAdapter(_photos);
		_layoutManager = new LinearLayoutManager(this);

		_recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		_recyclerView.setAdapter(_photoAdapter);
		_recyclerView.setLayoutManager(_layoutManager);
		setRecyclerViewScrollListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (_photos.isEmpty())
			requestPhoto();
	}

	private int getLastVisibleItemPosition() {
		return _layoutManager.findLastVisibleItemPosition();
	}

	@Override
	public void receivedNewPhoto(final Photo p) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_photos.add(p);
				_photoAdapter.notifyItemInserted(_photos.size());
			}
		});
	}

	private void requestPhoto() {
		try {
			_imageRequester.getPhoto();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setRecyclerViewScrollListener() {
		_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				int totalItemCount = _recyclerView.getLayoutManager().getItemCount();
				if (!_imageRequester.isLoadingData() && totalItemCount == getLastVisibleItemPosition() + 1)
					requestPhoto();
			}
		});
	}

	private ArrayList<Photo> _photos;
	private ImageRequester _imageRequester;
	private PhotoAdapter _photoAdapter;
	private LinearLayoutManager _layoutManager;
	private RecyclerView _recyclerView;
}
