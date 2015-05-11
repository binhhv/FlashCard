package com.example.flashcard;

import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.SettingController;
import com.example.flashcard.model.dto.SettingDTO;
import com.example.flashcard.model.utils.DefinedGlobalVar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public  class MainActivity extends Activity {
	private ImageButton btnStart;
	private ImageButton btnSetting;
	private ImageButton btnSing;
	
	public static int TYPE;
	public static int VOLUME;
	public static int SWIPE;
	SettingDTO dto;
	DefinedGlobalVar df;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		df = new DefinedGlobalVar();
		
		//btnStart =(ImageButton)findViewById(R.id.btnStart);
		//btnSetting =(ImageButton)findViewById(R.id.btnCategorySetting);
		//btnSing  =(ImageButton)findViewById(R.id.btnSing);
		
		//btnStart.setOnClickListener(this);
		//btnSetting.setOnClickListener(this);
		//btnSing.setOnClickListener(this);	
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	Intent i = new Intent(MainActivity.this,CategoryPlayActivity.class);
		    	startActivity(i);
		    	finish();
		    }
		}, 1000);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		int id = v.getId();
//		switch (id) {
//		case R.id.btnStart:
//			//Intent iCategory = new Intent(MainActivity.this,CategoryActivity.class);
//			//startActivity(iCategory);
//			//this.finish();
//			
//			Intent iCategoryPlay = new Intent(MainActivity.this,CategoryPlayActivity.class);
//			startActivity(iCategoryPlay);
//			finish();
//			
//			break;
//		case R.id.btnCategorySetting:
//			Intent iSetting = new Intent(MainActivity.this,SettingActivity.class);
//			startActivity(iSetting);
//			//this.finish();
//			break;
//		case R.id.btnSing:
//			Intent iSingSong = new Intent(MainActivity.this,DrawingActivity.class);
//			startActivity(iSingSong);
//			break;
//		default:
//			break;
//		}
//	}

}
