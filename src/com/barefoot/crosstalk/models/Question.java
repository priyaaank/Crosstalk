package com.barefoot.crosstalk.models;


import android.content.Context;
import android.text.format.Time;

import com.barefoot.crosstalk.R;
import com.barefoot.crosstalk.components.persistence.PersistableObject;
import com.barefoot.crosstalk.components.webserviceops.RemoteServiceWrapper;


public class Question extends PersistableObject {
	
	private long id;
	private String questionText;
	private String questionTitle;
	private String askedDate;
	private String latitude;
	private String longitude;
	private Context context;
	private RemoteServiceWrapper remoteServiceWrapper;
	
	private static String localtimezone = new Time().timezone;
	
	public Question(Context context) {
		this.context = context;
		this.remoteServiceWrapper = new RemoteServiceWrapper(context.getString(R.string.base_remote_url));
	}
	
	public Question(Context context, String questionText, String questionTitle, 
					String askedDate, String latitude, String longitude) {
		this.context = context;
		this.questionText = questionText;
		this.questionTitle = questionTitle;
		this.askedDate = askedDate;
		this.longitude = longitude;
		this.latitude = latitude;
		this.remoteServiceWrapper = new RemoteServiceWrapper(context.getString(R.string.base_remote_url));
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getAskedDate() {
		return askedDate;
	}

	public void setAskedDate(String askedDate) {
		this.askedDate = askedDate;
	}	
	
	public String getAskedDateInLocalTimezone() {
		if(getAskedDate() != null) {
			Time newTime = new Time("UTC");
			newTime.switchTimezone(localtimezone);
			newTime.parse3339(getAskedDate());
			return  newTime.format("%d-%b-%Y");
		}
		
		return "";
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	protected Context getContext() {
		return this.context;
	}

	@Override
	protected void setPrimaryKeyColumn(long id) {
		this.id = id;
	}

	@Override
	protected String getNullableFieldName() {
		return "questionText";
	}

	@Override
	protected String getPrimaryKeyFieldName() {
		return "id";
	}
	
	public void submitToService() {
		remoteServiceWrapper.postToRemoteService(context.getString(R.string.question_submit_url), getAttributeMap());
	}
}
