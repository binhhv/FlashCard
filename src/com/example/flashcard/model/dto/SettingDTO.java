package com.example.flashcard.model.dto;

import java.io.Serializable;

public class SettingDTO implements Serializable {
	private int id;
	private int language;
	private int volume;
	private int swipe;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getSwipe() {
		return swipe;
	}
	public void setSwipe(int swipe) {
		this.swipe = swipe;
	}
}
