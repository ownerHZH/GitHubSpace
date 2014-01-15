package com.owner.disclosureyourlife;


import com.owner.sliding.view.SlidingMenu;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private TabHost tabHost;
	private TabWidget tabWidget;
	private Context context;
	
	private Button btn;
	private SlidingMenu mSlidingMenu;
	
	private Button consume,income,embarrass;
	private boolean isSet=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		setContentView(R.layout.main);	
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setAlignScreenWidth((dm.widthPixels / 5) * 2);
		
		View rightView=getLayoutInflater().inflate(R.layout.activity_upload_own, null);
		View centerView=getLayoutInflater().inflate(R.layout.activity_center_main, null);

		mSlidingMenu.setRightView(rightView);
		mSlidingMenu.setCenterView(centerView);
		
		
		context=MainActivity.this;
		
		btn=(Button) centerView.findViewById(R.id.upload);
		
		initTabHost(savedInstanceState,centerView);   //初始化TabHost
        
        tabHost.setCurrentTab(0);
        
        consume=(Button) rightView.findViewById(R.id.consumeButton);
		income=(Button) rightView.findViewById(R.id.incomeButton);
		embarrass=(Button) rightView.findViewById(R.id.embarrassMakeSureButton);
				
		consume.setOnClickListener(l);
		income.setOnClickListener(l);
		embarrass.setOnClickListener(l);

    	
		btn.setOnClickListener(l);
	}
	
	//晒自己按钮监听器
	private OnClickListener l=new OnClickListener() {
		Intent intent=null;
		@Override
		public void onClick(View v) {			
			switch (v.getId()) {
			case R.id.upload:
				if(isSet)
				{
					btn.setBackgroundResource(R.drawable.align_just);
					isSet=false;
				}else {
					btn.setBackgroundResource(R.drawable.align_just_2);
					isSet=true;
				}				
				mSlidingMenu.showRightView();
				break;
			case R.id.consumeButton:
				intent=new Intent(context,ConsumeDetailActivity.class);
				startActivity(intent);
				break;
			case R.id.incomeButton:
				intent=new Intent(context,IncomeDetailActivity.class);
				startActivity(intent);
				break;
			case R.id.embarrassMakeSureButton:
				intent=new Intent(context,EmbarrassDetailActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};
	
	public void initTabHost(Bundle savedInstanceState, View centerView) {
		
		tabHost = (TabHost)centerView.findViewById(R.id.tabhost);
		//为了用Intent需要调用下面三句
		LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);  
        mLocalActivityManager.dispatchCreate(savedInstanceState);  
        tabHost.setup(mLocalActivityManager);
        
        //直接调用FrameLayout中的组件是直接调用下面一句
        //tabHost.setup();

        tabWidget = tabHost.getTabWidget();
                        
        tabHost.addTab(tabHost
                .newTabSpec("tab1")
                .setIndicator("消费排名",null)
                .setContent(new Intent(context,ConsumeListActivity.class)));//R.id.contentlist new Intent(context,ConsumeActivity.class)

        tabHost.addTab(tabHost
                .newTabSpec("tab2")
                .setIndicator("收入排名",null)
                .setContent(new Intent(context,IncomeListActivity.class)));//R.id.contentlist new Intent(context,IncomeActivity.class)
        tabHost.addTab(tabHost
                .newTabSpec("tab3")
                .setIndicator("糗事儿排名",null)
                .setContent(new Intent(context,EmbarrassListActivity.class)));
        //设置TabHost标题导航栏的高度和字体
        setTabViewParas();
        setTabBackground();//初始化导航背景
        tabHost.setOnTabChangedListener(new TabChangeListener());
	}
	
	//TabHost监听器
	public class TabChangeListener implements OnTabChangeListener {
		
		public void onTabChanged(String str) {
			setTabBackground();	        	        			
		  }
	}
	
	//设置TabHost标题导航栏的高度和字体
	public void setTabViewParas() {
		int count = tabWidget.getChildCount();
        for (int i = 0; i < count; i++) {
	        View view = tabWidget.getChildTabViewAt(i);   
	        view.getLayoutParams().height = 80; //tabWidget.getChildAt(i)
	        //view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
	        final TextView tv = (TextView) view.findViewById(android.R.id.title);
	        tv.setTextSize(20);
	        tv.setTextColor(this.getResources().getColorStateList(
	          android.R.color.black));
         }
	}
	
	//设置TabHost改变时导航栏样式的改变
	public void setTabBackground() {
		int count = tabWidget.getChildCount();
		for(int i=0;i<count;i++)
		{
			View v = tabWidget.getChildAt(i); 
	        if (tabHost.getCurrentTab() == i)
	        { 
		        v.setBackgroundColor(Color.WHITE); 
		        //v.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat)); 
	        } else 
	        { 
	           v.setBackgroundColor(Color.GRAY); 
	        } 
		}
	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
