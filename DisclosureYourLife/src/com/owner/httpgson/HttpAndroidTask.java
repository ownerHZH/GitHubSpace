package com.owner.httpgson;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class HttpAndroidTask extends AsyncTask<String, Integer, String> {
	// ���캯��
	HttpClientService service;
	HttpResponseHandler handler;
	Context context;
	ProgressDialog pdialog;
	HttpPreExecuteHandler preExecuteHandler;

	public HttpAndroidTask(Context context, HttpClientService svr,
			HttpResponseHandler h) {
		service = svr;
		handler = h;
		this.context = context;
	}

	public HttpAndroidTask(Context context, HttpClientService svr,
			HttpResponseHandler h, HttpPreExecuteHandler preExecuteHandler) {
		service = svr;
		handler = h;
		this.context = context;
		this.preExecuteHandler = preExecuteHandler;
	}

	protected void onPreExecute() {
		if (preExecuteHandler != null) {
			preExecuteHandler.onPreExecute(context);
		}
		super.onPreExecute();
	}

	protected String doInBackground(String... params) {
		// ��������
		String result = null;
		result = service.getResponse();
		return result;

	}

	protected void onPostExecute(String result) {
		if (pdialog != null) {
			pdialog.cancel();
		}

		if (result == null) {
			handler.onResponse(null);
			return;
		}
		handler.onResponse(result);
	}

	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
