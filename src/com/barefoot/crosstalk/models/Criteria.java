package com.barefoot.crosstalk.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Criteria<T extends PersistableObject> {
	
	private Map<String, List<String>> whereOptions = new HashMap<String, List<String>>();
	private List<String> fromTables = new ArrayList<String>();
	private List<String> selectFields = new ArrayList<String>();
	private List<String> ascendingFieldList = new ArrayList<String>();
	private List<String> descendingFieldList = new ArrayList<String>();
	
	public Criteria(T domainObject) {
		fromTables.add(domainObject.getTableName());
	}
	
	public Criteria<T> selectCount() {
		selectFields.add(" count(*) ");
		return this;
	}
	
	public Criteria<T> select(List<String> fields) {
		raiseErrorIfSelectFieldsAlreadySpecified();
		selectFields.addAll(fields);
		return this;
	}
	
	public Criteria<T> select(String...fields) {
		raiseErrorIfSelectFieldsAlreadySpecified();
		selectFields.addAll(Arrays.asList(fields));
		return this;
	}
	
	public Criteria<T> and(String key, String value) {
		where(key,value);
		return this;
	}
	
	public Criteria<T> and(String key, String...value) {
		where(key,value);
		return this;
	}
	
	public Criteria<T> and(String key, List<String> value) {
		where(key, value);
		return this;
	}
		
	public Criteria<T> where(String key, String value) {
		List<String> optionsList = new ArrayList<String>();
		optionsList.add(value);
		whereOptions.put(key, optionsList);
		return this;
	}
	
	public Criteria<T> where(String key, String...value) {
		List<String> optionsList = new ArrayList<String>(Arrays.asList(value));
		whereOptions.put(key, optionsList);
		return this;
	}
	
	public Criteria<T> where(String key, List<String> value) {
		return this;
	}
	
	public Criteria<T> orderByAscending(String...columnName) {
		ascendingFieldList.addAll(Arrays.asList(columnName));
		return this;
	}
	
	public Criteria<T> orderByDescending(String...columnName) {
		descendingFieldList.addAll(Arrays.asList(columnName));
		return this;
	}
	
	public String selectionQuery() {
		return null;
	}
	
	private void raiseErrorIfSelectFieldsAlreadySpecified() {
		if (selectFields.size() > 0) {
			throw new InvalidCriteriaConstructionException("Select fields have already been specified");
		}
	}

}

@SuppressWarnings("serial")
class InvalidCriteriaConstructionException extends RuntimeException {
	
	public InvalidCriteriaConstructionException() {
		super();
	}
	
	public InvalidCriteriaConstructionException(String message) {
		super(message);
	}
	
	public InvalidCriteriaConstructionException(Throwable throwable) {
		super(throwable);
	}
	
	public InvalidCriteriaConstructionException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
