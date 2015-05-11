package com.example.flashcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.flashcard.adapter.PuzzleListAdapter;
import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.model.dto.DataLanguageDTO;
import com.example.flashcard.model.utils.*;
import com.example.flashcard.model.utils.PuzzleView.ShowNumbers;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PuzzleActivity extends Activity implements
		android.view.View.OnClickListener {

	protected static final int MENU_SCRAMBLE = 0;
	protected static final int MENU_NEW_IMAGE = 1;
	protected static final int MENU_SELECT_IMAGE = 2;
	protected static final int MENU_TAKE_PHOTO = 3;
	protected static final int MENU_SHOW_NUMBERS = 4;
	protected static final int MENU_CHANGE_TILING = 5;
	protected static final int MENU_STATS = 6;
	protected static final int MENU_ABOUT = 7;

	protected static final int RESULT_SELECT_IMAGE = 0;
	protected static final int RESULT_TAKE_PHOTO = 1;

	protected static final int MIN_WIDTH = 2;
	protected static final int MAX_WIDTH = 6;

	protected static final String FILENAME_DIR = ".ntilepuzzle";
	protected static final String FILENAME_PHOTO_DIR = FILENAME_DIR + "/photo";
	protected static final String FILENAME_PHOTO = "photo.jpg";
	protected static final String FILENAME_IMAGE_PREFIX = "image_";
	protected static final String FILENAME_DISABLE_INTERNAL_IMAGES = ".disable_internal_images";
	protected String[] LABELS_SHOW_NUMBERS_ITEMS;
	String PATH;
	private Uri imageUri;
	private MenuItem menuShowNumbers;

	protected static final String KEY_SHOW_NUMBERS = "showNumbers";
	protected static final String KEY_IMAGE_URI = "imageUri";
	protected static final String KEY_PUZZLE = "puzzle";
	protected static final String KEY_PUZZLE_SIZE = "puzzleSize";
	protected static final String KEY_MOVE_COUNT = "moveCount";

	private ArrayList<DataLanguageDTO> listData;
	private int curIndex;
	private long startTime = 0L;
	private Handler handleClock = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;

	private ImageButton btnBackPuzzle;
	private ImageButton btnListImagePuzzle;
	private TextView textClock;
	// private TextView textMove;
	private ImageView imagePuzzle;
	private FrameLayout framePuzzle;

	// puzzle
	protected static final int DEFAULT_SIZE = 3;
	protected static final String SUFFIX_JPG = ".jpg";
	protected static final String SUFFIX_JPEG = ".jpeg";
	protected static final String SUFFIX_PNG = ".png";
	protected static final String KEY_MOVE_BEST_PREFIX = "moveBest_";
	protected static final String KEY_MOVE_AVERAGE_PREFIX = "moveAvg_";
	protected static final String KEY_PLAYS_PREFIX = "plays_";

	private PuzzleView view;
	private Puzzle puzzle;
	private int puzzleWidth = 1;
	private int puzzleHeight = 1;
	private boolean portrait;
	private boolean expert;
	private Options bitmapOptions;

	// dialog finis
	private Dialog myDialog;
	private ImageButton btnNextPuzzle;
	private ImageButton btnPlayAgain;
	private TextView textmove;
	int type = 1;
	PuzzleListAdapter adapter;
	ArrayList<Object> arrData;
	ProgressDialog pDialog;
	private AlertDialog myalertDialog=null;
	private Dialog customDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);

		listData = new ArrayList<DataLanguageDTO>();

		btnBackPuzzle = (ImageButton) findViewById(R.id.btnBackPuzzle);
		btnListImagePuzzle = (ImageButton) findViewById(R.id.btnListImagePuzzle);
		imagePuzzle = (ImageView) findViewById(R.id.imagePuzzle);
		framePuzzle = (FrameLayout) findViewById(R.id.framePuzzle);

		btnBackPuzzle.setOnClickListener(this);
		btnListImagePuzzle.setOnClickListener(this);
		

		InitData(type);
		startPuzzle(curIndex);
		customDialog = new Dialog(PuzzleActivity.this);
	}

	public void InitData(int type) {
		ActionEvent e = new ActionEvent(PuzzleActivity.this,
				Constants.PUZZLE_CARD, null, null);
		CardsController.getInstance().handleViewEvent(e, type,0,null);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//btnListImagePuzzle.setBackgroundResource(R.drawable.menu);
		
	   Intent returnIntent = new Intent();
	   setResult(RESULT_OK, returnIntent);
	   this.finish();
	
	}
	@SuppressWarnings("unchecked")
	public void handleControllerViewEvent(final ActionEvent e) {
		switch (e.action) {
		case Constants.PUZZLE_CARD:
			listData = (ArrayList<DataLanguageDTO>) e.viewData;

			break;
		case Constants.GET_ITEM:
			
			ArrayList<DataLanguageDTO> list = (ArrayList<DataLanguageDTO>) e.viewData;
			new VerySlowTask(list).execute();
			showListImagePuzzle();
			
			
		default:
			break;
		}
	}

	public void startPuzzle(int index) {
		framePuzzle.removeAllViews();
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inScaled = false;
		puzzle = new Puzzle();
		view = new PuzzleView(this, puzzle);
		framePuzzle.addView(view);
		scramble();
		setPuzzleSize(DEFAULT_SIZE, true);
		newImage(index);
		curIndex = index;
	}

	public void newImage(int index) {
		BitmapFactory.Options o1 = new BitmapFactory.Options();
		o1.inPurgeable = true;
		
		//imageFillBlank.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(listData.get(curIndex).getIdImage()),null,o));
		imagePuzzle.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(listData.get(index).getIdImage()),null,o1));
		Options o = new Options();
		o.inJustDecodeBounds = true;
		InputStream imageStream = this.getResources().openRawResource(
				listData.get(index).getIdImage());
		BitmapFactory.decodeStream(imageStream, null, o);
		int targetWidth = view.getTargetWidth();
		int targetHeight = view.getTargetHeight();
		if (o.outWidth > o.outHeight && targetWidth < targetHeight) {
			int i = targetWidth;
			targetWidth = targetHeight;
			targetHeight = i;
		}
		if (targetWidth < o.outWidth || targetHeight < o.outHeight) {
			double widthRatio = (double) targetWidth / (double) o.outWidth;
			double heightRatio = (double) targetHeight / (double) o.outHeight;
			double ratio = Math.max(widthRatio, heightRatio);

			o.inSampleSize = (int) Math.pow(2,
					(int) Math.round(Math.log(ratio) / Math.log(0.5)));
		} else {
			o.inSampleSize = 1;
		}
		o.inScaled = false;
		o.inJustDecodeBounds = false;
		imageStream = this.getResources().openRawResource(
				listData.get(index).getIdImage());
		Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, o);
		if (bitmap == null) {
			Toast.makeText(this, getString(R.string.hello_world),
					Toast.LENGTH_LONG).show();
			return;
		}
		setBitmap(bitmap);

	}

	private void setBitmap(Bitmap bitmap) {
		portrait = bitmap.getWidth() < bitmap.getHeight();

		view.setBitmap(bitmap);
		setPuzzleSize(Math.min(puzzleWidth, puzzleHeight), true);

		// setRequestedOrientation(portrait ?
		// ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
		// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	private void scramble() {
		puzzle.init(puzzleWidth, puzzleHeight);
		puzzle.scramble();
		// view.invalidate();
		expert = view.getShowNumbers() == ShowNumbers.NONE;

	}

	protected void setPuzzleSize(int size, boolean scramble) {
		float ratio = getImageAspectRatio();

		if (ratio < 1) {
			ratio = 1f / ratio;
		}

		int newWidth;
		int newHeight;

		if (portrait) {
			newWidth = size;
			newHeight = (int) (size * ratio);
		} else {
			newWidth = (int) (size * ratio);
			newHeight = size;
		}

		if (scramble || newWidth != puzzleWidth || newHeight != puzzleHeight) {
			puzzleWidth = newWidth;
			puzzleHeight = newHeight;
			scramble();
		}
	}

	private float getImageAspectRatio() {
		Bitmap bitmap = view.getBitmap();

		if (bitmap == null) {
			return 1;
		}

		float width = bitmap.getWidth();
		float height = bitmap.getHeight();

		return width / height;
	}

	public void onFinish() {

		fishPuzzle();
	}

	public PuzzleStats updateStats()

	{

		SharedPreferences prefs = getPreferences();
		Editor editor = prefs.edit();

		int i = (expert ? 10000 : 0)
				+ Math.min(puzzle.getWidth(), puzzle.getHeight()) * 100
				+ Math.max(puzzle.getWidth(), puzzle.getHeight());
		String index = String.valueOf(i);

		int plays = prefs.getInt(KEY_PLAYS_PREFIX + index, 0);
		int best = prefs.getInt(KEY_MOVE_BEST_PREFIX + index, 0);
		float avg = prefs.getFloat(KEY_MOVE_AVERAGE_PREFIX + index, 0);

		plays++;
		boolean isNewBest = best == 0 || best > puzzle.getMoveCount();

		if (isNewBest) {
			best = puzzle.getMoveCount();
		}

		avg = (avg * (plays - 1) + puzzle.getMoveCount()) / (float) plays;

		// editor.putInt(KEY_PLAYS_PREFIX + index, plays);
		// editor.putInt(KEY_MOVE_BEST_PREFIX + index, best);
		// editor.putFloat(KEY_MOVE_AVERAGE_PREFIX + index, avg);

		editor.commit();

		return new PuzzleStats(plays, best, avg, isNewBest);
	}

	private void showStats() {
		SharedPreferences prefs = getPreferences();

		int i = (expert ? 10000 : 0)
				+ Math.min(puzzle.getWidth(), puzzle.getHeight()) * 100
				+ Math.max(puzzle.getWidth(), puzzle.getHeight());
		String index = String.valueOf(i);

		int plays = prefs.getInt(KEY_PLAYS_PREFIX + index, 0);
		int best = prefs.getInt(KEY_MOVE_BEST_PREFIX + index, 0);
		float avg = prefs.getFloat(KEY_MOVE_AVERAGE_PREFIX + index, 0);

		PuzzleStats stats = new PuzzleStats(plays, best, avg, false);
		showStats(stats);
	}

	public void showStats(PuzzleStats stats) {
		String type = sizeToString(puzzleWidth, puzzleHeight);

		String msg;

		if (puzzle.isSolved()) {
			msg = MessageFormat.format(getString(R.string.hello_world), type,
					expert ? 1 : 0, puzzle.getMoveCount());
		} else {
			msg = MessageFormat.format(getString(R.string.hello_world), type,
					expert ? 1 : 0, puzzle.getMoveCount());
		}

		msg = MessageFormat.format(getString(R.string.hello_world), msg,
				stats.getBest(), stats.getAvg(), stats.getPlays());

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(!puzzle.isSolved() ? R.string.hello_world : stats
				.isNewBest() ? R.string.hello_world : R.string.hello_world);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.hello_world, null);

		builder.create().show();
	}

	protected String sizeToString(int width, int height) {
		return MessageFormat.format(getString(R.string.hello_world), width,
				height);
	}

	protected SharedPreferences getPreferences() {
		return getSharedPreferences(PuzzleActivity.class.getName(),
				Activity.MODE_PRIVATE);
	}

	public PuzzleView getView() {
		return view;
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public void fishPuzzle() {
		ActionEvent e = new ActionEvent(PuzzleActivity.this,
				Constants.UPDATE_PUZZLE, null, null);
		CardsController.getInstance().handleViewEvent(e, listData.get(curIndex).getId(),puzzle.getMoveCount(),null);
		myDialog = new Dialog(PuzzleActivity.this);
		myDialog.setContentView(R.layout.dialog_finish_puzzle);
		myDialog.setTitle("Complete puzzle");
		textmove = (TextView) myDialog.findViewById(R.id.txtmove);
		textmove.setText("Move : " + String.valueOf(puzzle.getMoveCount()));

		btnPlayAgain = (ImageButton) myDialog.findViewById(R.id.btnPlayAgain);
		btnPlayAgain.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startPuzzle(curIndex);
				myDialog.cancel();

			}
		});
		btnNextPuzzle = (ImageButton) myDialog.findViewById(R.id.btnNextPuzzle);
		btnNextPuzzle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				curIndex++;
				if(curIndex >= listData.size()) curIndex=0;
				startPuzzle(curIndex);
				myDialog.cancel();

			}
		});
		myDialog.show();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnBackPuzzle:
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			this.finish();
			break;
		case R.id.btnListImagePuzzle:
			//btnListImagePuzzle.setBackgroundResource(R.drawable.menup);
			customDialog.setTitle("waitting");
			customDialog.show();
			getItem();
			//showListImagePuzzle();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}

	public void getItem() {
		
		ActionEvent e = new ActionEvent(PuzzleActivity.this,
				Constants.GET_ITEM, null, null);
		CardsController.getInstance().handleViewEvent(e, type,0,null);
	}

	public void showListImagePuzzle() {
		final AlertDialog.Builder myDialog = new AlertDialog.Builder(
				PuzzleActivity.this);
		myDialog.setTitle("Images");
		
		//myDialog.show();
		
		
		final ListView listview = new ListView(PuzzleActivity.this);
		adapter = new PuzzleListAdapter(PuzzleActivity.this, listview.getId(),
				arrData);
	
		
		myDialog.setAdapter(adapter, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startPuzzle(which);
				//btnListImagePuzzle.setBackgroundResource(R.drawable.menu);
				myalertDialog.dismiss();
				
			}
		});
		customDialog.dismiss();
		//myDialog.set
		adapter.notifyDataSetChanged();
		myDialog.setCancelable(true);
	
		myDialog.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						//btnListImagePuzzle.setBackgroundResource(R.drawable.menu);
					}
				});
		myDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                	//btnListImagePuzzle.setBackgroundResource(R.drawable.menu);
                    myalertDialog.dismiss();
                }
                return true;
            }
        });
		myalertDialog = myDialog.show();
	}


	private class VerySlowTask extends AsyncTask<String, Object, Void> {
		ArrayList<DataLanguageDTO> _newArr;

		public VerySlowTask(ArrayList<DataLanguageDTO> arrData) {
			this._newArr = arrData;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			arrData = new ArrayList<Object>();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			for (int i = 0; i < _newArr.size(); i++) {
				publishProgress(_newArr.get(i));
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			arrData.add(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {			
			//adapter.notifyDataSetChanged();
			
		}

	}
	
	
	
}
