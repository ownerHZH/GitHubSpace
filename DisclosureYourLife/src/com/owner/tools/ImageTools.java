package com.owner.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Tools for handler picture
 * 
 * @author owner
 * 
 */
public final class ImageTools {

	private static Dialog alertDialog;
	private static Builder alertDialogBuilder;
	private static ImageView imgv_large;
	/**
	 * Transfer drawable to bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap to drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * Input stream to bitmap
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Bitmap inputStreamToBitmap(InputStream inputStream)
			throws Exception {
		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * Byte transfer to bitmap
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * Byte transfer to drawable
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = null;
		if (byteArray != null) {
			ins = new ByteArrayInputStream(byteArray);
		}
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * Bitmap transfer to bytes
	 * 
	 * @param byteArray
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		byte[] bytes = null;
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	/**
	 * Drawable transfer to bytes
	 * 
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		byte[] bytes = bitmapToBytes(bitmap);
		;
		return bytes;
	}

	/**
	 * Base64 to byte[]
//	 */
//	public static byte[] base64ToBytes(String base64) throws IOException {
//		byte[] bytes = Base64.decode(base64);
//		return bytes;
//	}
//
//	/**
//	 * Byte[] to base64
//	 */
//	public static String bytesTobase64(byte[] bytes) {
//		String base64 = Base64.encode(bytes);
//		return base64;
//	}

	/**
	 * Create reflection images
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Get rounded corner images
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Resize the bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * Resize the drawable
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		matrix.postScale(sx, sy);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}
	
	/**
	 * Get images from SD card by path and the name of image
	 * @param photoName
	 * @return
	 */
	public static Bitmap getPhotoFromSDCard(String path,String photoName){
		Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" +photoName +".png");
		if (photoBitmap == null) {
			return null;
		}else {
			return photoBitmap;
		}
	}
	
	/**
	 * Check the SD card 
	 * @return
	 */
	public static boolean checkSDCardAvailable(){
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * Get image from SD card by path and the name of image
	 * @param fileName
	 * @return
	 */
	public static boolean findPhotoFromSDCard(String path,String photoName){
		boolean flag = false;
		
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName().split("\\.")[0];
					if (fileName.equals(photoName)) {
						flag = true;
					}
				}
			}else {
				flag = false;
			}
//			File file = new File(path + "/" + photoName  + ".jpg" );
//			if (file.exists()) {
//				flag = true;
//			}else {
//				flag = false;
//			}
			
		}else {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * Save image to the SD card 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()){
				dir.mkdirs();
			}
			
			File photoFile = new File(path , photoName + ".png");
			Log.e("拍照图片", photoFile.toString());
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
//						fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	
	/**
	 * Delete the image from SD card
	 * @param context
	 * @param path
	 * file:///sdcard/temp.jpg
	 */
	public static void deleteAllPhoto(String path){
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}
	
	public static void deletePhotoAtPathAndName(String path,String fileName){
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getName());
				if (files[i].getName().equals(fileName)) {
					files[i].delete();
				}
			}
		}
	}
	
	/**
	 * 将选择的或者拍照的照片显示在LinearLayout linear_uploads中
	 * @param con
	 * @param file_path_list  保存图片路径的列表
	 * @param linear_uploads  要显示图片的组件
	 */
	public static  void showImageInLinearLayout(final Context con,final List<String> file_path_list,
			final LinearLayout linear_uploads) {
		for(int i=0;i<file_path_list.size();i++)
		{
			ImageView imgv_ = new ImageView(con);
			//BitmapFactory.Options options = new BitmapFactory.Options();
			//options.inSampleSize = 64;//容量缩小的比例
			Bitmap photo = decodeFile(file_path_list.get(i));	
			imgv_.setImageBitmap(photo);
			if (photo != null && !photo.isRecycled()) {
				photo = null;
			}
			linear_uploads.addView(imgv_);
			android.view.ViewGroup.LayoutParams params = imgv_.getLayoutParams();
			params.height = 100;
			params.width = 100;
			imgv_.setLayoutParams(params);
			MarginLayoutParams mp = (MarginLayoutParams) imgv_.getLayoutParams();
			mp.setMargins(10, 10, 10, 10);
			imgv_.setLayoutParams(mp);

			Date date = new Date();
			imgv_.setId((int) date.getTime());
			imgv_.setClickable(true);
			imgv_.setLongClickable(true);

			imgv_.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(final View v) {
					final int x = linear_uploads.indexOfChild(v);

					// 判断照片是横拍，还是竖拍。begin
					ExifInterface exifInterface = null;
					// int degree = 0;
					try {
						exifInterface = new ExifInterface(file_path_list.get(x));
						int tag = exifInterface.getAttributeInt(
								ExifInterface.TAG_ORIENTATION, -1);

						// 判断照片是横拍，还是竖拍。end
						
						imgv_large = new ImageView(con);
						BitmapFactory.Options options_large = new BitmapFactory.Options();
						final Bitmap bitmap = BitmapFactory.decodeFile(
								file_path_list.get(x), options_large);
						// imgv_large.setImageBitmap(bitmap);
						if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
							// 设置旋转参数
							// 获取这个图片的宽和高
							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							// 定义预转换成的图片的宽度和高度
							// int newWidth = height;
							// int newHeight = width;
							// // 计算缩放率，新尺寸除原始尺寸
							// float scaleWidth = ((float) newWidth) / width;
							// float scaleHeight = ((float) newHeight) / height;
							// 创建操作图片用的matrix对象
							Matrix matrix = new Matrix();
							// 缩放图片动作
							// matrix.postScale(scaleWidth, scaleHeight);
							// 旋转图片 动作
							matrix.postRotate(90);
							// 创建新的图片
							Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0,
									0, width, height, matrix, true);
							imgv_large.setImageBitmap(resizedBitmap);
						}else{
							imgv_large.setImageBitmap(bitmap);
						}
						alertDialogBuilder = new AlertDialog.Builder(con)
								.setView(imgv_large)

								.setPositiveButton("删除",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (alertDialog != null) {
													alertDialog.dismiss();
												}
												imgv_large = null;
												linear_uploads.removeView(v);
												file_path_list.remove(x);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (alertDialog != null) {
													alertDialog.dismiss();
												}
												imgv_large = null;
											}
										});
						alertDialog = alertDialogBuilder.create();
						if (!alertDialog.isShowing()) {
							alertDialog.show();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}

			});
			imgv_ = null;
			//options = null;
		}
	}
	
	//防止内存泄露
	private static Bitmap decodeFile(String string){
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(string),null,o);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE=70;

	        //Find the correct scale value. It should be the power of 2.
	        int scale=1;
	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
	            scale*=2;

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(string), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}
	
	public static String bitmaptoString(Bitmap bitmap) 
	{
		// 将Bitmap转换成字符串
	    String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	public static Bitmap stringtoBitmap(String string) 
	{
		// 将字符串转换成Bitmap类型
	    Bitmap bitmap = null;
		try {
	    byte[] bitmapArray;
		bitmapArray = Base64.decode(string, Base64.DEFAULT);
		bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
		bitmapArray.length);
		} catch (Exception e) {
	       e.printStackTrace();
		}
	    return bitmap;
	}
}
