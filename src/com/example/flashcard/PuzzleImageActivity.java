package com.example.flashcard;

import java.util.ArrayList;

import com.example.flashcard.adapter.PuzzleListAdapter;
import com.example.flashcard.common.ActionEvent;
import com.example.flashcard.constants.Constants;
import com.example.flashcard.controller.CardsController;
import com.example.flashcard.model.dto.DataLanguageDTO;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PuzzleImageActivity extends Activity {
	int type = 1;
	private ArrayList<Object> arrData;
	private PuzzleListAdapter adapter;
	private ListView listImagePuzzle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_image);
		getItem();
		arrData = new ArrayList<Object>();
		listImagePuzzle = (ListView)findViewById(R.id.listImagePuzzle);
		adapter = new PuzzleListAdapter(PuzzleImageActivity.this,R.layout.row_image_puzzle,arrData);
		listImagePuzzle.setAdapter(adapter);
		

		listImagePuzzle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_image, menu);
		return true;
	}
	public void getItem(){
		ActionEvent e = new ActionEvent(PuzzleImageActivity.this,
				Constants.GET_ITEM, null, null);
		CardsController.getInstance().handleViewEvent(e,type,0,null);
	}
	@SuppressWarnings("unchecked")
	public void handleControllerViewEvent(final ActionEvent e) {
		switch (e.action) {
		case Constants.GET_ITEM:
			ArrayList<DataLanguageDTO> list = (ArrayList<DataLanguageDTO>) e.viewData;
			new VerySlowTask(list).execute();
		default:
			break;
		}
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
		adapter.notifyDataSetChanged();
	}

}
}
