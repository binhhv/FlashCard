package com.example.flashcard.controller;

import com.example.flashcard.CardsActivity;
import com.example.flashcard.FindCardsActivity;
import com.example.flashcard.MainActivity;
import com.example.flashcard.SettingActivity;
import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.model.utils.SettingModel;

public class SettingController {
	private static SettingController instance;
	public static 	SettingController getInstance(){
		if(instance == null)
			instance = new SettingController();
		return instance;
	}
	public void handleViewEvent(ActionEvent e, int typeLanguage, int volume,int swipe){
		switch (e.action) {
		case Constants.GET_SETTING:
			SettingModel.getInstance().handleControllerEvent(e, typeLanguage, volume, swipe);
			break;
		case Constants.GET_SETTING_PLAY:
			SettingModel.getInstance().handleControllerEvent(e, typeLanguage, volume, swipe);
			break;
		case Constants.GET_SETTING_FIND:
			SettingModel.getInstance().handleControllerEvent(e, typeLanguage, volume, swipe);
			break;
		case Constants.SET_SETTING:
			SettingModel.getInstance().handleControllerEvent(e, typeLanguage, volume, swipe);
			break;
		default:
			break;
		}
	}
	public void handleModelViewEvent(ActionEvent e){
		switch (e.action) {
		case Constants.GET_SETTING:
			SettingActivity sender = (SettingActivity)e.sender;
			sender.handleControllerViewEvent(e);
			break;
		case Constants.GET_SETTING_PLAY:
			CardsActivity senderCards = (CardsActivity)e.sender;
			senderCards.handleControllerViewEvent(e);
			break;
		case Constants.GET_SETTING_FIND:
			FindCardsActivity senderFind = (FindCardsActivity)e.sender;
			senderFind.handleControllerViewEvent(e);
			break;
		default:
			
			break;
		}
	}
}
