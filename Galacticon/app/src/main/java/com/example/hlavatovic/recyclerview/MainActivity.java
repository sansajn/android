package com.example.hlavatovic.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
		_linearLayout = new LinearLayoutManager(this);
		_gridLayout = new GridLayoutManager(this, 2);

		_recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		_recyclerView.setAdapter(_photoAdapter);
		_recyclerView.setLayoutManager(_linearLayout);
		setRecyclerViewScrollListener();
		setRecyclerViewItemTouchListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (_photos.isEmpty())
			requestPhoto();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_change_recycler_manager) {
			changeLayoutManager();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	private int getLastVisibleItemPosition() {
		if (_recyclerView.getLayoutManager().equals(_linearLayout))
			return _linearLayout.findLastVisibleItemPosition();
		else
			return _gridLayout.findLastVisibleItemPosition();
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

	private void setRecyclerViewItemTouchListener() {
		ItemTouchHelper.SimpleCallback itemTouchCallback =
			new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
				@Override
				public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
					return false;
				}

				@Override
				public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
					int position = viewHolder.getAdapterPosition();
					_photos.remove(position);
					_recyclerView.getAdapter().notifyItemRemoved(position);
				}
			};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
		itemTouchHelper.attachToRecyclerView(_recyclerView);
	}

	private void changeLayoutManager() {
		if (_recyclerView.getLayoutManager().equals(_linearLayout)) {
			_recyclerView.setLayoutManager(_gridLayout);
			if (_photos.size() == 1)
				requestPhoto();
		}
		else
			_recyclerView.setLayoutManager(_linearLayout);
	}

	private ArrayList<Photo> _photos;
	private ImageRequester _imageRequester;
	private PhotoAdapter _photoAdapter;
	private LinearLayoutManager _linearLayout;
	private GridLayoutManager _gridLayout;
	private RecyclerView _recyclerView;
}
