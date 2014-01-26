package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.List;

import com.owner.constant.AppConstants;
import com.owner.domain.JsonEntity;
import com.owner.domain.Spinner;
import com.owner.domain.User;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.tools.GsonUtil;
import com.owner.tools.PhoneInfo;
import com.owner.tools.SpinnerDb;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

public class GetUserInfoActivity extends Activity {

	private Context context=GetUserInfoActivity.this;
	private Handler handler;
	//private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_user_info);
		handler=new Handler();
		PhoneInfo siminfo = new PhoneInfo(context);  
        User u=new User();
        u.setDevice(siminfo.getDeviceId());
        u.setPhone(siminfo.getNativePhoneNumber()==null?
        		siminfo.getSimSerialNumber():siminfo.getNativePhoneNumber());
        //把用户的数据保存到数据库
        sendData(u);
        //获取服务器spinner中的数据，保存到本地数据库
        setSpinnerData();
	}
	
	//获取服务器spinner中的数据，保存到本地数据库
	private void setSpinnerData() {
		HttpClientService svr = new HttpClientService(AppConstants.SpinnerAction);
		//参数
		//svr.addParameter("user",GsonUtil.getGson().toJson(user));
		
		HttpAndroidTask task = new HttpAndroidTask(context, svr,
				new HttpResponseHandler() {
					// 响应事件
					public void onResponse(Object obj) {
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,context,false);
						if (jsonEntity.getStatus() == 1) {
						} else if (jsonEntity.getStatus() == 0) {
							List<Spinner> spinners=GsonUtil.getGson().fromJson(jsonEntity.getData(), AppConstants.type_spinnerList);
							if(spinners!=null&&spinners.size()>0)
							{                               
							   handler.postDelayed(runnable(spinners), 200);
							}				
						} 
					}										
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {						
					}
				});
		task.execute(new String[] {});
	}
	
	private Runnable runnable(final List<Spinner> spinners) {
		return new Runnable() {
			
			@Override
			public void run() {
				SpinnerDb db=new SpinnerDb(context);
				List<String> str1=new ArrayList<String>();
				List<String> str2=new ArrayList<String>();
				db.clearDb();
				for(int i=0;i<spinners.size();i++)
				{
					Spinner spinner=spinners.get(i);
					if(spinner.getSid()==1)
					{
						str1.add(spinner.getValue());
					}else if (spinner.getSid()==2) {
						str2.add(spinner.getValue());
					}
				}
				db.insert(1, str1);
				db.insert(2, str2);
				db.close();
			}
		};		
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
