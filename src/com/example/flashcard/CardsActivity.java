package com.example.flashcard;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.controller.SettingController;
import com.example.flashcard.model.dto.DataLanguageDTO;
import com.example.flashcard.model.dto.SettingDTO;
import com.example.flashcard.model.utils.DefinedGlobalVar;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class CardsActivity extends Activity implements OnClickListener,
		OnTouchListener, ViewFactory {

	private ImageButton btnCategoryCard;
	private ImageButton btnVolume;
	private ImageButton btnPreviousImg;
	private ImageButton btnNextImg;
	//private ImageSwitcher Switch;
	private ImageButton btnReplaySpeakCard;
	private TextView textNameImg;
	private ArrayList<DataLanguageDTO> listData;
	private SoundPool soundPool;
	int downX;
	int upX;
	int curIndex = 0;
	ListUpdater listUpdater;

	AudioManager audioManager;
	float curVolume;
	float maxVolume;
	float leftVolume;
	float rightVolume;
	int priority = 0;
	int no_loop = 0;
	float normal_playback_rate = 1f;
	int category;
	private ArrayList<DataLanguageDTO> arrData;
	Handler handler = new Handler();
	int type = 1;
	int swipeImage = 0;
	SettingDTO dto;
	
	private MediaPlayer playSound;
	private LinearLayout layoutSoundCard;
	private LinearLayout layoutIMGSoundCard;
	private ImageView imageView1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cards);
		dto = new SettingDTO();

		stopPlaying();
		
		//Toast.makeText(this, String.valueOf(dtoSetting.getLanguage()), Toast.LENGTH_LONG).show();
		//mp = new MediaPlayer();
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
		audioManager = (AudioManager) getSystemService(MainActivity.AUDIO_SERVICE);
		curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		leftVolume = curVolume / maxVolume;
		rightVolume = curVolume / maxVolume;
		listData = new ArrayList<DataLanguageDTO>();

		btnCategoryCard = (ImageButton) findViewById(R.id.btnBackCard);
		btnVolume = (ImageButton) findViewById(R.id.btnCategoryPlay);
		btnPreviousImg = (ImageButton) findViewById(R.id.btnPreviousImg);
		btnNextImg = (ImageButton) findViewById(R.id.btnNextImg);
		//Switch = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		layoutSoundCard =(LinearLayout)findViewById(R.id.layoutSoundCard);
		imageView1 = (ImageView)findViewById(R.id.imageView111);
		imageView1.setOnTouchListener(this);
		
		layoutSoundCard.setOnClickListener(this);
		
		//layoutIMGSoundCard = (LinearLayout)findViewById(R.id.layoutIMGSoundCard);
		//layoutIMGSoundCard.setOnClickListener(this);
		//Switch.setOnClickListener(this);
		textNameImg = (TextView) findViewById(R.id.txtNameImage);
		btnReplaySpeakCard =(ImageButton)findViewById(R.id.btnReplaySpeakCard);

		
		btnCategoryCard.setOnClickListener(this);
		btnVolume.setOnClickListener(this);
		btnPreviousImg.setOnClickListener(this);
		btnNextImg.setOnClickListener(this);
		btnReplaySpeakCard.setOnClickListener(this);
		//Switch.setOnTouchListener(this);
		//Switch.setFactory(this);
		//Switch.setInAnimation(AnimationUtils.loadAnimation(this,
				//android.R.anim.fade_in));

		btnPreviousImg.setVisibility(View.INVISIBLE);
		
		
		InitDataSetting();
		InitData(dto.getLanguage());
		loadCardCurent(curIndex);
		//InitData(dto.getLanguage());
		
		if(swipeImage == 1){
			btnNextImg.setVisibility(View.INVISIBLE);
			btnPreviousImg.setVisibility(View.INVISIBLE);
		}
		//Switch.setImageDrawable(decodeFile(listData.get(curIndex).getIdImage()));
		//loadCardFirst();
		
//		t.start();
	}
	Thread t = new Thread(new Runnable() {
		
		@Override
		
		public synchronized void run() {
			// TODO Auto-generated method stub
			loadCardCurent(curIndex);
		}
	});
	public void InitData(int type) {
		ActionEvent e = new ActionEvent(CardsActivity.this,
				Constants.PLAY_CARDS, null, null);
		CardsController.getInstance().handleViewEvent(e,type,0,null);
		
	}
	public void InitDataSetting() {
		ActionEvent e = new ActionEvent(CardsActivity.this,
				Constants.GET_SETTING_PLAY, null, null);
		SettingController.getInstance().handleViewEvent(e,0,0,0);
		
	}

	public void loadCardFirst() {	
		textNameImg.setText(listData.get(0).getEnglish());
		//Switch.setImageResource(listData.get(0).getIdImage());
		//Switch.setImageDrawable(decodeFile(listData.get(0).getIdImage()));
		playMedia(0);
	}
	public void loadCardCurent(int index){
		
		imageView1.setImageResource(listData.get(index).getIdImage());
//		Switch.setImageDrawable(decodeFile(listData.get(curIndex).getIdImage()));
		textNameImg.setText(listData.get(index).getEnglish());
		//playMedia(index); sua o day
		stopPlaying();
		playSoundCard(index);
	}
	public void loadFirst(){
		textNameImg.setText(listData.get(0).getEnglish());
		//Switch.setImageResource(listData.get(0).getIdImage());
		soundPool.play((soundPool.load(CardsActivity.this, listData.get(0).getIdSoundE(), 1)), leftVolume,
				rightVolume, priority, no_loop, normal_playback_rate);
	}
	public void loadIdSoundPool(final SoundPool soundPool) {
		new Thread(new Runnable(
				) {
			
			@Override
			public void run() {
				for (int i = 0; i < listData.size(); i++) {
					DataLanguageDTO dto = listData.get(i);
					int id = soundPool.load(CardsActivity.this, dto.getIdSoundE(), 1);
					listData.get(i).setIdSoundPool(id);
				}
				
			}
		}).start();
			
		
		
	}

	public void playAudio(int index) {
		
		soundPool.play(listData.get(index).getIdSoundPool(), leftVolume,
				rightVolume, priority, no_loop, normal_playback_rate);
	}
	public void playMedia(int index){
		 AssetFileDescriptor afd = this.getResources().openRawResourceFd(listData.get(index).getIdSoundE());
		 //MediaPlayer media = MediaPlayer.create(this,listData.get(index).getIdSoundE());
         MediaPlayer mp = new MediaPlayer();
         try {
        	 mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        	 afd.close();
        	 mp.setVolume((float)dto.getVolume()/100, (float)dto.getVolume()/100);
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
	public void playSoundCard(int index){
		playSound = MediaPlayer.create(CardsActivity.this,listData.get(index).getIdSoundE());
		playSound.start();
	}
	private void stopPlaying() {
        if (playSound != null) {
        	playSound.stop();
        	playSound.release();
        	playSound = null;
       }
    }
	@SuppressWarnings("unchecked")
	public void handleControllerViewEvent(ActionEvent e) {
		switch (e.action) {
		case Constants.PLAY_CARDS:
			listData = (ArrayList<DataLanguageDTO>) e.viewData;
			//new ListUpdater(listData).execute();
			break;
		default:
			dto =(SettingDTO)e.viewData;
			swipeImage = dto.getSwipe();
			break;
		}
	}

	class ListUpdater extends AsyncTask<Object, Object, Void> {
		private ArrayList<DataLanguageDTO> listDT;

		public ArrayList<DataLanguageDTO> getListData() {
			return listDT;
		}

		public ListUpdater(ArrayList<DataLanguageDTO> list) {
			this.listDT = list;

		}

		@Override
		protected Void doInBackground(Object... params) {
			for (int i = 0; i < listDT.size(); i++) {
				publishProgress(listDT.get(i));	
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			listData.add((DataLanguageDTO) values[0]);
			
			// super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
		
			
		}
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return imageView;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//stopPlaying();
		//playSoundCard(curIndex);
		if(swipeImage == 1){
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				downX = (int) event.getX();
				Log.i("event.getX()", " downX " + downX);
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				upX = (int) event.getX();
				Log.i("event.getX()", " upX " + downX);
				if (upX - downX > 100) {
					Animation in = AnimationUtils.loadAnimation(this,
							android.R.anim.fade_in);
					Animation out = AnimationUtils.loadAnimation(this,
							android.R.anim.fade_out);
					curIndex--;
					if (curIndex > 0) {
						//btnNextImg.setVisibility(View.VISIBLE);
					} else {
						//btnNextImg.setVisibility(View.VISIBLE);
						//btnPreviousImg.setVisibility(View.INVISIBLE);
						curIndex = 0;
					}
					textNameImg.setText(listData.get(curIndex).getEnglish());
					//Switch.setImageResource(listData.get(curIndex).getIdImage());
					imageView1.setImageDrawable(decodeFile(listData.get(curIndex).getIdImage()));
					stopPlaying();
					playSoundCard(curIndex);
					//playMedia(curIndex);
				} else if (downX - upX > 100) {
					curIndex++;
					if (curIndex < listData.size() - 1) {
						//btnPreviousImg.setVisibility(View.VISIBLE);
					} else {
						//btnNextImg.setVisibility(View.INVISIBLE);
						//btnPreviousImg.setVisibility(View.VISIBLE);
						curIndex = listData.size() - 1;
					}
					textNameImg.setText(listData.get(curIndex).getEnglish());
					//Switch.setImageResource(listData.get(curIndex).getIdImage());
					imageView1.setImageDrawable(decodeFile(listData.get(curIndex).getIdImage()));
					stopPlaying();
					playSoundCard(curIndex);
					//playMedia(curIndex);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnBackCard:
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			this.finish();
			break;
		case R.id.btnCategoryPlay:
			Intent iSetting = new Intent(CardsActivity.this,
					SettingActivity.class);
			startActivity(iSetting);
			//finish();
			break;
		case R.id.btnPreviousImg:
			curIndex--;
			if (curIndex > 0) {
				btnNextImg.setVisibility(View.VISIBLE);
			} else {
				btnPreviousImg.setVisibility(View.INVISIBLE);
				btnNextImg.setVisibility(View.VISIBLE);
				curIndex = 0;
			}
			textNameImg.setText(listData.get(curIndex).getEnglish());
			//Switch.setImageResource(listData.get(curIndex).getIdImage());
			imageView1.setImageDrawable(decodeFile(listData.get(curIndex).getIdImage()));
			stopPlaying();
			playSoundCard(curIndex);
			//playMedia(curIndex);
			break;
		case R.id.btnNextImg:
			curIndex++;
			if (curIndex < listData.size() - 1) {
				btnPreviousImg.setVisibility(View.VISIBLE);
			} else {
				btnNextImg.setVisibility(View.INVISIBLE);
				btnPreviousImg.setVisibility(View.VISIBLE);

			}
			textNameImg.setText(listData.get(curIndex).getEnglish());
			//Switch.setImageResource(listData.get(curIndex).getIdImage());
			imageView1.setImageDrawable(decodeFile(listData.get(curIndex).getIdImage()));
			stopPlaying();
			playSoundCard(curIndex);
			//playMedia(curIndex);
			break;
		case R.id.btnReplaySpeakCard:
			playMedia(curIndex);
			break;
//		case R.id.imageSwitcher:
//			stopPlaying();
//			playSoundCard(curIndex);
//			break;
		case R.id.layoutSoundCard:
			stopPlaying();
			playSoundCard(curIndex);
			break;
//		case R.id.layoutIMGSoundCard:
//			stopPlaying();
//			playSoundCard(curIndex);
//			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroy() {
		soundPool.release();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		//Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
		//InitDataSetting();
		if(swipeImage == 1){
			btnNextImg.setVisibility(View.INVISIBLE);
			btnPreviousImg.setVisibility(View.INVISIBLE);
		}
		else {
			btnNextImg.setVisibility(View.VISIBLE);
		}
		
//		loadCardCurent(curIndex);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//Toast.makeText(this,"pause",Toast.LENGTH_SHORT).show();
		super.onPause();
	}
	@SuppressWarnings("deprecation")
	private Drawable decodeFile(int id){
	    //Decode image size
		Drawable drawable;
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		o.inPurgeable = true;
		BitmapFactory.decodeStream(this.getResources().openRawResource(id),null,o);

		//The new size we want to scale to
		final int REQUIRED_SIZE=100;

		//Find the correct scale value. It should be the power of 2.
		int scale=2;
		while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
		    scale*=2;

		//Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		//o2.inSampleSize=scale;
		o2.inPurgeable = true;
		drawable = new BitmapDrawable(BitmapFactory.decodeStream(this.getResources().openRawResource(id),null,o2));
		return  drawable;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		stopPlaying();
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		this.finish();
	}

	
}
