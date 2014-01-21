package com.zgan.wifi;

import android.net.NetworkInfo.State;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.Manifest.permission;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText name;
	private EditText password;
	private Button connect;
	private TextView showInfo;
	private Handler handler;
	private StringBuilder builder;
	private WifiAdmin wifiAdmin;
	/** WIFI开关状态监听广播 **/
	private BroadcastReceiver receiver;
	/** WIFI设置成功状态监听广播 **/
	private BroadcastReceiver setReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		name=(EditText) findViewById(R.id.wifiNameEditText);
		password=(EditText) findViewById(R.id.wifiPasswordEditText);
		connect=(Button) findViewById(R.id.connectButton);
		showInfo=(TextView) findViewById(R.id.showInfoTextView);
		builder=new StringBuilder();
		handler=new Handler();
		wifiAdmin = new WifiAdmin(MainActivity.this);
		connect.setOnClickListener(new OnClickListener() {		
		@Override
		public void onClick(View v) {
			if (!wifiAdmin.getmWifiManager().isWifiEnabled()) {
				if (wifiAdmin.getmWifiManager().setWifiEnabled(true)) {
					registerWifiChangeBoradCast();
				}else{
					Toast.makeText(MainActivity.this, "wifi开启失败，请手动开启", Toast.LENGTH_LONG).show();
				}
			}else{
				String na=name.getText().toString();
				String pa=password.getText().toString();
				connectTargetWifi(na, pa);
			}
			
			//registerWifiChangeBoradCast();					    
		}
	});
		
	}
	
	private Handler cHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111)
			{
				Log.e("开启执行", "开启执行");
				String na=name.getText().toString();
				String pa=password.getText().toString();
				connectTargetWifi(na, pa);
			}			
		}
	};
	
	/**
	 * Description 注册WIFI状态改变监听广播
	 *//*
	private void registerSetWifiChangedBoradCast(){
		setReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if (wifiAdmin.GetWifiInfo().getSupplicantState()==SupplicantState.COMPLETED) {				
					Message msg=new Message();
					msg.what=0x222;
					cHandler.sendMessageDelayed(msg, 2000);
					unregisterSetWifiChangedBoradCast();
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		String broadcastPermission = permission.ACCESS_WIFI_STATE;
		registerReceiver(setReceiver, filter, broadcastPermission, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			}
		});
	}

	*//**
	 * Description 注销WIFI状态改变监听广播 
	 *//*
	private void unregisterSetWifiChangedBoradCast(){
		if (setReceiver != null) {
			unregisterReceiver(setReceiver);
		}
	}*/
	
	/**
	 * Description 注册WIFI状态改变监听广播
	 */
	private void registerWifiChangeBoradCast(){
		receiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// WIFI可用时连接到指定热点
				System.out.println( wifiAdmin.getmWifiManager().getWifiState());
				if (wifiAdmin.getmWifiManager().getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
					Log.i("TAG", "WIFI成功开启");					
					Message msg=new Message();
					msg.what=0x111;
					cHandler.sendMessageDelayed(msg, 5000);
					unregisterWifiChangeReceiver();
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
		String broadcastPermission = permission.ACCESS_WIFI_STATE;
		registerReceiver(receiver, filter, broadcastPermission, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				System.out.println("注册广播的msg"+msg.toString());
			}
		});
	}

	/**
	 * Description 注销WIFI状态改变监听广播 
	 */
	private void unregisterWifiChangeReceiver(){
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void connectTargetWifi(String na, String pa) {
		 
		 builder.append("初始时的wifi信息"+wifiAdmin.GetWifiInfo()+"\n");
		 wifiAdmin.openWifi(); 
		 wifiAdmin.startScan();

		 WifiConfiguration cofg3=wifiAdmin.CreateWifiInfo(na, pa, 3);
		 WifiConfiguration cofg2=wifiAdmin.CreateWifiInfo(na, pa, 2);
		 WifiConfiguration cofg1=wifiAdmin.CreateWifiInfo(na, "", 1);
		 if(wifiAdmin.AddNetwork(cofg3)
				 ||wifiAdmin.AddNetwork(cofg2)
				    ||wifiAdmin.AddNetwork(cofg1))
		 {
			 //registerSetWifiChangedBoradCast();    
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					String str=wifiAdmin.GetWifiInfo().getSupplicantState().toString();
					if(str.equals("COMPLETED"))
					{
						builder.append("链接成功后的wifi信息"+str+"\n");					    
					}else
					{
						builder.append("链接失败后的wifi信息"+str+"\n");
					}
					showInfo.setText(builder.toString());					
				}
			}, 3000);
		 }else
		 {
			 builder.append("链接出错！请手动链接");
			 showInfo.setText(builder.toString());
		 }
		
	}

	
}


