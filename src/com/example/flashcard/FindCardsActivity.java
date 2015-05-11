package com.example.flashcard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.controller.SettingController;
import com.example.flashcard.model.dto.DataLanguageDTO;
import com.example.flashcard.model.dto.SettingDTO;
import com.example.flashcard.model.utils.ScaleImageView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindCardsActivity extends Activity implements OnClickListener {

	private ImageButton btnBackCardCategoryFind;
	private ImageButton btnCategoryPlayF;
	private ImageView btnImageFind1;
	private ImageView btnImageFind2;
	private ImageView btnImageFind3;
	private ImageView btnImageFind4;

	private LinearLayout layoutCardFind;
	private TextView txtNameImageFindCards;
	int category;
	private ArrayList<DataLanguageDTO> listData;
	private SoundPool soundPool;
	AudioManager audioManager;
	float curVolume;
	float maxVolume;
	float leftVolume;
	float rightVolume;
	int priority = 0;
	int no_loop = 0;
	float normal_playback_rate = 1f;

	int curIndex = 0;
	private int idCard;
	private int idImage;
	private boolean first = false;
	private ArrayList<ImageView> listButton;
	private Random rd;
	int type =1;
	SettingDTO dto;
	boolean resume;
	
	//advance new
	private MediaPlayer playSound;
	private LinearLayout layoutFindSoundCard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_cards);

		dto = new SettingDTO();
		InitDataSetting();
		listData = new ArrayList<DataLanguageDTO>();
		listButton = new ArrayList<ImageView>();
		InitData(dto.getLanguage());
		rd = new Random();
		curIndex = rd.nextInt(listData.size());
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
		audioManager = (AudioManager) getSystemService(MainActivity.AUDIO_SERVICE);
		curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		leftVolume = curVolume / maxVolume;
		rightVolume = curVolume / maxVolume;
		// loadIdSoundPool(soundPool);

		btnBackCardCategoryFind = (ImageButton) findViewById(R.id.btnBackCardCategoryFind);
		btnCategoryPlayF = (ImageButton) findViewById(R.id.btnCategoryPlayF);
	
		
		btnImageFind1 = (ImageView) findViewById(R.id.image1);
		btnImageFind2 = (ImageView) findViewById(R.id.image2);
		btnImageFind3 = (ImageView) findViewById(R.id.image3);
		btnImageFind4 = (ImageView) findViewById(R.id.image4);
		
		layoutCardFind = (LinearLayout) findViewById(R.id.layoutCardFind);
		txtNameImageFindCards = (TextView) findViewById(R.id.txtNameImageF);
		layoutFindSoundCard = (LinearLayout)findViewById(R.id.layoutFindSoundCard);
		
		listButton.add(btnImageFind1);
		listButton.add(btnImageFind2);
		listButton.add(btnImageFind3);
		listButton.add(btnImageFind4);
		
		btnBackCardCategoryFind.setOnClickListener(this);
		btnCategoryPlayF.setOnClickListener(this);

		btnImageFind1.setOnClickListener(this);
		btnImageFind2.setOnClickListener(this);
		btnImageFind3.setOnClickListener(this);
		btnImageFind4.setOnClickListener(this);
		layoutFindSoundCard.setOnClickListener(this);
		
		loadQues();
		first = true;
	}

	public void InitData(int type) {
		ActionEvent e = new ActionEvent(FindCardsActivity.this,
				Constants.FIND_CARDS, null, null);
		CardsController.getInstance().handleViewEvent(e,type,0,null);
	}

	public void InitDataSetting() {
		ActionEvent e = new ActionEvent(FindCardsActivity.this,
				Constants.GET_SETTING_FIND, null, null);
		SettingController.getInstance().handleViewEvent(e,0,0,0);
		
	}
	@SuppressWarnings("unchecked")
	public void handleControllerViewEvent(ActionEvent e) {
		switch (e.action) {
		case Constants.FIND_CARDS:
			listData = (ArrayList<DataLanguageDTO>) e.viewData;
			break;

		default:
			dto =(SettingDTO)e.viewData;
			break;
		}
	}

	public void loadIdSoundPool(SoundPool soundPool) {
		for (int i = 0; i < listData.size(); i++) {
			DataLanguageDTO dto = listData.get(i);
			int id = soundPool.load(this, dto.getIdSoundE(), 1);
			listData.get(i).setIdSoundPool(id);
		}
	}

	public void playAudio(int index) {
		soundPool.play(listData.get(index).getIdSoundPool(), leftVolume,
				rightVolume, priority, no_loop, normal_playback_rate);
	}

	public void playMedia(int index) {
		AssetFileDescriptor afd = this.getResources().openRawResourceFd(
				listData.get(index).getIdSoundE());

		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_cards, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnBackCardCategoryFind:
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			this.finish();
			break;
		case R.id.btnCategoryPlayF:
			resume = true;
			Intent iSetting = new Intent(FindCardsActivity.this,
					SettingActivity.class);
			startActivity(iSetting);
			//finish();
			break;
		case R.id.image1:
			checkAns(btnImageFind1);
			break;

		case R.id.image2:
			checkAns(btnImageFind2);
			break;

		case R.id.image3:
			checkAns(btnImageFind3);
			break;

		case R.id.image4:
			checkAns(btnImageFind4);
			break;
		case R.id.layoutFindSoundCard:
			stopPlaying();
			playSoundCard(curIndex);
			break;
		default:
			break;
		}
	}

	public ArrayList<Map<String, Integer>> getQues() {

		ArrayList<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		Random rd = new Random();
		int indexAns = randomIndexAns();
		ArrayList<Integer> listIndex = new ArrayList<Integer>();
		listIndex.add(curIndex);
		int k = 1;
		for (int i = 0; i < 4; i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (i == indexAns) {
				map.put("idCard", listData.get(curIndex).getId());
				map.put("idImg", listData.get(curIndex).getIdImage());
			} else {
				int index = getRandomWithExclusion(rd, listIndex);
				listIndex.add(index);
				map.put("idCard", listData.get(index).getId());
				map.put("idImg", listData.get(index).getIdImage());
			}
			list.add(map);
		}
		return list;
	}

	public int randomIndexAns() {
		Random rd = new Random();
		return rd.nextInt(4);
	}

	public int getRandomWithExclusion(Random rnd, ArrayList<Integer> exclude) {
		int random = rnd.nextInt(listData.size());
		while (contains(exclude, random)) {
			random = rnd.nextInt(listData.size());
		}
		return random;
	}

	public boolean contains(ArrayList<Integer> list, int k) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == k) {
				return true;
			}
		}
		return false;
	}

	public void loadQues() {

		if (curIndex >= listData.size())  curIndex = 0;
			layoutCardFind.setBackgroundResource(android.R.color.transparent);
			txtNameImageFindCards.setText(listData.get(curIndex).getEnglish());
			ArrayList<Map<String, Integer>> list = getQues();

			
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inPurgeable = true;
			
			btnImageFind1.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(list.get(0).get("idImg")),null,o));//setBackgroundResource(list.get(0).get("idImg"));
			btnImageFind1.setTag(list.get(0).get("idCard"));
			
			//btnImageFind2.setBackgroundResource(list.get(1).get("idImg"));
			btnImageFind2.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(list.get(1).get("idImg")),null,o));
			btnImageFind2.setTag(list.get(1).get("idCard"));
			
			btnImageFind3.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(list.get(2).get("idImg")),null,o));		
			//btnImageFind3.setBackgroundResource(list.get(2).get("idImg"));
			btnImageFind3.setTag(list.get(2).get("idCard"));
			
			btnImageFind4.setImageBitmap(BitmapFactory.decodeStream(this.getResources().openRawResource(list.get(3).get("idImg")),null,o));
			//btnImageFind4.setBackgroundResource(list.get(3).get("idImg"));
			btnImageFind4.setTag(list.get(3).get("idCard"));
			if (!first) {
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// Do something after 5s = 5000ms
						//playMedia(curIndex);
						stopPlaying();
						playSoundCard(curIndex);
						// soundPool.play(listData.get(curIndex).getIdSoundPool(),
						// leftVolume, rightVolume, priority, no_loop,
						// normal_playback_rate);
					}
				}, 200);
			} else {
				for (ImageView bt : listButton) {
					bt.setVisibility(View.VISIBLE);
					bt.clearAnimation();
				}
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// Do something after 5s = 5000ms
						//playMedia(curIndex);
						stopPlaying();
						playSoundCard(curIndex);
						// soundPool.play(listData.get(curIndex).getIdSoundPool(),
						// leftVolume, rightVolume, priority, no_loop,
						// normal_playback_rate);
					}
				}, 200);
				// playMedia(curIndex);
				// soundPool.play(listData.get(curIndex).getIdSoundPool(),
				// leftVolume, rightVolume, priority, no_loop,
				// normal_playback_rate);
			}

		
		
	}

	public void checkAns(ImageView button) {
		// int id = Integer.parseInt(btnImageFind1.getTag().toString()) ;
		if (Integer.parseInt(button.getTag().toString()) == listData.get(
				curIndex).getId()) {
			playTouch(true);
			for (ImageView bt : listButton) {
				// if(bt.getId() != button.getId()){
				bt.clearAnimation();
				bt.setVisibility(View.INVISIBLE);
				// }
			}
			layoutCardFind.setBackgroundResource(listData.get(curIndex)
					.getIdImage());
			curIndex++;
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// Do something after 5s = 5000ms
					loadQues();
				}
			}, 2000);

		} else {
			playTouch(false);
			Animation animBounce = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.bounce);
			button.startAnimation(animBounce);
		}
	}
	@Override
	protected void onResume() {
		//Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
		InitDataSetting();
		InitData(dto.getLanguage());
		if(resume){
			txtNameImageFindCards.setText(listData.get(curIndex).getEnglish());
			//playMedia(curIndex);
			stopPlaying();
			playSoundCard(curIndex);
		}
		super.onResume();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		this.finish();
	}
	public void playSoundCard(int index){
		playSound = MediaPlayer.create(FindCardsActivity.this,listData.get(index).getIdSoundE());
		playSound.start();
	}
	private void stopPlaying() {
        if (playSound != null) {
        	playSound.stop();
        	playSound.release();
        	playSound = null;
       }
    }
}
