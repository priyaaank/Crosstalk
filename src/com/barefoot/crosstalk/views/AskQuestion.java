package com.barefoot.crosstalk.views;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.barefoot.crosstalk.R;

public class AskQuestion extends Activity {
	
	protected static final int TAKE_PICTURE = 1;
	public static String LOG_TAG = AskQuestion.class.getName();
	private Uri imageUri;
	
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
		
		findViewById(R.id.ask_question_map).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uri = "geo:"+ 0 + "," + 0 + "?q=kunal+icon+pimple+saudagar+pune";
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			}
		});
		
		
		findViewById(R.id.ask_question_take_picture).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
			    imageUri = Uri.fromFile(photo);
			    startActivityForResult(intent, TAKE_PICTURE);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	            Uri selectedImage = imageUri;
	            getContentResolver().notifyChange(selectedImage, null);
	            ImageView imageView = (ImageView) findViewById(R.id.image_view);
	            ContentResolver cr = getContentResolver();
	            Bitmap bitmap;
	            try {
	                 bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

	                imageView.setImageBitmap(bitmap);
	                Toast.makeText(this, selectedImage.toString(),
	                        Toast.LENGTH_LONG).show();
	            } catch (Exception e) {
	                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
	                        .show();
	                Log.e("Camera", e.toString());
	            }
	        }
	    }
	}

}
