package com.barefoot.crosstalk.models;

import java.util.List;

import com.barefoot.crosstalk.utils.LogUtil;


public class Question extends PersistableObject {
	
	private final static String LOG_TAG = LogUtil.logTagForMe();
	
	private long id;
	private String questionText;
	private String questionTitle;
	private String askedDate;
	
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
	public boolean exists(Criteria<PersistableObject> sqlCriteria) {
		return false;
	}

	@Override
	public void create(PersistableObject persistableObject) {
		
	}

	@Override
	public void update(PersistableObject persistableObject) {
		
	}

	@Override
	public void delete(PersistableObject persistableObject) {
		
	}
	
	@Override
	public List<PersistableObject> findAllFor(Criteria<PersistableObject> sqlCriteria) {
		return null;
	}

	@Override
	public Criteria<PersistableObject> getCriteriaInstance() {
		return null;
	}
}
