package com.barefoot.crosstalk.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Criteria<T extends PersistableObject> {
	
	private Map<String, String> whereOptions = new HashMap<String, String>();
	private List<String> fromTables = new ArrayList<String>();
	private List<String> selectFields = new ArrayList<String>();
	private List<String> ascendingFieldList = new ArrayList<String>();
	private List<String> descendingFieldList = new ArrayList<String>();
	
	public Criteria(T domainObject) {
		fromTables.add(domainObject.getTableName());
	}
	
	public Criteria<T> select(List<String> fields) {
		return this;
	}
		
	public Criteria<T> where(String key, String value) {
		return this;
	}
	
	public Criteria<T> where(String key, List<String> value) {
		return this;
	}
	
	public Criteria<T> orderByAscending() {
		return this;
	}
	
	public Criteria<T> orderByDescending() {
		return this;
	}
	
	public String selectionQuery() {
		return null;
	}
	
	public String countQuery() {
		return null;
	}
}
