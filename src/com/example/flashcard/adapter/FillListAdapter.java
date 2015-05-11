package com.example.flashcard.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flashcard.R;
import com.example.flashcard.adapter.PuzzleListAdapter.ViewHolder;
import com.example.flashcard.model.dto.DataLanguageDTO;

public class FillListAdapter extends ArrayAdapter<Object>{
	private Context myContext;
	private ArrayList<Object> arrObjects;
	
	
	public FillListAdapter(Context context, int resource, ArrayList<Object> objects) {
		super(context, resource, objects);
		myContext = context;
		setArrObjects(objects);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getArrObjects().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder{
		ImageView img;
		ImageView imageCheckFinished;
		TextView tv;
	}
//	class ViewHolder{
//		TextView tv;
//	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.row_image_fill, parent, false);
		//ViewHolder holder = new ViewHolder();
		//holder.tv = (TextView)rootView.findViewById(R.id.txtTextImg);
		//DataLanguageDTO dto;
		//holder.tv.setText("move: ");
		ViewHolder holder = new ViewHolder();
		holder.tv = (TextView)rootView.findViewById(R.id.textTimeComplete);
		holder.imageCheckFinished = (ImageView)rootView.findViewById(R.id.imageCheckFinishedFill);
		holder.img = (ImageView)rootView.findViewById(R.id.imageItemFill);
		DataLanguageDTO dto;
		Object o = (Object)getArrObjects().get(position);
		dto = (DataLanguageDTO)o;
		Bitmap bm =decodeFile(dto.getIdImage());
		holder.img.setImageBitmap(bm);
		if(dto.getCheckFinishedFill()==0){
			Bitmap bmuncheck =decodeFile(R.drawable.uncheck);
			holder.imageCheckFinished.setImageBitmap(bmuncheck);
		}
		else {
			Bitmap bmcheck =decodeFile(R.drawable.check);
			holder.imageCheckFinished.setImageBitmap(bmcheck);
		}
		holder.tv.setText("time: " + String.valueOf(dto.getTimeFill())+ "s");
		return rootView;
	}
	public ArrayList<Object> getArrObjects() {
		return arrObjects;
	}

	public void setArrObjects(ArrayList<Object> arrObjects) {
		this.arrObjects = arrObjects;
	}
	private Bitmap decodeFile(int id){
	    //Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		o.inPurgeable = true;
		BitmapFactory.decodeStream(myContext.getResources().openRawResource(id),null,o);

		//The new size we want to scale to
		final int REQUIRED_SIZE=50;

		//Find the correct scale value. It should be the power of 2.
		int scale=1;
		while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
		    scale*=2;

		//Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize=scale;
		o2.inPurgeable = true;
		return  BitmapFactory.decodeStream(myContext.getResources().openRawResource(id),null,o2);
	}
}
