package com.barefoot.crosstalk.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.barefoot.crosstalk.R;
import com.barefoot.crosstalk.models.Question;
import com.barefoot.crosstalk.utils.LogUtil;

public class AskQuestion extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		setContentView(R.layout.ask_question);
		setComponentListeners();
		
        Question question = (Question) new Question(this).findById(1);
        Log.i(LogUtil.logTagForMe(), "Found question is!!!! :  " + question);

	}
	
	private void setComponentListeners() {
		findViewById(R.id.ask_question_submit_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

}
