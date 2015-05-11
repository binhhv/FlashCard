package com.example.flashcard.model.dto;

import java.io.Serializable;

public class ImageLanguageDTO implements Serializable {
	private int id;
	private String imageName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
}
