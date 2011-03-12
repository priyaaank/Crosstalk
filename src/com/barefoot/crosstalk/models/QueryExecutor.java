package com.barefoot.crosstalk.models;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class QueryExecutor {
	
	private static String LOG_TAG = QueryExecutor.class.getName();
	private Context context;
	private SQLiteOpenHelper database;
	
	public QueryExecutor(Context context) {
		this.context = context; 
	}
	
	abstract public void databaseOperation();
	
	public SQLiteOpenHelper getDatabase() {
		return this.database;
	}
	
	private void openDatabase() {
		this.database = new CrosstalkDatabase(context);
	}
	
	private void closeDatabase() {
		try {
			if(database != null)
				database.close();
		} catch(Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
	}
	
	public void execute() {
		openDatabase();
		try {
			databaseOperation();
		} catch(Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			
		}
		closeDatabase();
	}	
}