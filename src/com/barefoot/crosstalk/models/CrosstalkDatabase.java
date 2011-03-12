package com.barefoot.crosstalk.models;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.barefoot.crosstalk.R;

public class CrosstalkDatabase extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "CROSSTALK";
	private final static int DATABASE_VERSION = 1;
	private final Context context;
	private final static String LOG_TAG = CrosstalkDatabase.class.getName();

	public CrosstalkDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	@Override
	public void finalize() {
		super.close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e(LOG_TAG, "Creating Crosstalk database for first time");
		String[] creationSqls = context.getString(R.string.create_database_sqls).split(";");
		db.beginTransaction();
		try {
			executeManySqlStatements(db, creationSqls);
			db.setTransactionSuccessful();
		} catch (SQLException sqle) {
			Log.e(LOG_TAG, "Exception occured, while creating database. The error is:" + sqle.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(LOG_TAG, "Upgrading database from version " + oldVersion + " to "+ newVersion);
		onCreate(db);
	}
	
	private void executeManySqlStatements(SQLiteDatabase db, String[] sqlStaements) {
		for (String eachSqlStatement : sqlStaements) {
			if(eachSqlStatement.trim().length() > 0) {
				db.execSQL(eachSqlStatement.toLowerCase());
			}
		}
	}
}