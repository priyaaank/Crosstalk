package com.barefoot.crosstalk.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.barefoot.crosstalk.R;

public class AskQuestion extends Activity {
	
	public static String LOG_TAG = AskQuestion.class.getName();
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		setContentView(R.layout.ask_question);
		setComponentListeners();

	}
	
	private void setComponentListeners() {
		findViewById(R.id.ask_question_submit_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

}
