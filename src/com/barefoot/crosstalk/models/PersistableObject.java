package com.barefoot.crosstalk.models;

import static com.barefoot.crosstalk.utils.Utils.isNotNullAndEmpty;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public abstract class PersistableObject {
	
	public String getTableName() {
		return this.getClass().getName().toUpperCase();
	}
	
	public PersistableObject findById(int id) {
		Criteria<PersistableObject> searchCriteria = getCriteriaInstance().where("id", Integer.toString(id));
		List<PersistableObject> listOfObjects = findAllForGiven(searchCriteria);
		return isNotNullAndEmpty(listOfObjects) ? listOfObjects.get(0) : null; 
	}
	
	public abstract List<PersistableObject> findAllFor(Criteria<PersistableObject> sqlCriteria);
	
	public abstract boolean exists(Criteria<PersistableObject> sqlCriteria);
	
	public abstract void create(PersistableObject persistableObject);
	
	public abstract void update(PersistableObject persistableObject);
	
	public abstract void delete(PersistableObject persistableObject);
	
	public abstract Criteria<PersistableObject> getCriteriaInstance();
	
	protected List<PersistableObject> findAllForGiven(Criteria<PersistableObject> criteria) {
		
		return null;
	}

	private static class PersistableObjectCursor extends SQLiteCursor {
		
		public PersistableObjectCursor(SQLiteDatabase db, SQLiteCursorDriver driver, 
							  		   String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
		}
		
		private static class Factory implements SQLiteDatabase.CursorFactory {

			@Override
			public Cursor newCursor(SQLiteDatabase db,SQLiteCursorDriver masterQuery, 
									String editTable, SQLiteQuery query) {
				return new PersistableObjectCursor(db, masterQuery, editTable, query);
			}
			
		}
		
		protected Factory getCursorFactory() {
			return new PersistableObjectCursor.Factory();
		}
		
		public PersistableObject getModelObject() {
			return null;
		}
	}
	
}
