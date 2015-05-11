package com.example.flashcard.model.dto;

import java.io.Serializable;

import android.media.MediaPlayer;
import android.media.SoundPool;

public class DataLanguageDTO implements Serializable  {
	private int id;
	private String English;
	private String Japanese;
	private String Chinese;
	private String imageName;
	private int category;
	private String soundE;
	private int idImage;
	private int idSoundE;
	private int idSoundPool;
	private int checkfinishedpuzzle;
	private int movePuzzle;
	private int checkFinishedFill;
	private String timeFill;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEnglish() {
		return English;
	}
	public void setEnglish(String english) {
		English = english;
	}
	public String getJapanese() {
		return Japanese;
	}
	public void setJapanese(String japanese) {
		Japanese = japanese;
	}
	public String getChinese() {
		return Chinese;
	}
	public void setChinese(String chinese) {
		Chinese = chinese;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getIdImage() {
		return idImage;
	}
	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}
	public String getSoundE() {
		return soundE;
	}
	public void setSoundE(String soundE) {
		this.soundE = soundE;
	}
	public int getIdSoundE() {
		return idSoundE;
	}
	public void setIdSoundE(int idSoundE) {
		this.idSoundE = idSoundE;
	}
	public int getIdSoundPool() {
		return idSoundPool;
	}
	public void setIdSoundPool(int idSoundPool) {
		this.idSoundPool = idSoundPool;
	}
	public int getCheckfinishedpuzzle() {
		return checkfinishedpuzzle;
	}
	public void setCheckfinishedpuzzle(int checkfinishedpuzzle) {
		this.checkfinishedpuzzle = checkfinishedpuzzle;
	}
	public int getMovePuzzle() {
		return movePuzzle;
	}
	public void setMovePuzzle(int movePuzzle) {
		this.movePuzzle = movePuzzle;
	}
	public String getTimeFill() {
		return timeFill;
	}
	public void setTimeFill(String timeFill) {
		this.timeFill = timeFill;
	}
	public int getCheckFinishedFill() {
		return checkFinishedFill;
	}
	public void setCheckFinishedFill(int checkFinishedFill) {
		this.checkFinishedFill = checkFinishedFill;
	}


	
}
