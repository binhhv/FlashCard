package com.example.flashcard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.example.flashcard.model.utils.DefinedGlobalVar;


public class CategoryActivity extends Activity implements OnClickListener {
	private ImageButton btnBackCategory;
	private ImageButton category1;
	private int typePlay = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			typePlay = extras.getInt(DefinedGlobalVar.PLAY_CATEGORY);
		}
		
		btnBackCategory =(ImageButton) findViewById(R.id.btnBackCategory);
		category1 =(ImageButton)findViewById(R.id.category1);
		btnBackCategory.setOnClickListener(this);
		category1.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnBackCategory:
			this.finish();
			break;
		case R.id.category1:
			typePlayIntent(typePlay,1);
			break;
		case R.id.category2:
			typePlayIntent(typePlay,2);
			break;
		case R.id.category3:
			typePlayIntent(typePlay,3);
			break;
		case R.id.category4:
			typePlayIntent(typePlay,4);
			break;
		case R.id.category5:
			typePlayIntent(typePlay,5);
			break;
		case R.id.category6:
			typePlayIntent(typePlay,6);
			break;
		default:
			break;
		}
	}

	public void typePlayIntent(int type, int category){
		Intent i;
		switch (type){
		case DefinedGlobalVar.VALUE_PLAY_CATEGORY1:
			 i = new Intent(CategoryActivity.this,CardsActivity.class);
			 i.putExtra("category",category);
			 startActivity(i);
			break;
		case DefinedGlobalVar.VALUE_PLAY_CATEGORY2:
			 i = new Intent(CategoryActivity.this,FindCardsActivity.class);
			 i.putExtra("category",category);
			 startActivity(i);
			break;
		case DefinedGlobalVar.VALUE_PLAY_CATEGORY3:
			 i = new Intent(CategoryActivity.this,FillBlankCardActivity.class);
			 i.putExtra("category",category);
			 startActivity(i);
			break;
		default:
			 i = new Intent(CategoryActivity.this,CardsActivity.class);
			 i.putExtra("category",category);
			 startActivity(i);
			break;
		}
	}
}
