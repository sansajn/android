package com.example.hlavatovic.recyclerview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Photo implements Serializable {

	public Photo(JSONObject photoJSON) {
		try {
			_date = photoJSON.getString("date");
			_humanDate = convertDateToHumanDate();
			_explanation = photoJSON.getString("explanation");
			_url = photoJSON.getString("url").replace("http://", "https://");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getHumanDate() {return _humanDate;}
	public String getExplanation() {return _explanation;}
	public String getUrl() {return _url;}

	private String convertDateToHumanDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat humanDateFormat = new SimpleDateFormat("dd MMMM yyyy");
		try {
			Date date = dateFormat.parse(_date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return humanDateFormat.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String _date;
	private String _humanDate;
	private String _explanation;
	private String _url;
}
