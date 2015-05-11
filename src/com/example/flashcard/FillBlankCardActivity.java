package com.example.flashcard;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.example.flashcard.adapter.FillListAdapter;
import com.example.flashcard.adapter.PuzzleListAdapter;
import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.model.dto.DataLanguageDTO;
import com.example.flashcard.model.utils.FlipAnimation;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FillBlankCardActivity extends Activity implements OnClickListener {
	private ArrayList<ImageButton> listButtonAns;
	private ArrayList<ImageButton> listButtonQues;
	private ArrayList<ImageButton> listButtonKeyWord;
	private ArrayList<DataLanguageDTO> listData;
	
	private LinearLayout linearLayoutKey;
	private LinearLayout linearLayoutAlpha1;
	private LinearLayout linearLayoutAlpha2;
	private ButtonListener buttonListener;
	
	private ImageButton btnBackFill;
	private ImageButton btnCategoryPlayFill;
	private ImageButton btnHelpFill;
	private ImageView imageFillBlank;
	private int curIndex;
	private int indexCharQ;
	private int category;
	
	//sound
	private SoundPool soundPool;
	AudioManager audioManager;
	float curVolume;
	float maxVolume ;
	float leftVolume ;
	float rightVolume ;
	int priority = 0;
	int no_loop = 0;
	float normal_playback_rate = 1f;
	
	private boolean helpFill;
	TableRow tr;
	private Random rd;
	int type =1;
	
	//time
	private long startTime = 0L;
	private Handler handleClock = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	private TextView textViewClock;
	private ImageButton btnSpeaker;
	
	//
	private AlertDialog myalertDialog;
	private ArrayList<Object> arrData;
	ProgressDialog pDialog;
	FillListAdapter adapter;
	private Dialog customDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_blank_card);
		buttonListener = new ButtonListener();
		
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
	    audioManager = (AudioManager)getSystemService(MainActivity.AUDIO_SERVICE);
	    curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    leftVolume = curVolume/maxVolume;
	    rightVolume = curVolume/maxVolume;
	   
		
		
		listData = new ArrayList<DataLanguageDTO>();
		
		InitData(type);
		
		rd = new Random();
		curIndex = rd.nextInt(listData.size());
		
		//loadIdSoundPool(soundPool);
		//get control from layout
		linearLayoutKey =(LinearLayout)findViewById(R.id.linearLayout);
		linearLayoutAlpha1 = (LinearLayout)findViewById(R.id.linearLayoutAlpha1);
		linearLayoutAlpha2 = (LinearLayout)findViewById(R.id.linearLayoutAlpha2);
		btnBackFill =(ImageButton)findViewById(R.id.btnBackFill);
		btnCategoryPlayFill =(ImageButton)findViewById(R.id.btnCategoryPlayFill);
		btnHelpFill =(ImageButton)findViewById(R.id.btnHelpFill);
		imageFillBlank = (ImageView)findViewById(R.id.imageFillBlank);
		textViewClock =(TextView)findViewById(R.id.textViewClock);
		btnSpeaker =(ImageButton)findViewById(R.id.btnSpeaker);
		
		//set click or touch
		btnBackFill.setOnClickListener(this);
		btnCategoryPlayFill.setOnClickListener(this);
		btnHelpFill.setOnClickListener(this);
		imageFillBlank.setOnClickListener(this);
		btnSpeaker.setOnClickListener(this);
	
		startQues(listData.get(curIndex).getEnglish());
		customDialog = new Dialog(FillBlankCardActivity.this);
	}

	
	public void InitData(int type) {
		ActionEvent e = new ActionEvent(FillBlankCardActivity.this, Constants.FILLBLANK_CARD,
				null, null);
		CardsController.getInstance().handleViewEvent(e,type,0,null);
	}
	@SuppressWarnings("unchecked")
	public void handleControllerViewEvent(ActionEvent e) {
		switch (e.action) {
		case Constants.FILLBLANK_CARD:
			listData = (ArrayList<DataLanguageDTO>) e.viewData;
			break;
		case Constants.GET_ITEM_FILL:
			ArrayList<DataLanguageDTO> list = (ArrayList<DataLanguageDTO>) e.viewData;
			new VerySlowTask(list).execute();
			showListImage();
		default:
			break;
		}
	}
	public void loadIdSoundPool(SoundPool soundPool){
		for(int i = 0; i < listData.size();i++)
		{
			DataLanguageDTO dto = listData.get(i);
			int id = soundPool.load(this, dto.getIdSoundE(),1);
			listData.get(i).setIdSoundPool(id);
		}
	}
	public void playAudio(int index){
			soundPool.play(listData.get(index).getIdSoundPool(), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
	}
	
	public void playMedia(int index){
		 AssetFileDescriptor afd = this.getResources().openRawResourceFd(listData.get(index).getIdSoundE());
 
        MediaPlayer mp = new MediaPlayer();
        try {
       	 mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
       	 afd.close();
	         mp.prepare();
	         mp.start();
	         mp.setOnCompletionListener(new OnCompletionListener() {
	        	    public void onCompletion(MediaPlayer mp) {
	        	        mp.release();

	        	    };
	         });
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	    
	}
	public void playTouch(boolean touch){
		AssetFileDescriptor afd = this.getResources().openRawResourceFd(touch ? R.raw.khophinh : R.raw.cham);

		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			afd.close();
			mp.prepare();
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
        	    public void onCompletion(MediaPlayer mp) {
        	        mp.release();

        	    };
         });
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void playSoundFinish(){
		AssetFileDescriptor afd = this.getResources().openRawResourceFd(R.raw.clapping);

		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			afd.close();
			mp.prepare();
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
        	    public void onCompletion(MediaPlayer mp) {
        	        mp.release();

        	    };
         });
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_blank_card, menu);
		return true;
	}

	public void addButtonQues(String name){
		name = name.toLowerCase();
		ArrayList<Integer> listCharQ = randomCharQues(name);
		 for(int i = 0 ; i <name.length(); i++){
			 	ImageButton btn = new ImageButton(this);
	            btn.setId(i);
	            btn.setTag(Character.toString(name.charAt(i)));
	           if(listCharQ.contains(i)){
	        	   btn.setBackgroundResource(this.getResources().getIdentifier("ques" , "drawable", this.getPackageName()));
	        	   listButtonQues.add(btn);
	        	  // listId.add(i);
	           }
	           else btn.setBackgroundResource(this.getResources().getIdentifier(Character.toString(name.charAt(i)) , "drawable", this.getPackageName()));
	           if(Character.toString(name.charAt(i)).equals(" "))
	        	   btn.setVisibility(View.INVISIBLE);
	           listButtonKeyWord.add(btn);
	           linearLayoutKey.addView(btn);
		 }
	}
	public void clearanimation(){
		for (ImageButton bt:listButtonQues)
			bt.clearAnimation();
	}
	public ArrayList<Integer> randomCharQues(String name){
		ArrayList<Integer> listChar = new ArrayList<Integer>();
		Random rd = new Random();
		int min=name.length()-1;
		//if(name.length() > 3 ) min = 2 ;
		//else min = 1;
		int count =  rd.nextInt(name.length() - min) + min;
		for(int i = 0 ; i < count ; i++){
			int index = rd.nextInt(name.length());
			while(listChar.contains(index)){
				index = rd.nextInt(name.length());
			}	
			listChar.add(index);
		}	
		return listChar;
	}
	public void addButtonAns(String name){
		ArrayList<String> list = getlistStringAns(name);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 0, 0, 0);
		for(int i = 0; i< list.size(); i++){
			ImageButton btn = new ImageButton(this);
            btn.setId(i);
            btn.setTag(list.get(i));
            btn.setBackgroundResource(this.getResources().getIdentifier(list.get(i)+"l" , "drawable", this.getPackageName()));
            listButtonAns.add(btn);
            btn.setOnClickListener(buttonListener);
			if(i < list.size()/2){
				btn.setLayoutParams(params);
				linearLayoutAlpha1.addView(btn);
			}
			else {
				btn.setLayoutParams(params);
				linearLayoutAlpha2.addView(btn);
			}
		}
	}
	public ArrayList<String> getlistStringAns(String name){
		Random rd = new Random();
		name = name.trim();
		name = name.toLowerCase();
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < name.length(); i++){
			if(!list.contains(Character.toString(name.charAt(i))) && !(Character.toString(name.charAt(i))).equals(" "))
				list.add(Character.toString(name.charAt(i)));
		}
		while(list.size() < 12){
			int i = rd.nextInt(26);
			while(list.contains(Character.toString((char)(i+97)))){
				i = rd.nextInt(26);
			}
			list.add(Character.toString((char)(i+97)));
		}
		Collections.sort(list);
		return list;
	}
	public void startQues(String name){
		if(curIndex >= listData.size()) curIndex = 0;
		listButtonAns = new ArrayList<ImageButton>();
		listButtonQues  = new ArrayList<ImageButton>();
		listButtonKeyWord  = new ArrayList<ImageButton>();
		timeClock();
		linearLayoutAlpha1.removeAllViews();
		linearLayoutAlpha2.removeAllViews();
		linearLayoutKey.removeAllViews();
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inPurgeable = true;
		
		imageFillBlank.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(listData.get(curIndex).getIdImage()),null,o));//setBackgroundResource(listData.get(curIndex).getIdImage());
		addButtonQues(name);
		addButtonAns(name);
		ImageButton bt = listButtonQues.get(0);
		FlipAnimation flipAnimation = new FlipAnimation((View)bt, (View)bt);
   	 	flipAnimation.setRepeatCount(-1);
   	 	flipAnimation.setRepeatMode(2);
   	 	bt.startAnimation(flipAnimation);
	}
	public void checkAns(ImageButton bt){
		String tag = bt.getTag().toString();
		for(int i = 0; i < listButtonQues.size();i++){
			ImageButton btn = listButtonQues.get(i);
			if(i == indexCharQ){
				if(btn.getTag().toString().equals(tag)){
					btn.setBackgroundResource(this.getResources().getIdentifier(btn.getTag().toString() , "drawable", this.getPackageName()));
					btn.clearAnimation();
					playTouch(true);
		        	indexCharQ++;  	
				}
				else playTouch(false);
				//if(btn.getTag().toString().equals(" "))  indexCharQ++;  	
				if(checkFinishAns()) 
	        		nextQues();
	        	else 
	        		nextCharacter(indexCharQ);
				break;
			}
			
		}
	}
	public void nextCharacter(int index){
		ImageButton bt = listButtonQues.get(index);
		if(bt.getTag().toString().equals(" ")){
			indexCharQ++; 
			index ++;
			bt = listButtonQues.get(index);
		}
		FlipAnimation flipAnimation = new FlipAnimation((View)bt, (View)bt);
   	 	flipAnimation.setRepeatCount(-1);
   	 	flipAnimation.setRepeatMode(2);
   	 	bt.startAnimation(flipAnimation);
	}
	public boolean checkFinishAns(){
		boolean result = false;
		if(indexCharQ >= listButtonQues.size()){
			//playMedia(curIndex);
			fadeKeyWord();
			indexCharQ = 0;
			helpFill = false;
			btnHelpFill.setBackgroundResource(R.drawable.help);
			listButtonAns = new ArrayList<ImageButton>();
			listButtonQues  = new ArrayList<ImageButton>();
			listButtonKeyWord  = new ArrayList<ImageButton>();
			result = true;
			updateFillImage();
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			    @Override
			    public void run() {
			    	if(curIndex < listData.size())
			    		startQues(listData.get(curIndex).getEnglish());
			    }
			}, 3300);
			
			
		}
		return result;
	}
	public void fadeKeyWord(){
		playSoundFinish();
		Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_out);
		for(int i = 0; i < listButtonKeyWord.size(); i ++){
			if(!listButtonKeyWord.get(i).getTag().toString().equals(" "))
				listButtonKeyWord.get(i).startAnimation(animFadeOut);
		}
	}
	public void nextQues(){
		indexCharQ = 0;
		curIndex ++;
	}
	public void updateFillImage(){
		ActionEvent e = new ActionEvent(FillBlankCardActivity.this,
				Constants.UPDATE_FILL, null, null);
		CardsController.getInstance().handleViewEvent(e, listData.get(curIndex).getId(),0,getTimeFill(textViewClock.getText().toString()));
	}
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {		
				int id = v.getId(); 
				//playTouch(false);
				Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
						R.anim.bounce);
				v.startAnimation(animBounce);
				if(helpFill) v.clearAnimation();
				checkAns((ImageButton)v);
			}
			
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.btnBackFill:
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			this.finish();
			break;
		case R.id.btnCategoryPlayFill:
			//btnCategoryPlayFill.setBackgroundResource(R.drawable.menup);
			customDialog.setTitle("waiting");
			customDialog.show();
			getItem();
			//showListImage();
			break;
		case R.id.btnHelpFill:
			if(!helpFill && indexCharQ < listButtonQues.size()){
				ImageButton btn = listButtonQues.get(indexCharQ);
				ImageButton bt = null;
				for(ImageButton button:listButtonAns){
					if(button.getTag().toString().equals(btn.getTag().toString())){
						bt = button;
						break;
					}	
				}
				FlipAnimation flipAnimation = new FlipAnimation((View)bt, (View)bt);
		   	 	flipAnimation.setRepeatCount(-1);
		   	 	flipAnimation.setRepeatMode(2);
		   	 	bt.startAnimation(flipAnimation);
		   	 	btnHelpFill.setBackgroundResource(R.drawable.nohelp);
		   	 	helpFill =true;
			}
			break;
		case R.id.imageFillBlank:
			playMedia(curIndex);
			break;
		case R.id.btnSpeaker:
			playMedia(curIndex);
			break;
		default :
			
			break;
		}
	}
	private void timeClock(){
		startTime = SystemClock.uptimeMillis();
		handleClock.postDelayed(updateTimerThread, 0);
	}
	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			
			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			textViewClock.setText("" + mins + ":"+ String.format("%02d", secs));
			handleClock.postDelayed(this, 0);
		}

	};
	public String getTimeFill(String time){
		String[] arr = time.split(":");
		int mins = Integer.parseInt(arr[0]);
		int sec = Integer.parseInt(arr[1]);
		return String.valueOf(mins*60 +sec);
	}
	public void showListImage() {
		arrData = new ArrayList<Object>();
		final AlertDialog.Builder myDialog = new AlertDialog.Builder(
				FillBlankCardActivity.this);
		myDialog.setTitle("Images");
		final ListView listview = new ListView(FillBlankCardActivity.this);
		//myDialog.setView(listview);
		
		adapter = new FillListAdapter(FillBlankCardActivity.this, listview.getId(),arrData);
		
		myDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				curIndex = which;
				indexCharQ = 0;
				clearanimation();
				startQues(listData.get(which).getEnglish());
				//btnCategoryPlayFill.setBackgroundResource(R.drawable.menu);
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
						//btnCategoryPlayFill.setBackgroundResource(R.drawable.menu);
						dialog.dismiss();
					}
				});
		myDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                	//btnCategoryPlayFill.setBackgroundResource(R.drawable.menu);
                    myalertDialog.dismiss();
                }
                return true;
            }
        });
		//listview.setAdapter(adapter);
//		//getItem();
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				curIndex = arg2;
//				indexCharQ = 0;
//				clearanimation();
//				startQues(listData.get(arg2).getEnglish());
//				myalertDialog.dismiss();
//
//			}
//		});
		myalertDialog = myDialog.show();
	}

	public void getItem(){
		ActionEvent e = new ActionEvent(FillBlankCardActivity.this,
				Constants.GET_ITEM_FILL, null, null);
		CardsController.getInstance().handleViewEvent(e, type,0,null);
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
//			pDialog = new ProgressDialog(FillBlankCardActivity.this);
//			pDialog.setMessage("waitting");
//			pDialog.setIndeterminate(true);
//			pDialog.setCancelable(false);
//			pDialog.show();
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
			// TODO Auto-generated method stub
			//pDialog.dismiss();
			//adapter.notifyDataSetChanged();
		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		this.finish();
	}
}
