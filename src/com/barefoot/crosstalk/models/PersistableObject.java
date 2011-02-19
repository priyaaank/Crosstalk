package com.barefoot.crosstalk.models;

import java.util.List;

public interface PersistableObject<T> {
	
	public String getTableName();
	
	public T findById(int id);
	
	public List<T> findAllFor(Criteria sqlCriteria);
	
	public boolean exists(Criteria sqlCriteria);
	
	public void create(T persistableObject);
	
	public void update(T persistableObject);
	
	public void delete(T persistableObject);
	
}
