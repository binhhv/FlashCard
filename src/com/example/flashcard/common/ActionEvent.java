package com.example.flashcard.common;

import android.os.Bundle;

public class ActionEvent {
	public Object sender;
	public int action;
	public Object viewData;
	public Bundle bundle;
	public int mytype;
	
	public ActionEvent(Object sender, int action, Object viewData,Bundle bundle){
		super();
		this.sender = sender;
		this.action = action;
		this.viewData = viewData;
		this.bundle = bundle;
	}
}
