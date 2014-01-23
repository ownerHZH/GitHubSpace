package com.owner.disclosureyourlife;

import com.owner.tools.PhoneInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;

public class GetUserInfoActivity extends Activity {

	private Context context=GetUserInfoActivity.this;
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_user_info);
		
		PhoneInfo siminfo = new PhoneInfo(context);  
        System.out.println("getProvidersName:"+siminfo.getProvidersName());  
        System.out.println("getNativePhoneNumber:"+siminfo.getNativePhoneNumber());  
        System.out.println("------------------------");  
        System.out.println("getPhoneInfo:"+siminfo.getPhoneInfo());
        
        btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_get_user_info, menu);
		return false;
	}

}
