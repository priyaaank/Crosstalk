package com.barefoot.crosstalk.models;

import static com.barefoot.crosstalk.utils.Utils.basicPluralize;
import static com.barefoot.crosstalk.utils.Utils.camelCaseToSnakeCase;
import static com.barefoot.crosstalk.utils.Utils.isNotNullAndEmpty;
import static com.barefoot.crosstalk.utils.Utils.setterNameFor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public abstract class PersistableObject {
	
	private final String LOG_TAG = PersistableObject.class.getName();
	
	public String getTableName() {
		return basicPluralize(this.getClass().getSimpleName().toLowerCase());
	}
	
	public PersistableObject findById(int id) {
		Criteria<PersistableObject> searchCriteria = getCriteriaInstance().where("id", Integer.toString(id));
		List<PersistableObject> listOfObjects = findAllForGiven(searchCriteria);
		return isNotNullAndEmpty(listOfObjects) ? listOfObjects.get(0) : null; 
	}
	
	public boolean exists(Criteria<PersistableObject> sqlCriteria) {
		return false;
	}
	
	public long create() {
		return 0;
	}
	
	public long update() {
		return 0;
	}
	
	public abstract void delete();
	
	protected Criteria<PersistableObject> getCriteriaInstance() {
		return new Criteria<PersistableObject>(this);
	}
	
	protected List<PersistableObject> findAllForGiven(final Criteria<PersistableObject> criteria) {
		final List<PersistableObject> objectList = new ArrayList<PersistableObject>();
		
		(new QueryExecutor(getContext()) {
			@Override
			public void databaseOperation() {
				Cursor persistableObjectReview = null;
				
				try {
					persistableObjectReview = getDatabase().getReadableDatabase().rawQueryWithFactory(getCursorFactory(), 
																									  criteria.escapedSelectionQuery(), 
																									  criteria.selectionQueryParamsArray(), 
																									  null);
					if(persistableObjectReview != null && persistableObjectReview.moveToFirst()) {
						do {
							objectList.add(((PersistableObjectCursor)persistableObjectReview).getModelObject(PersistableObject.this, getContext(), getFieldNamesForObject()));
						} while(persistableObjectReview.moveToNext());
					}
				} catch(SQLException sqle) {
					Log.e(LOG_TAG, "SQL exception thrown:" + sqle.getMessage());
				} finally {
					if(persistableObjectReview != null && !persistableObjectReview.isClosed()) {
						persistableObjectReview.close();
					}
				}
			}
		}).execute();
		
		return objectList;
	}

	public static PersistableObjectCursor.Factory getCursorFactory() {
		return new PersistableObjectCursor.Factory();
	}
	
	public static class PersistableObjectCursor extends SQLiteCursor {
		
		private final String LOG_TAG = PersistableObjectCursor.class.getName();
		
		public PersistableObjectCursor(SQLiteDatabase db, SQLiteCursorDriver driver, 
							  		   String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
		}
		
		public static class Factory implements SQLiteDatabase.CursorFactory {

			@Override
			public Cursor newCursor(SQLiteDatabase db,SQLiteCursorDriver masterQuery, 
									String editTable, SQLiteQuery query) {
				return new PersistableObjectCursor(db, masterQuery, editTable, query);
			}
		}
		
		public PersistableObject getModelObject(PersistableObject subClassObject, Context context, List<String> fieldNames) {
			PersistableObject convertedObject = null;
			try {
				Class<?> classDefinition = Class.forName(subClassObject.getClass().getName());
				
				Object[] constructorArgs = new Object[] { context };
				Class<?>[] argsClass = new Class[] {Context.class};
				convertedObject = (PersistableObject) classDefinition.getConstructor(argsClass).newInstance(constructorArgs);
				int modifiers;
				String fieldName;
				String fieldSetterName;
				Method currentMethod;
				for(Field eachField : classDefinition.getDeclaredFields()) {
					
					if (!(fieldNames.contains(camelCaseToSnakeCase(eachField.getName()))))
						continue;
					
					modifiers = eachField.getModifiers();
					fieldName = eachField.getName();
					fieldSetterName = setterNameFor(fieldName);
					if(!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
						try {
							currentMethod = classDefinition.getMethod(fieldSetterName, new Class[] {eachField.getType()});
							currentMethod.invoke(convertedObject, new Object[]{getDatabaseValue(eachField)});
						} catch (Exception e) {
							Log.e(LOG_TAG, e.getMessage());
						}
					}
				}
			} catch (Exception e) {
				Log.e(LOG_TAG, e.getMessage());
			} 
			return convertedObject;
		}
		
		private Object getDatabaseValue(Field field) {
			String fieldType = field.getType().getName();
			String fieldName = field.getName();
			int dbValueIndex = getColumnIndexOrThrow(camelCaseToSnakeCase(fieldName));
			if("java.lang.String".equals(fieldType)) {
				return getString(dbValueIndex);
			} else if("int".equals(fieldType)) {
				return getInt(dbValueIndex);
			} else if("long".equals(fieldType)) {
				return getLong(dbValueIndex);				
			} else if("short".equals(fieldType)) {
				return getShort(dbValueIndex);
			} else if("float".equals(fieldType)) {
				return getDouble(dbValueIndex);
			} else if("double".equals(fieldType)) {
				return getDouble(dbValueIndex);
			}

			return null;
		}
	}
	
	protected List<String> getFieldNamesForObject() {
		final List<String> columnNames = new ArrayList<String>();
		(new QueryExecutor(getContext()) {
			@Override
			public void databaseOperation() {
				Cursor query = null;
				try {
					query = getDatabase().getReadableDatabase().query(getTableName(), null,"1=1 limit 1", null,null, null, null);
					String[] columnNamesArray = query.getColumnNames();
					columnNames.addAll(Arrays.asList(columnNamesArray));	
				} catch(SQLException sqle) {
					Log.e(LOG_TAG, sqle.getMessage());
				} finally {
					if(query != null && !query.isClosed()) {
						query.close();
					}
				}
			}
		}).execute();
		
		
		return columnNames;
	}
	
	abstract protected Context getContext();

}
