package com.example.flashcard.model.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAccess extends SQLiteOpenHelper {

	private static String DB_PATH;
	private static String DB_NAME = "FlashCardDB.sqlite";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase myDatabase;
	private final Context myContext;

	public DBAccess(Context context) {

		super(context, DB_NAME, null, DATABASE_VERSION);
		myContext = context;
		DB_PATH = context.getDatabasePath(DB_NAME).toString();
		if (!checkDataBase()) {
			Log.e("Khong ton tai DB", "khong ton tain");
			try {

				File createOutFile = new File(DB_PATH);
				CreateDatabase();
				/*
				 * if(!createOutFile.exists()){ createOutFile.mkdir();
				 * CreateDatabase(); }
				 */
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void CreateDatabase() throws IOException {
		// boolean dbExist = checkDataBase();
		//
		// if (dbExist) {

		this.getReadableDatabase();

		try {

			copyDataBase();
			Log.e("da copy DB", "db da ton tain");
		} catch (IOException e) {

			throw new Error("Error copying database");

		}
		// }
	}

	private boolean checkDataBase() {
		try {
			String myPath = DB_PATH;
			File dbfile = new File(myPath);
			return dbfile.exists();

		} catch (SQLiteException e) {
			Log.e("Loi ", "DatabaseNotFound ");
		}
		return false;
	}

	private void copyDataBase() throws IOException {
		// fix
		// Open your local db as the input stream
		InputStream myinput = myContext.getResources().getAssets().open(DB_NAME);
		BufferedInputStream buffStream = new BufferedInputStream(myinput);
		//GZIPInputStream zis = new GZIPInputStream(buffStream);

		// Path to the just created empty dbn
		String outfilename = DB_PATH ;

		// Open the empty db as the output stream
		OutputStream myoutput = new FileOutputStream(outfilename);

		// transfer byte to inputfile to outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = buffStream.read(buffer)) > 0) {
			myoutput.write(buffer, 0, length);
		}

		// Close the streams
		myoutput.flush();
		myoutput.close();
		
		buffStream.close();
		myinput.close();
		// end fix

		// Open your local db as the input stream
//		InputStream myInput = myContext.getResources().getAssets()
//				.open(DB_NAME);
//
//		// Path to the just created empty db
//		String outFileName = DB_PATH;
//
//		// Open the empty db as the output stream
//		OutputStream myOutput = new FileOutputStream(outFileName);
//
//		// transfer bytes from the inputfile to the outputfile
//		byte[] buffer = new byte[1024];
//		int length;
//		while ((length = myInput.read(buffer)) > 0) {
//			myOutput.write(buffer, 0, length);
//		}
//		// Close the streams
//		myOutput.flush();
//		myOutput.close();
//		myInput.close();
//		Log.e("copy data base", "da copy");
	}

	// @SuppressLint("NewApi")
	public boolean openDataBase() {

		String myPath = DB_PATH;

		myDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		// myDatabase.enableWriteAheadLogging();
		if (myDatabase != null) {
			return true;
		}
		return false;
	}

	public Cursor Excutequery(String SQL) {
		Cursor c = null;
		myDatabase = this.getReadableDatabase();
		c = myDatabase.rawQuery(SQL, null);
		return c;
	}

	public int ExecuteNonQuery(String sql) {
		int i = 0;
		myDatabase = this.getWritableDatabase();
		myDatabase.rawQuery(sql, null);
		return 1;
	}

	public void ExcuteUpdate(String sql) {
		// TODO Auto-generated method stub
		int i = 0;
		myDatabase = this.getWritableDatabase();
		myDatabase.beginTransaction();
		// i = this.getWritableDatabase().update(DATABASE_TABLE, values, where,
		// null);
		myDatabase.execSQL(sql);
		myDatabase.setTransactionSuccessful();
		myDatabase.endTransaction();
		// return i;
	}

	public void ExcuteInsert(String sql, String DATABASE_TABLE,
			ContentValues values) {
		myDatabase = this.getWritableDatabase();
		myDatabase.beginTransaction();
		myDatabase.execSQL(sql);
		myDatabase.setTransactionSuccessful();
		myDatabase.endTransaction();
	}

	// public int ExcuteUpdate(String table, ContentValues values,String
	// where,String[] args){
	// int i=0;
	// SQLiteDatabase db = this.getWritableDatabase();
	// i=db.update(table, values, where, args);
	// return i;
	// }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
