package com.example.hlavatovic.recyclerview;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

	public CustomAdapter() {
		// make some content
		_data = new String[ITEM_COUNT];
		for (int i = 0; i < ITEM_COUNT; ++i) {
			_data[i] = "this is element #" + i;
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(TAG, "RecyclerView element clicked");
				}
			});

			_photoView = (ImageView)v.findViewById(R.id.photoImageView);
			_captionView = (TextView)v.findViewById(R.id.textView);
		}

		public ImageView getPhotoView() {return _photoView;}
		public TextView getCaptionView() {return _captionView;}

		private final ImageView _photoView;
		private final TextView _captionView;
	}  // ViewHolder

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
			R.layout.gallery_row_item, viewGroup, false);

		return new ViewHolder(v);
	}

	// replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.getPhotoView().setImageResource(R.drawable.android_face);
		viewHolder.getCaptionView().setText(_data[position]);
	}

	// return the size of your dataset
	@Override
	public int getItemCount() {return _data.length;}

	private String [] _data;
	private static final int ITEM_COUNT = 60;
	private static final String TAG = "CustomAdapter";
}
