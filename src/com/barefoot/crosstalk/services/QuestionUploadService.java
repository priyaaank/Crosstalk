package com.barefoot.crosstalk.services;

import java.util.List;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.barefoot.crosstalk.components.persistence.PersistableObject;
import com.barefoot.crosstalk.models.Question;

public class QuestionUploadService extends WakeEventService {

	private static final String LOG_TAG = "QuestionUploadService";

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }
	
	@Override
	public void doServiceTask() {
		try {
			List<PersistableObject> allQuestions = new Question(this).findAll();
			if(allQuestions != null && allQuestions.size() > 0) {
				for(PersistableObject eachQuestion : allQuestions) {
					((Question)eachQuestion).submitToService();
				}
			}
		} catch(Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		} finally {
			this.stopSelf();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
