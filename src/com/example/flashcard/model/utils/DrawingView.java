package com.example.flashcard.model.utils;

import com.example.flashcard.R;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class DrawingView extends View implements ImageAware{
	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF660000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	private float brushSize, lastBrushSize;
	
	private boolean erase=false;
	public DrawingView(Context context, AttributeSet attrs){
	    super(context, attrs);
	    
	    setupDrawing();
	}
	private void setupDrawing(){
		//get drawing area setup for interaction
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(20);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
		brushSize = getResources().getInteger(R.integer.medium_size);
		lastBrushSize = brushSize;
		
		drawPaint.setStrokeWidth(brushSize);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		//super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//super.onDraw(canvas);
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	//detect user touch     
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
			return false;
		}
		invalidate();
		return true;
	
	}
	public void setColor(String newColor){
		//set color  
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
			    newSize, getResources().getDisplayMetrics());
			brushSize=pixelAmount;
			drawPaint.setStrokeWidth(brushSize);
	}
	
	public void setLastBrushSize(float lastSize){
	    lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
	    return lastBrushSize;
	}
	
	public void setErase(boolean isErase,String color){
		//set erase true or false 
		erase=isErase;
		if(erase) {
			invalidate();
			paintColor = Color.parseColor("#FFFFFFFF");
			drawPaint.setColor(paintColor);
		}
		else {
			invalidate();
			paintColor = Color.parseColor(color);
			drawPaint.setColor(paintColor);
		}
		//drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		//else drawPaint.setXfermode(null);
	}
	
	public void startNew(){
	    drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();
	}
	public void setBackgroundView(Bitmap bmt){
		//Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_1);
        //float scale = (float) bmt.getHeight() / (float) getHeight();
        //int newWidth = Math.round(bmt.getWidth() / scale);
        //int newHeight = Math.round(bmt.getHeight() / scale);
		//drawCanvas.drawColor(Color.WHITE);
		//this.onSizeChanged(w, h, oldw, oldh);
		int width = bmt.getWidth();
	    int height = bmt.getHeight();
	    float scaleWidth = ((float) drawCanvas.getWidth()) / width;
	    float scaleHeight = ((float) drawCanvas.getHeight()) / height;
	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();
	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);
	    // recreate the new Bitmap
	    Bitmap resizedBitmap = Bitmap.createBitmap(bmt, 0, 0, width, height, matrix, false);
		
//        Bitmap mBitmap = Bitmap.createScaledBitmap(bmt, drawCanvas.getWidth(), drawCanvas.getHeight(), false);
        
        drawCanvas.drawBitmap(resizedBitmap, 0, 0, null);
        resizedBitmap.recycle();
		//this.setbac
	}
	@Override
	public ViewScaleType getScaleType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public View getWrappedView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isCollected() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean setImageBitmap(Bitmap arg0) {
		Canvas canvas = new Canvas(arg0.copy(Bitmap.Config.ARGB_8888, true));
		
//		Bitmap mBitmap = Bitmap.createScaledBitmap(arg0, drawCanvas.getWidth(), drawCanvas.getHeight(), true);
//        drawCanvas.drawBitmap(mBitmap, 0, 0, null);
//        mBitmap.recycle();
		return false;
	}
	@Override
	public boolean setImageDrawable(Drawable arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
