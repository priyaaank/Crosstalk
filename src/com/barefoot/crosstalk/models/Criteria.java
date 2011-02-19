package com.barefoot.crosstalk.models;

import java.util.List;

public class Criteria {
	
	public Criteria() {
		
	}
	
	public Criteria select(List<String> fields) {
		
		return this;
	}
		
	public Criteria where(String key, String value) {
		
		return this;
	}
	
	public Criteria where(String key, List<String> value) {
		
		return this;
	}
	
	public Criteria orderByAscending() {
	
		return this;
	}
	
	public Criteria orderByDescending() {
		
		return this;
	}
	
	public String selectionClauseForQuery() {
		
		return null;
	}
}
