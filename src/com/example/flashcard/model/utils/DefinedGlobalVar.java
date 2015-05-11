package com.example.flashcard.model.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;

import com.example.flashcard.MainActivity;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.model.dto.SettingDTO;

public class DefinedGlobalVar {
	public static final int VALUE_PLAY_CATEGORY1 = 1001;
	public static final int VALUE_PLAY_CATEGORY2 = 1002;
	public static final int VALUE_PLAY_CATEGORY3 = 1003;
	public static final int VALUE_PLAY_CATEGORY4 = 1004;
	
	public static final String PLAY_CATEGORY ="playCategory";

}
