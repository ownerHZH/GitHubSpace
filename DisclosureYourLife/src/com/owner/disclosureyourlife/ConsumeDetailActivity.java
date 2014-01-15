package com.owner.disclosureyourlife;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.owner.adapter.CatalogSpinnerAdapter;
import com.owner.constant.AppConstants;
import com.owner.domain.Consume;
import com.owner.domain.JsonEntity;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.tools.GsonUtil;
import com.owner.tools.MyProgressDialog;
import com.owner.tools.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumeDetailActivity extends Activity {

	private final int FLAG=0;
	private Button back;//返回按钮
	private TextView title;//标题
	private Spinner catalogSpinner;//内容选择项
	private LinearLayout itemContainer;//装载动态添加的容器
	private Context context;///全局的内容
	private List<String> items;//选择项的内容
	private Button submit;//提交按钮
    private ViewStub viewStub;
    private CatalogSpinnerAdapter catalogAdapter;
    
    private TextView clauses;
    private TextView money;
    private MyProgressDialog pdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		setContentView(R.layout.activity_consume_detail);
		context=ConsumeDetailActivity.this;
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.embarrassTitle);
		title.setText(getString(R.string.consume_title));
		back.setOnClickListener(l);
		
		Intent intent=getIntent();
		if(intent.hasExtra("data"))//显示详细的页面
		{
			Consume data=(Consume) intent.getSerializableExtra("data");
			Log.e("data数据", data.toString());
			//detail.setText(data);
			title.setText(getString(R.string.consume_detail));
			viewStub=(ViewStub) findViewById(R.id.consumeDetailLayout);
			viewStub.inflate();
			
			clauses=(TextView) findViewById(R.id.clauses);
			money=(TextView) findViewById(R.id.money);
			
			clauses.setText(data.getName());
			money.setText("  "+data.getMoney()+"  ");
		}else {//上传自己的页面显示
			viewStub=(ViewStub) findViewById(R.id.uploadLayout);
			viewStub.inflate();
			
			catalogSpinner=(Spinner) findViewById(R.id.catalogspinner);
			itemContainer=(LinearLayout) findViewById(R.id.itemContainer);
			submit=(Button) findViewById(R.id.submitButton);
			
			items=new ArrayList<String>();
			items.add("选总共，就不分小项");
			items.add("买衣物");
			items.add("买食物");
			items.add("送礼物");
			items.add("交通");
			items.add("总共");
			
			catalogSpinner.setAdapter(new CatalogSpinnerAdapter(context,items,FLAG));			
			catalogSpinner.setOnItemSelectedListener(itemSelected);
			submit.setOnClickListener(l);
		}
				
		
	}
	
	//Spinner组件选择监视器
	private OnItemSelectedListener itemSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if(position!=0)
			{
				//根据Spinner的选择动态创建组建
				Utils.createModule(context,items,position,itemContainer,FLAG);
				submit.setVisibility(View.VISIBLE);
			}
			
		}		

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	//按钮单机监视器
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.submitButton:
				//获得动态创建组件中的详细
				if(itemContainer.getChildCount()>1)
				{
					upLoadData();
				}			
				break;
			default:
				break;
			}
		}
	};
	
	//点击确定按钮上传字段
	public void upLoadData()
	{		
		List<Consume> consumes=new ArrayList<Consume>();//上传的一个列表
		parseModule(consumes);//把提交的数据封装到list当中
		if(consumes!=null&&consumes.size()>0)
		{
			/*
			 * 上传访问的地址  
			 *   true 有File文件要上传  
			 *   false无File文件要上传
			 */
			HttpClientService svr = new HttpClientService(AppConstants.ConsumeUpLoadAction,true);
			//参数
			svr.addParameter("consumes",GsonUtil.getGson().toJson(consumes));
			
			HttpAndroidTask task = new HttpAndroidTask(context, svr,
					new HttpResponseHandler() {
						// 响应事件
						public void onResponse(Object obj) {
							pdialog.stop();
							JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
									obj,context,false);
							if (jsonEntity.getStatus() == 1) {
								Toast.makeText(context, "提交数据失败", 2).show();
							} else if (jsonEntity.getStatus() == 0) {
								Toast.makeText(context, "提交成功", 2).show();
								finish();
							}else
							{
								Toast.makeText(context, "服务器出错", 2).show();
							}
						}
					}, new HttpPreExecuteHandler() {
						public void onPreExecute(Context context) {
							pdialog = new MyProgressDialog(context);
							pdialog.start("提交数据中...");
						}
					});
			task.execute(new String[] {});
		}else{
			Toast.makeText(context, "无有价值的数据可上报", 2).show();
		}		
	}

	//把提交的数据封装到list当中
	private void parseModule(List<Consume> consumes) {
		int uid=AppConstants.USER_ID;
		for(int i=0;i<itemContainer.getChildCount()-1;i++)
		{
			Consume consume=new Consume();//把一条小项封装为一个实体
			
			LinearLayout linearLayout=(LinearLayout)itemContainer.getChildAt(i);
			TextView textView=(TextView)linearLayout.getChildAt(0);//索引0 是选择上传的项名字
			EditText editText=(EditText)linearLayout.getChildAt(1);//索引1 是选择上传的项的钱数
			/**
			 * 由于这个子段的格式是{ xxx: ￥ }
			 * 为了得到  xxx
			 */
			String name=textView.getText().toString().split(":")[0].trim();//得到项的名称
			String money=editText.getText().toString().trim();//得到输入的数量
			//封装
			if(money!=null&&money!=""&&!money.equals(null)&&!money.equals(""))
			{
				consume.setUid(uid);
				consume.setName(name);
				consume.setMoney(Double.valueOf(money));
				consumes.add(consume);//加入列表
			}			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consume_detail, menu);
		return false;
	}

}
