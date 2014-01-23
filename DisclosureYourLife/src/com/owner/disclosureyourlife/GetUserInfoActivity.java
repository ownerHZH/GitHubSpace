package com.owner.disclosureyourlife;

import com.owner.constant.AppConstants;
import com.owner.domain.JsonEntity;
import com.owner.domain.User;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.tools.GsonUtil;
import com.owner.tools.PhoneInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GetUserInfoActivity extends Activity {

	private Context context=GetUserInfoActivity.this;
	//private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_user_info);
		PhoneInfo siminfo = new PhoneInfo(context);  
        User u=new User();
        u.setDevice(siminfo.getDeviceId());
        u.setPhone(siminfo.getNativePhoneNumber()==null?
        		siminfo.getSimSerialNumber():siminfo.getNativePhoneNumber());
        //把用户的数据保存到数据库
        sendData(u);
        //gestureDetector=new GestureDetector(this, new Si);
	}
	
	public void sendData(User user)
	{
		HttpClientService svr = new HttpClientService(AppConstants.AddUserAction);
		//参数
		svr.addParameter("user",GsonUtil.getGson().toJson(user));
		
		HttpAndroidTask task = new HttpAndroidTask(context, svr,
				new HttpResponseHandler() {
					// 响应事件
					public void onResponse(Object obj) {
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,context,false);
						if (jsonEntity.getStatus() == 1) {
						} else if (jsonEntity.getStatus() == 0) {
							User u=GsonUtil.getGson().fromJson(jsonEntity.getData(), AppConstants.type_user);
							if(u!=null)
							{
                               Log.e("插入的用户==》》》", u.toString());
                               AppConstants.USER=u;
                               AppConstants.USER_ID=u.getUid();
							}				
						} 
					}					
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {						
					}
				});
		task.execute(new String[] {});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_get_user_info, menu);
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in2,R.anim.fade_out2);
		return super.onTouchEvent(event);
	}

}
