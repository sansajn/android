package com.example.hlavatovic.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

	public CustomAdapter(String [] data) {
		_data = data;
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

			_textView = (TextView)v.findViewById(R.id.textView);
		}

		public TextView getTextView() {return _textView;}

		private final TextView _textView;
	}  // ViewHolder

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
			R.layout.text_row_item, viewGroup, false);

		return new ViewHolder(v);
	}

	// replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.getTextView().setText(_data[position]);
	}

	// return the size of your dataset
	@Override
	public int getItemCount() {return _data.length;}

	private String [] _data;
	private static final String TAG = "CustomAdapter";
}
