package com.example.flashcard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.flashcard.model.utils.DefinedGlobalVar;

public class CategoryPlayActivity extends Activity implements OnClickListener {

	private ImageButton btnBackCategoryPlay;
	private ImageButton btnPlay1;
	private ImageButton btnPlay2;
	private ImageButton btnPlay3;
	private ImageButton btnPlay4;
	private ImageButton btnPlay5;
	private Button btnSetting;
	private LinearLayout layout_bt_home;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_play);
		

		btnPlay1 = (ImageButton)findViewById(R.id.btnPlay1);
		btnPlay2 = (ImageButton)findViewById(R.id.btnPlay2);
		btnPlay3 = (ImageButton)findViewById(R.id.btnPlay3);
		btnPlay4 = (ImageButton)findViewById(R.id.btnPlay4);
		btnPlay5 =(ImageButton)findViewById(R.id.btnPlay5);
		layout_bt_home = (LinearLayout)findViewById(R.id.layout_bt_home);
		btnSetting =(Button)findViewById(R.id.btnSetting);
		
	
		btnPlay1.setOnClickListener(this);
		btnPlay2.setOnClickListener(this);
		btnPlay3.setOnClickListener(this);
		btnPlay4.setOnClickListener(this);
		btnPlay5.setOnClickListener(this);
		btnSetting.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_play, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//layout_bt_home.setBackgroundResource(R.drawable.button_bg_click);
		int id = v.getId();
		switch(id){
		case R.id.btnPlay1:
			btnPlay1.setBackgroundResource(R.drawable.button_home_click_01);
			Intent iCategory1 = new Intent(CategoryPlayActivity.this,CardsActivity.class);
			startActivityForResult(iCategory1,1);
			break;
		case R.id.btnPlay2:
			btnPlay2.setBackgroundResource(R.drawable.button_home_click_02);
			Intent iCategory2 = new Intent(CategoryPlayActivity.this,FindCardsActivity.class);
			startActivityForResult(iCategory2,1);
			break;
		case R.id.btnPlay3:
			btnPlay3.setBackgroundResource(R.drawable.button_home_click_03);
			Intent  iCategory3= new Intent(CategoryPlayActivity.this,FillBlankCardActivity.class);
			startActivityForResult(iCategory3,1);
			break;
		case R.id.btnPlay4:
			btnPlay4.setBackgroundResource(R.drawable.button_home_click_04);
			Intent iCategory4  = new Intent(CategoryPlayActivity.this,PuzzleActivity.class);
			startActivityForResult(iCategory4,1);
			break;
		case R.id.btnPlay5:
			btnPlay5.setBackgroundResource(R.drawable.button_home_click_05);
			Intent iDrawing = new Intent (CategoryPlayActivity.this,DrawingActivity.class);
			startActivityForResult(iDrawing,1);
			break;
		case R.id.btnSetting:
			btnSetting.setBackgroundResource(R.drawable.button_home_click_06);
			Intent iSetting = new Intent(CategoryPlayActivity.this,SettingActivity.class);
			startActivityForResult(iSetting,1);
			break;
			default:
				
		break;
		}
	}
	public void setBackGroundButtonBack(){
		//layout_bt_home.setBackgroundResource(R.drawable.button_bg);
		btnPlay1.setBackgroundResource(R.drawable.button_home_01);
		btnPlay2.setBackgroundResource(R.drawable.button_home_02);
		btnPlay3.setBackgroundResource(R.drawable.button_home_03);
		btnPlay4.setBackgroundResource(R.drawable.button_home_04);
		btnPlay5.setBackgroundResource(R.drawable.button_home_05);
		btnSetting.setBackgroundResource(R.drawable.button_home_06);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode ==1){
			if(resultCode == RESULT_OK){
				setBackGroundButtonBack();
			}
		}
		//uper.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
