package com.owner.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GetImgFromServer extends AsyncTask<Object, Object, Object>{
	private ImageView imageview;
	public GetImgFromServer(ImageView img)
	{
		imageview=img;
	}
	@Override
	protected Bitmap doInBackground(Object... arg0) {
		URL myFileUrl = null; 
		Bitmap bitmap = null; 
		try { 
		myFileUrl = new URL(arg0[0].toString()); 
		} catch (MalformedURLException e) { 
		e.printStackTrace(); 
		} 
		try { 
		HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); 
		conn.setDoInput(true); 
		conn.connect(); 
		InputStream is = conn.getInputStream(); 
		bitmap = BitmapFactory.decodeStream(is); 
		is.close(); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} 
		return bitmap;
	}
	@Override
	protected void onPostExecute(Object result) {
		imageview.setImageBitmap((Bitmap)result);
	} 
}
