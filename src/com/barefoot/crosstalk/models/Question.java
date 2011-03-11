package com.barefoot.crosstalk.models;

import android.content.Context;


public class Question extends PersistableObject {
	
	private final static String LOG_TAG = Question.class.getName();
	
	private long id;
	private String questionText;
	private String questionTitle;
	private String askedDate;

	private Context context;
	
	public Question() {
		//Do nothing
	}
	
	public Question(Context context) {
		this.context = context;
	}
	
	public Question(Context context, long id, String questionText, String questionTitle, String askedDate) {
		this.context = context;
		this.id = id;
		this.questionText = questionText;
		this.questionTitle = questionTitle;
		this.askedDate = askedDate;
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

	@Override
	public void create() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void delete() {
		
	}

	@Override
	protected Context getContext() {
		return this.context;
	}
}
