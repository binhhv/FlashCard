package com.example.flashcard;

import java.util.ArrayList;

import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.controller.SettingController;
import com.example.flashcard.model.dto.DataLanguageDTO;
import com.example.flashcard.model.dto.SettingDTO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	private ImageButton btnBack;
	private ImageButton btnHome;
	private Spinner spinnerLanguage;
	private SeekBar seekBarVolume;
	private CheckBox cbSwipe;
	private ImageButton btnSaveSetting;
	private SettingDTO dto;
	private int typeLanguage;
	private int volume;
	private int swipe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		initData();
		btnBack =(ImageButton)findViewById(R.id.btnStart);
		btnHome =(ImageButton)findViewById(R.id.btnCategorySetting);
		spinnerLanguage =(Spinner)findViewById(R.id.spinnerLanguage);
		seekBarVolume =(SeekBar)findViewById(R.id.seekBarVolume);
		cbSwipe =(CheckBox)findViewById(R.id.cbSwipe);
		btnSaveSetting =(ImageButton)findViewById(R.id.btnSaveSetting);
		
		spinnerLanguage.setOnItemSelectedListener(this);
		btnBack.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		btnSaveSetting.setOnClickListener(this);
		
		showOptionSetting();
	}
	public void showOptionSetting(){
		spinnerLanguage.setSelection(dto.getLanguage()-1);
		seekBarVolume.setProgress(dto.getVolume());
		if(dto.getSwipe() == 0){
			cbSwipe.setChecked(false);
		}
		else cbSwipe.setChecked(true);
	}
	public void saveSetting(){
		typeLanguage = spinnerLanguage.getSelectedItemPosition() + 1;
		volume = seekBarVolume.getProgress();
		if(cbSwipe.isChecked()){
			swipe =1;
		}
		else swipe = 0;
		ActionEvent e = new ActionEvent(SettingActivity.this,
				Constants.SET_SETTING, null, null);
		SettingController.getInstance().handleViewEvent(e,typeLanguage,volume,swipe);
	}
	public void initData(){
		ActionEvent e = new ActionEvent(SettingActivity.this,
				Constants.GET_SETTING, null, null);
		SettingController.getInstance().handleViewEvent(e,0,0,0);
	}
	@SuppressWarnings("unchecked")
	public void handleControllerViewEvent(ActionEvent e) {
		switch (e.action) {
		case Constants.GET_SETTING:
			dto = (SettingDTO)e.viewData;
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnStart:
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			this.finish();
			break;
		case R.id.btnCategorySetting:
			Intent iCategory = new Intent(SettingActivity.this,CategoryActivity.class);
			startActivity(iCategory);
			this.finish();
			break;
		case R.id.btnSaveSetting:
			saveSetting();
			Intent returnIntentSave = new Intent();
			setResult(RESULT_OK, returnIntentSave);
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		int d = seekBarVolume.getProgress();
		//Toast.makeText(parent.getContext(),"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString() + d,Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		this.finish();
	}

}
