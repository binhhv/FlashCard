package com.example.flashcard.model.utils;

import android.content.Context;
import android.database.Cursor;

import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.SettingController;
import com.example.flashcard.model.dto.SettingDTO;

public class SettingModel {
	private static SettingModel instance;
	public static SettingModel getInstance(){
		if(instance == null)
			instance = new SettingModel();
		return instance;
	}
	public void handleControllerEvent(ActionEvent e, int typeLanguage, int volume,int swipe){
		switch (e.action) {
		case Constants.GET_SETTING:
			SettingDTO dto = getOptionSetting((Context)e.sender);
			e.viewData = dto;
			SettingController.getInstance().handleModelViewEvent(e);
			break;
		case Constants.GET_SETTING_PLAY:
			SettingDTO dtop = getOptionSetting((Context)e.sender);
			e.viewData = dtop;
			SettingController.getInstance().handleModelViewEvent(e);
			break;
		case Constants.GET_SETTING_FIND:
			SettingDTO dtof= getOptionSetting((Context)e.sender);
			e.viewData = dtof;
			SettingController.getInstance().handleModelViewEvent(e);
			break;
		case Constants.SET_SETTING:
			setOptionSetting((Context)e.sender, typeLanguage, volume, swipe);
			break;
		default:
			break;
		}
	}
	public SettingDTO getOptionSetting(Context context){
		SettingDTO dto = new SettingDTO();
		String sql = "SELECT* FROM Setting";
		DBAccess db = new DBAccess(context);
		Cursor c = db.Excutequery(sql);
		while (c.moveToNext()) {
			dto.setLanguage(c.getInt(c.getColumnIndex("language")));
			dto.setVolume(c.getInt(c.getColumnIndex("volume")));
			dto.setSwipe(c.getInt(c.getColumnIndex("swipe")));
		}
		c.close();
		db.close();
		return dto;
	}
	public void setOptionSetting(Context context, int type, int volume, int swipe){
		String sql = "update Setting set language ="+type+", volume ="+volume+",swipe ="+swipe+" where id = 1";
		DBAccess db = new DBAccess(context);
		db.ExcuteUpdate(sql);
	}
}
