package com.example.hlavatovic.recyclerview;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageRequester {

	public interface ImageRequesterResponse {
		void receivedNewPhoto(Photo newPhoto);
	}

	public boolean isLoadingData() {
		return _loadingData;
	}

	public ImageRequester(Activity listeningActivity) {
		_calendar = Calendar.getInstance();
		_dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		_responseListener = (ImageRequesterResponse) listeningActivity;
		_context = listeningActivity.getApplicationContext();
		_client = new OkHttpClient();
		_loadingData = false;
	}

	public void getPhoto() throws IOException {

		String date = _dateFormat.format(_calendar.getTime());

		String urlRequest = BASE_URL + DATE_PARAMETER + date + API_KEY_PARAMETER +
			_context.getString(R.string.api_key);
		Request request = new Request.Builder().url(urlRequest).build();
		_loadingData = true;

		_client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				_loadingData = false;
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				try {
					JSONObject photoJSON = new JSONObject(response.body().string());

					_calendar.add(Calendar.DAY_OF_YEAR, -1);

					if (!photoJSON.getString(MEDIA_TYPE_KEY).equals(MEDIA_TYPE_VIDEO_VALUE)) {
						Photo receivedPhoto = new Photo(photoJSON);
						_responseListener.receivedNewPhoto(receivedPhoto);
						_loadingData = false;
					} else {
						getPhoto();
					}
				} catch (JSONException e) {
					_loadingData = false;
					e.printStackTrace();
				}
			}
		});
	}

	private Calendar _calendar;
	private SimpleDateFormat _dateFormat;
	private ImageRequesterResponse _responseListener;
	private Context _context;
	private OkHttpClient _client;
	private boolean _loadingData;
	private static final String BASE_URL = "https://api.nasa.gov/planetary/apod?";
	private static final String DATE_PARAMETER = "date=";
	private static final String API_KEY_PARAMETER = "&api_key=";
	private static final String MEDIA_TYPE_KEY = "media_type";
	private static final String MEDIA_TYPE_VIDEO_VALUE = "video";
}
