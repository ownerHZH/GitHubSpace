package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.List;

import com.owner.adapter.CatalogSpinnerAdapter;
import com.owner.constant.AppConstants;
import com.owner.domain.Consume;
import com.owner.domain.Income;
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
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class IncomeDetailActivity extends Activity {

	private final int FLAG=1;
	private Button back;
	private TextView title;
	private Context context;
	
	private ViewStub viewStub;
	private Spinner catalogSpinner;//内容选择项
	private LinearLayout itemContainer;//装载动态添加的容器
	private List<String> items;//选择项的内容
	private Button submit;//提交按钮
	
	private TextView clauses;
	private TextView money;
	private MyProgressDialog pdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		setContentView(R.layout.activity_income_detail);
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.embarrassTitle);
		context=IncomeDetailActivity.this;
		title.setText("晒自己的收入");
		back.setOnClickListener(l);
		
		Intent intent=getIntent();	
		if(intent.hasExtra("data"))
		{
			Income data=(Income) intent.getSerializableExtra("data");
			title.setText("收入详细");
			viewStub=(ViewStub) findViewById(R.id.incomeDetailLayout);
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
			items.add("一工资");
			items.add("二工资");
			items.add("三工资");
			items.add("其他");
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
					Utils.createModule(context,items,position,itemContainer,FLAG);
					submit.setVisibility(View.VISIBLE);
				}
				
			}		

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
		
		
	
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
		List<Income> incomes=new ArrayList<Income>();//上传的一个列表
		try {
			parseModule(incomes);//把提交的数据封装到list当中
			if(incomes!=null&&incomes.size()>0)
			{
				/*
				 * 上传访问的地址  
				 *   true 有File文件要上传  
				 *   false无File文件要上传
				 */
				HttpClientService svr = new HttpClientService(AppConstants.IncomeUpLoadAction,true);
				//参数
				svr.addParameter("incomes",GsonUtil.getGson().toJson(incomes));
				
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
				Toast.makeText(context, "无有价值的数据可上传", 2).show();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//把提交的数据封装到list当中
	private void parseModule(List<Income> incomes) throws Exception {
		int uid=AppConstants.USER_ID;
		for(int i=0;i<itemContainer.getChildCount()-1;i++)
		{
			Income income=new Income();//把一条小项封装为一个实体
			
			LinearLayout linearLayout=(LinearLayout)itemContainer.getChildAt(i);
			TextView textView=(TextView)linearLayout.getChildAt(0);//索引0 是选择上传的项名字
			EditText editText=(EditText)linearLayout.getChildAt(1);//索引1 是选择上传的项的钱数
			/**
			 * 由于这个子段的格式是{ xxx: ￥ }
			 * 为了得到  xxx
			 */
			String name=textView.getText().toString().split(":")[0].trim();//得到项的名称
			String money=editText.getText().toString().trim();//得到输入的数量
			if(money!=null&&money!=""&&!money.equals(null)&&!money.equals(""))
			{
				//封装
				income.setUid(uid);
				income.setName(name);
				income.setMoney(Double.valueOf(money));
				incomes.add(income);//加入列表
			}			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.income_detail, menu);
		return false;
	}

}
