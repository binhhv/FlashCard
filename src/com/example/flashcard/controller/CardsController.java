package com.example.flashcard.controller;


import com.example.flashcard.CardsActivity;
import com.example.flashcard.FillBlankCardActivity;
import com.example.flashcard.FindCardsActivity;
import com.example.flashcard.PuzzleActivity;
import com.example.flashcard.PuzzleImageActivity;
import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.model.utils.CardsModel;



public class CardsController {
	private static CardsController instance;
	public static 	CardsController getInstance(){
		if(instance == null)
			instance = new CardsController();
		return instance;
	}
	public void handleViewEvent(ActionEvent e, int typeLanguage, int move,String time){
		switch (e.action) {
		case Constants.PLAY_CARDS:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,0,null);
			break;
		case Constants.FIND_CARDS:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,0,null);
			break;
		case Constants.FILLBLANK_CARD:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,0,null);
			break;
		case Constants.PUZZLE_CARD:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,0,null);
			break;
		case Constants.GET_ITEM:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,0,null);
			break;
		case Constants.UPDATE_PUZZLE:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,move,null);
			break;
		case Constants.UPDATE_FILL:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,move,time);
			break;
		case Constants.GET_ITEM_FILL:
			CardsModel.getInstance().handleControllerEvent(e,typeLanguage,0,null);
			break;
		default:
			break;
		}
	}
	
	public void handleModelViewEvent(ActionEvent e, int typeLanguage){
		switch (e.action) {
		case Constants.PLAY_CARDS:
			CardsActivity sender = (CardsActivity)e.sender;
			sender.handleControllerViewEvent(e);
			break;
		case Constants.FIND_CARDS:
			FindCardsActivity senderFindCard = (FindCardsActivity)e.sender;
			senderFindCard.handleControllerViewEvent(e);
			break;
		case Constants.FILLBLANK_CARD:
			FillBlankCardActivity senderFillBlankCard = (FillBlankCardActivity)e.sender;
			senderFillBlankCard.handleControllerViewEvent(e);
			break;
		case Constants.PUZZLE_CARD:
			PuzzleActivity senderPuzzleCard = (PuzzleActivity)e.sender;
			senderPuzzleCard.handleControllerViewEvent(e);
			break;
		case Constants.GET_ITEM:
			PuzzleActivity senderPuzzleItem = (PuzzleActivity)e.sender;
			senderPuzzleItem.handleControllerViewEvent(e);
			break;
		case Constants.GET_ITEM_FILL:
			FillBlankCardActivity senderFillItem = (FillBlankCardActivity)e.sender;
			senderFillItem.handleControllerViewEvent(e);
			break;
		default:
			break;
		}
	}
}
