package com.example.flashcard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SingSongActivity extends Activity implements OnClickListener {
	private ImageButton btnHomeSing;
	private ImageButton btnCategorySing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sing_song);
		
		btnHomeSing =(ImageButton)findViewById(R.id.btnHomeSing);
		btnCategorySing =(ImageButton)findViewById(R.id.btnCategorySing);
		
		btnHomeSing.setOnClickListener(this);
		btnCategorySing.setOnClickListener(this);
		int a;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sing_song, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.btnHomeSing:
			this.finish();
			break;
		case R.id.btnCategorySing:
			Intent iCategory = new Intent(SingSongActivity.this,CategoryActivity.class);
			startActivity(iCategory);
			this.finish();
			break;
		default:
			
			break;
		}
	}

}
