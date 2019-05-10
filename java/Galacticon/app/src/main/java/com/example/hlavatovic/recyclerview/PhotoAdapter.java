package com.example.hlavatovic.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

	public PhotoAdapter(ArrayList<Photo> photos) {
		_photos = photos;
	}

	public static class PhotoHolder extends RecyclerView.ViewHolder {
		public PhotoHolder(View v) {
			super(v);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(TAG, "RecyclerView element clicked");
				}
			});

			_imageView = (ImageView)v.findViewById(R.id.item_image);
			_dateView = (TextView)v.findViewById(R.id.item_date);
			_descriptionView = (TextView)v.findViewById(R.id.item_description);
		}

		public void bindPhoto(Photo photo) {
			_photo = photo;
			Picasso.with(_imageView.getContext()).load(photo.getUrl()).into(_imageView);
			_dateView.setText(photo.getHumanDate());
			_descriptionView.setText(photo.getExplanation());
		}

		private ImageView _imageView;
		private TextView _dateView;
		private TextView _descriptionView;
		private Photo _photo;
	}  // PhotoHolder

	@Override
	public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
			R.layout.item_row, viewGroup, false);

		return new PhotoHolder(v);
	}

	// replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(PhotoHolder photoHolder, final int position) {
		Photo photo = _photos.get(position);
		photoHolder.bindPhoto(photo);
	}

	// return the size of your dataset
	@Override
	public int getItemCount() {return _photos.size();}

	private ArrayList<Photo> _photos;
	private static final String TAG = "PhotoAdapter";
}
