package com.barefoot.crosstalk.models;

import static com.barefoot.crosstalk.utils.Utils.isNotNullAndEmpty;
import static com.barefoot.crosstalk.utils.Utils.setterNameFor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import com.barefoot.crosstalk.utils.LogUtil;

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

	public static PersistableObjectCursor.Factory getCursorFactory() {
		return new PersistableObjectCursor.Factory();
	}
	
	public static class PersistableObjectCursor extends SQLiteCursor {
		
		private final String LOG_TAG = LogUtil.logTagForMe();
		
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
		
		public PersistableObject getModelObject(PersistableObject subClassObject) {
			PersistableObject convertedObject = null;
			try {
				Class classDefinition = Class.forName(subClassObject.getClass().getName());
				convertedObject = (PersistableObject) classDefinition.newInstance();
				int modifiers;
				String fieldName;
				String fieldSetterName;
				Method currentMethod;
				for(Field eachField : classDefinition.getDeclaredFields()) {
					modifiers = eachField.getModifiers();
					fieldName = eachField.getName();
					System.out.println(fieldName);
					fieldSetterName = setterNameFor(fieldName);
					if(!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
						try {
							System.out.println("HERE!!!" + fieldSetterName);
							currentMethod = classDefinition.getMethod(fieldSetterName, new Class[] {eachField.getType()});
							System.out.println("CurrentMethod" + convertedObject);
							currentMethod.invoke(convertedObject, new Object[]{getDatabaseValue(eachField)});
						} catch (SecurityException e) {
							Log.e(LOG_TAG, e.getMessage());
						} catch (NoSuchMethodException e) {
							System.out.println("Error2");
							Log.e(LOG_TAG, e.getMessage());
						} catch (IllegalArgumentException e) {
							Log.e(LOG_TAG, e.getMessage());
						} catch (InvocationTargetException e) {
							Log.e(LOG_TAG, e.getMessage());
						}
					}
				}
			} catch (ClassNotFoundException e) {
				Log.e(LOG_TAG, e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(LOG_TAG, e.getMessage());
			} catch (InstantiationException e) {
				Log.e(LOG_TAG, e.getMessage());
			}
			
			return convertedObject;
		}
		
		private Object getDatabaseValue(Field field) {
			String fieldType = field.getType().getName();
			String fieldName = field.getName();
			int dbValueIndex = getColumnIndexOrThrow(fieldName.toLowerCase());
			System.out.println(dbValueIndex);
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
}
