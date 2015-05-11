package com.example.flashcard;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.example.flashcard.model.utils.DrawingView;
import com.example.flashcard.model.utils.PaintView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DrawingActivity extends Activity implements OnClickListener {
	private PaintView drawView;
	// private ImageButton currPaint;
	private float smallBrush, mediumBrush, largeBrush;
	private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, backBtn,
			btnOpenGalary, btnOpenCamera;
	Uri imageUri = null;
	private static int RESULT_LOAD_IMAGE = 1;
	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	DrawingActivity drawActivity = null;
	Bitmap bmCamera = null;
	DisplayImageOptions options;
	private static final int CAMERA_REQUEST = 3;
	int mWidth;
	int mHeight;
	LinearLayout drawing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawing);
		mWidth = mHeight = 0;
		drawActivity = this;
		drawing = (LinearLayout) findViewById(R.id.drawing);
		drawView = new PaintView(this);
		drawing.addView(drawView);

		LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
		currPaint = (ImageButton) paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));

		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		drawBtn = (ImageButton) findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);
		eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);
		newBtn = (ImageButton) findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);
		saveBtn = (ImageButton) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		backBtn = (ImageButton) findViewById(R.id.new_btnback);
		backBtn.setOnClickListener(this);
		btnOpenGalary = (ImageButton) findViewById(R.id.btnOpenGalary);
		btnOpenGalary.setOnClickListener(this);
		btnOpenCamera = (ImageButton) findViewById(R.id.btnOpenCamera);
		btnOpenCamera.setOnClickListener(this);
		drawView.setBrushSize(mediumBrush);
		initImageLoader(this);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.noimages)
				.showImageForEmptyUri(R.drawable.noimages)
				.showImageOnFail(R.drawable.noimages).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	public static int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private Bitmap getBitmapFromUri(Uri data) {
		Bitmap bitmap = null;

		// Starting fetch image from file
		InputStream is = null;
		try {

			is = getContentResolver().openInputStream(data);

			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			// BitmapFactory.decodeFile(path, options);
			BitmapFactory.decodeStream(is, null, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, mWidth,
					mHeight);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;

			is = getContentResolver().openInputStream(data);

			bitmap = BitmapFactory.decodeStream(is, null, options);

			if (bitmap == null) {
				Toast.makeText(getBaseContext(), "Image is not Loaded",
						Toast.LENGTH_SHORT).show();
				return null;
			}

			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		mWidth = drawView.getWidth();
		mHeight = drawView.getHeight();
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(30 * 1024 * 1024)
				// 30 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void paintClicked(View view) {
		// use chosen color
		drawView.setErase(false, currPaint.getTag().toString());
		drawView.setBrushSize(drawView.getLastBrushSize());
		if (view != currPaint) {
			// update color
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			drawView.setColor(color);

			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(
					R.drawable.paint));
			currPaint = (ImageButton) view;

		}

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.draw_btn) {
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			ImageButton smallBtn = (ImageButton) brushDialog
					.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					drawView.setErase(false, currPaint.getTag().toString());
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton) brushDialog
					.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					drawView.setErase(false, currPaint.getTag().toString());
					brushDialog.dismiss();
				}
			});

			ImageButton largeBtn = (ImageButton) brushDialog
					.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(largeBrush);
					drawView.setLastBrushSize(largeBrush);
					drawView.setErase(false, currPaint.getTag().toString());
					brushDialog.dismiss();
				}
			});
			brushDialog.show();
		} else if (view.getId() == R.id.erase_btn) {
			// switch to erase - choose size
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.brush_chooser);

			ImageButton smallBtn = (ImageButton) brushDialog
					.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true, currPaint.getTag().toString());
					drawView.setBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton) brushDialog
					.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true, currPaint.getTag().toString());
					drawView.setBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton largeBtn = (ImageButton) brushDialog
					.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setErase(true, currPaint.getTag().toString());
					drawView.setBrushSize(largeBrush);
					// drawView.setBrushSize(smallBrush);
					// drawView.setLastBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});

			brushDialog.show();
		} else if (view.getId() == R.id.new_btn) {
			// new button
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog
					.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							drawView.startNew();
							dialog.dismiss();
						}
					});
			newDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			newDialog.show();
		}

		else if (view.getId() == R.id.save_btn) {
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// save drawing
							drawView.setDrawingCacheEnabled(true);
							String imgSaved = MediaStore.Images.Media
									.insertImage(getContentResolver(), drawView
											.getDrawingCache(), UUID
											.randomUUID().toString() + ".png",
											"drawing");

							if (imgSaved != null) {
								Toast savedToast = Toast.makeText(
										getApplicationContext(),
										"Drawing saved to Gallery!",
										Toast.LENGTH_SHORT);
								savedToast.show();
							} else {
								Toast unsavedToast = Toast.makeText(
										getApplicationContext(),
										"Oops! Image could not be saved.",
										Toast.LENGTH_SHORT);
								unsavedToast.show();
							}
							drawView.destroyDrawingCache();
						}
					});
			saveDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			saveDialog.show();

		} else if (view.getId() == R.id.new_btnback) {
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			this.finish();
		} else if (view.getId() == R.id.btnOpenGalary) {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
		} else if (view.getId() == R.id.btnOpenCamera) {
			 openCamera();
//			Intent cameraIntent = new Intent(
//					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
	}

	String imgURI = "";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			// imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			// drawView.setbac
			Uri uri = data.getData();
			Bitmap bitmap = getBitmapFromUri(uri);
			drawing.removeAllViews();
			drawing.invalidate();
			drawView = new PaintView(this);
			drawing.addView(drawView);
			
			drawView.addBitmap(bitmap);
			drawView.invalidate();
			// drawView.setImageBitmap(decodeImage(picturePath));//setBackgroundView(decodeImage(picturePath));

		} else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

			if (resultCode == RESULT_OK) {

				/*********** Load Captured Image And Data Start ****************/

				 String imageId = convertImageUriToFile(
				 imageUri,drawActivity);

				// Create and excecute AsyncTask to load capture image

				// new LoadImagesFromSDCard().execute(""+imageId);
//				Uri uri = data.getData();
				Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageId);
				imgURI = getPath(uri);
				/************** Decode an input stream into a bitmap. *********/
				// Bitmap bitmap = null;
				// try {
				// bitmap =
				// BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
				// } catch (FileNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// if (bitmap != null) {
				//
				// /********* Creates a new bitmap, scaled from an existing
				// bitmap. ***********/
				//
				// Bitmap newBitmap =
				// Bitmap.createScaledBitmap(bitmap,bitmap.getHeight(),
				// bitmap.getWidth(), true);
				//
				// bitmap.recycle();
				//
				// if (newBitmap != null) {
				//
				// bmCamera = newBitmap;
				// }
				// }
				Bitmap bitmap = getBitmapFromUri(uri);
				drawing.removeAllViews();

				drawing.invalidate();
				drawView = new PaintView(this);
				drawing.addView(drawView);
				drawView.addBitmap(bitmap);
				drawView.invalidate();
				/*********** Load Captured Image And Data End ****************/

			} else if (resultCode == RESULT_CANCELED) {

				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private Bitmap decodeImage(String picturePath) {
		try {
			File file = new File(picturePath);
			// Get image size
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, opts);

			// The new size we want to scale to
			final int MIN_SIZE = 70;

			// Find the correct scale value.
			int scale = 1;
			while (((opts.outWidth / scale) >> 1) >= MIN_SIZE
					&& ((opts.outHeight / scale) >> 1) >= MIN_SIZE) {
				scale <<= 1;
			}

			BitmapFactory.Options opts2 = new BitmapFactory.Options();
			opts2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(file), null,
					opts2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		this.finish();
	}

	public void openCamera() {
		String fileName = "Camera_Example.jpg";

		// Create parameters for Intent with filename

		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);

		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");

		// imageUri is the current activity attribute, define and save it for
		// later usage

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@SuppressWarnings("deprecation")
	public static String convertImageUriToFile(Uri imageUri, Activity activity) {

		Cursor cursor = null;
		int imageID = 0;

		try {

			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = activity.managedQuery(

			imageUri, // Get data for specific image URI
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null // Order-by clause (ascending by name)

					);

			// Get Query Data

			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int columnIndexThumb = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int file_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			// int orientation_ColumnIndex = cursor.
			// getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/

			if (size == 0) {

				// imageDetails.setText("No Image");
			} else {

				int thumbID = 0;
				if (cursor.moveToFirst()) {

					/**************** Captured image details ************/

					/***** Used to show image on view in LoadImagesFromSDCard class ******/
					imageID = cursor.getInt(columnIndex);

					thumbID = cursor.getInt(columnIndexThumb);

					String Path = cursor.getString(file_ColumnIndex);

					// String orientation =
					// cursor.getString(orientation_ColumnIndex);

					String CapturedImageDetails = " CapturedImageDetails : \n\n"
							+ " ImageID :"
							+ imageID
							+ "\n"
							+ " ThumbID :"
							+ thumbID + "\n" + " Path :" + Path + "\n";

					// Show Captured Image detail on activity
					// imageDetails.setText( CapturedImageDetails );

				}
			}
		} finally {
			if (cursor != null) {
				// cursor.close();
			}
		}

		// Return Captured Image ImageID ( By this ImageID Image will load from
		// sdcard )

		return "" + imageID;
	}

	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

		private ProgressDialog Dialog = new ProgressDialog(DrawingActivity.this);

		Bitmap mBitmap;

		protected void onPreExecute() {
			/****** NOTE: You can call UI Element here. *****/

			// Progress Dialog
			Dialog.setMessage(" Loading image from Sdcard..");
			Dialog.show();
		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;

			try {

				/**
				 * Uri.withAppendedPath Method Description Parameters baseUri
				 * Uri to append path segment to pathSegment encoded path
				 * segment to append Returns a new Uri based on baseUri with the
				 * given segment appended to the path
				 */

				uri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
								+ urls[0]);

				/************** Decode an input stream into a bitmap. *********/
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));

				if (bitmap != null) {

					/********* Creates a new bitmap, scaled from an existing bitmap. ***********/

					newBitmap = Bitmap.createScaledBitmap(bitmap,
							bitmap.getHeight(), bitmap.getWidth(), true);

					bitmap.recycle();

					if (newBitmap != null) {

						mBitmap = newBitmap;
					}
				}
			} catch (IOException e) {
				// Error fetching image, try to recover

				/********* Cancel execution of this task. **********/
				cancel(true);
			}

			return null;
		}

		protected void onPostExecute(Void unused) {

			// NOTE: You can call UI Element here.

			// Close progress dialog
			Dialog.dismiss();

			if (mBitmap != null) {
				// Set Image to ImageView
				bmCamera = mBitmap;
				// drawView.setBackgroundView(mBitmap);
			}

		}

	}

}
