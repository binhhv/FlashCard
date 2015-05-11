package com.example.flashcard.model.utils;

import java.util.ArrayList;

import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.model.dto.DataLanguageDTO;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;




public class CardsModel {
	private static CardsModel instance;
	public static CardsModel getInstance(){
		if(instance == null)
			instance = new CardsModel();
		return instance;
	}
	public void handleControllerEvent(ActionEvent e, int typeLanguage, int move,String time){
		switch (e.action) {
		case Constants.PLAY_CARDS:
			ArrayList<DataLanguageDTO> newArr = getCardCategory((Context) e.sender,1);
			e.viewData = newArr;
			CardsController.getInstance().handleModelViewEvent(e,1);
			break;
		case Constants.FIND_CARDS:
			ArrayList<DataLanguageDTO> newArrFind = getCardCategory((Context) e.sender,1);
			e.viewData = newArrFind;
			CardsController.getInstance().handleModelViewEvent(e,1);
			break;
		case Constants.FILLBLANK_CARD:
			ArrayList<DataLanguageDTO> newArrFill = getCardCategory((Context) e.sender,1);
			e.viewData = newArrFill;
			CardsController.getInstance().handleModelViewEvent(e,1);
			break;
		case Constants.PUZZLE_CARD:
			ArrayList<DataLanguageDTO> newArrPuzzle = getCardCategory((Context) e.sender,1);
			e.viewData = newArrPuzzle;
			CardsController.getInstance().handleModelViewEvent(e,1);
			break;
		case Constants.GET_ITEM:
			ArrayList<DataLanguageDTO> newArrItem = getCardCategory((Context) e.sender,1);
			e.viewData = newArrItem;
			CardsController.getInstance().handleModelViewEvent(e,1);
			break;
		case Constants.GET_ITEM_FILL:
			ArrayList<DataLanguageDTO> newArrItemFill = getCardCategory((Context) e.sender,1);
			e.viewData = newArrItemFill;
			CardsController.getInstance().handleModelViewEvent(e,1);
			break;
		case Constants.UPDATE_PUZZLE:
			updateImagePuzzle((Context)e.sender, typeLanguage, move);
			break;
		case Constants.UPDATE_FILL:
			updateImageFillBlank((Context)e.sender, typeLanguage, time);
			break;
		default:
			break;
		}
	}
	public ArrayList<DataLanguageDTO> getCardCategory(Context context,int typeLanguage){
		ArrayList<DataLanguageDTO> listCards = new ArrayList<DataLanguageDTO>();
		String sql = "SELECT* FROM DataLanguage";
		DBAccess db = new DBAccess(context);
		Cursor c = db.Excutequery(sql);
		while (c.moveToNext()) {
			DataLanguageDTO dto = new DataLanguageDTO ();
			dto.setId(c.getInt(c.getColumnIndex("id")));
			if(typeLanguage == 1){
				dto.setEnglish(c.getString(c.getColumnIndex("English")));
				dto.setSoundE(c.getString(c.getColumnIndex("soundE")));
				dto.setIdSoundE(context.getResources().getIdentifier(c.getString(c.getColumnIndex("soundE")) , "raw", context.getPackageName()));
			}
			else if(typeLanguage ==2){
				dto.setEnglish(c.getString(c.getColumnIndex("Vietnamese")));
				dto.setSoundE(c.getString(c.getColumnIndex("soundV")));
				dto.setIdSoundE(context.getResources().getIdentifier(c.getString(c.getColumnIndex("soundV")) , "raw", context.getPackageName()));
			}
			else {
				dto.setEnglish(c.getString(c.getColumnIndex("Japanese")));
				dto.setSoundE(c.getString(c.getColumnIndex("soundJ")));
				dto.setIdSoundE(context.getResources().getIdentifier(c.getString(c.getColumnIndex("soundJ")) , "raw", context.getPackageName()));
			}
			dto.setImageName(c.getString(c.getColumnIndex("imageName")));
			dto.setCategory(c.getInt(c.getColumnIndex("category")));
			dto.setCheckfinishedpuzzle(c.getInt(c.getColumnIndex("finishedpuzzle")));
			dto.setCheckFinishedFill(c.getInt(c.getColumnIndex("finishedfill")));
			dto.setMovePuzzle(c.getInt(c.getColumnIndex("movepuzzle")));
			dto.setTimeFill(c.getString(c.getColumnIndex("timefill")));
			dto.setIdImage(context.getResources().getIdentifier(c.getString(c.getColumnIndex("imageName")) , "drawable", context.getPackageName()));
			listCards.add(dto);
		}
		c.close();
		db.close();
		
		return listCards;
	}
	public void updateImagePuzzle(Context context, int id, int move){
		String sql = "update DataLanguage set finishedpuzzle = 1,movepuzzle = " + move + " where id ="+id+"";
		DBAccess db = new DBAccess(context);
		db.ExcuteUpdate(sql);
	}
	public void updateImageFillBlank(Context context, int id, String time){
		String sql = "update DataLanguage set finishedfill = 1,timefill = " +""+time+""+" where id ="+id+"";
		DBAccess db = new DBAccess(context);
		db.ExcuteUpdate(sql);
	}
}
