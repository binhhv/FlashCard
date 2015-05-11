package com.example.flashcard.model.utils;

import com.example.flashcard.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
	// drawing path
	private Path drawPath;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	// initial color
	private int paintColor = 0xFF660000;
	// canvas
	private Canvas drawCanvas;
	// canvas bitmap
	private Bitmap canvasBitmap;
	private float brushSize, lastBrushSize;
	
	Paint mPaint;
	Bitmap mBitmap;
	Matrix mMatrix;
	RectF mSrcRectF;
	RectF mDestRectF;
	boolean mPause;
	private boolean erase = false;
	public int width;
	public int height;

	public PaintView(Context context) {
		super(context);
		mPaint = new Paint();
		mMatrix = new Matrix();
		mSrcRectF = new RectF();
		mDestRectF = new RectF();
		mPause = false;
		setupDrawing();
	}

	public void setupDrawing() {
		// get drawing area setup for interaction
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
		// super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// // TODO Auto-generated method stub
	// //super.onDraw(canvas);
	// canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
	// canvas.drawPath(drawPath, drawPaint);
	// }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// detect user touch
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

	public void setColor(String newColor) {
		// set color
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	public void setBrushSize(float newSize) {
		float pixelAmount = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, newSize, getResources()
						.getDisplayMetrics());
		brushSize = pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	public void setLastBrushSize(float lastSize) {
		lastBrushSize = lastSize;
	}

	public float getLastBrushSize() {
		return lastBrushSize;
	}

	public void setErase(boolean isErase, String color) {
		// set erase true or false
		erase = isErase;
		if (erase) {
			invalidate();
			paintColor = Color.parseColor("#FFFFFFFF");
			drawPaint.setColor(paintColor);
		} else {
			invalidate();
			paintColor = Color.parseColor(color);
			drawPaint.setColor(paintColor);
		}
		// drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		// else drawPaint.setXfermode(null);
	}

	public void startNew() {
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

	public void addBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (!mPause) {
			if (mBitmap != null) {

				// Setting size of Source Rect
				mSrcRectF.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());

				// Setting size of Destination Rect
				mDestRectF.set(0, 0, getWidth(), getHeight());

				// Scaling the bitmap to fit the PaintView
				mMatrix.setRectToRect(mSrcRectF, mDestRectF, ScaleToFit.CENTER);

				// Drawing the bitmap in the canvas
				canvas.drawBitmap(mBitmap, mMatrix, mPaint);

			}
			canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			canvas.drawPath(drawPath, drawPaint);
			// Redraw the canvas
			invalidate();
		}
	}

	// Pause or resume onDraw method
	public void pause(boolean pause) {
		mPause = pause;
	}

	public void resetLayout() {
		mPaint.reset();
		drawPath.reset();
	}
}