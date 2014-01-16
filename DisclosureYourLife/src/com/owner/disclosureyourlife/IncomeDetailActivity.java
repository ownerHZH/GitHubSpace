package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.List;

import com.owner.adapter.CatalogSpinnerAdapter;
import com.owner.constant.AppConstants;
import com.owner.domain.Income;
import com.owner.domain.IncomeComment;
import com.owner.domain.JsonEntity;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.tools.GsonUtil;
import com.owner.tools.MyProgressDialog;
import com.owner.tools.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	
	private Button leaveANoteButton;    //评论按钮
    private ListView leaveANoteListView;//评论显示列表
    private View leaveANoteListViewHeader;//评论显示列表的头
    private EditText leaveANoteEditText;//评论信息输入框
    private boolean isWriteNote=true;//初始化点击就是写信息
    private List<IncomeComment> incomeCommentList;
    private IncomeCommentAdapter incomeCommentAdapter;
    private int iid=0;
    private Handler handler;//用于上传发布的评论
    private IncomeComment ic;
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
			leaveANoteListView=(ListView) findViewById(R.id.leave_a_note_listView);
			leaveANoteListViewHeader=LayoutInflater.from(this).inflate(R.layout.consume_income_detail_list_header, null);
			handler=new Handler();
			iid=data.getIid();//这个详细页面所显示的数据id号
			clauses=(TextView) leaveANoteListViewHeader.findViewById(R.id.clauses);
			money=(TextView) leaveANoteListViewHeader.findViewById(R.id.money);
			
			clauses.setText(data.getName());
			money.setText("  "+data.getMoney()+"  ");
			
			leaveANoteButton=(Button) leaveANoteListViewHeader.findViewById(R.id.leave_a_note);
			leaveANoteButton.setOnClickListener(l);
			
			leaveANoteEditText=(EditText) leaveANoteListViewHeader.findViewById(R.id.noteEditText);
			leaveANoteEditText.setVisibility(View.GONE);
			leaveANoteListView.addHeaderView(leaveANoteListViewHeader);
			//列表显示信息
			incomeCommentList=new ArrayList<IncomeComment>();
			incomeCommentAdapter=new IncomeCommentAdapter(incomeCommentList);
			leaveANoteListView.setAdapter(incomeCommentAdapter);			
			//获取网络评论信息
			handler.postDelayed(receiveCommentRunnable, 100);	
			
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
			case R.id.leave_a_note:
				//评论点击按钮实现函数
				leaveANote();
				break;
			default:
				break;
			}
		}
	};
	
	//获取网络评论信息的runnable
		private Runnable receiveCommentRunnable=new Runnable() {
			
			@Override
			public void run() {
				HttpClientService svr = new HttpClientService(AppConstants.IncomeCommentRequestAction,false);
				//参数
				svr.addParameter("iid",iid);
				//svr.addParameter("pageno",0);
				//svr.addParameter("pagesize",0);
				
				HttpAndroidTask task = new HttpAndroidTask(context, svr,
						new HttpResponseHandler() {
							// 响应事件
							public void onResponse(Object obj) {
								//pdialog.stop();
								JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
										obj,context,false);
								if (jsonEntity.getStatus() == 1) {
									//Toast.makeText(context, "提交数据失败", 2).show();
								} else if (jsonEntity.getStatus() == 0) {
									//Toast.makeText(context, "提交成功", 2).show();
									List<IncomeComment> ccl=GsonUtil.getGson().fromJson( jsonEntity.getData(),
											  AppConstants.type_incomeCommentList);
									if(ccl!=null&&ccl.size()>0)
									{
										for(int i=0;i<ccl.size();i++)
										{
											incomeCommentList.add(ccl.get(i));
										}
										//ListView更新数据
										incomeCommentAdapter.notifyDataSetChanged();
										//为了让ScrollView里面的List显示完整
										//Utils.setListViewHeightBasedOnChildren(leaveANoteListView);
									}								
								}else
								{
									//Toast.makeText(context, "服务器出错", 2).show();
								}
							}
						}, new HttpPreExecuteHandler() {
							public void onPreExecute(Context context) {
								/*pdialog = new MyProgressDialog(context);
								pdialog.start("提交数据中...");*/
							}
						});
				task.execute(new String[] {});
				
			}
		};
		
		//上传发布的评论runnable
		private Runnable uploadCommitRunnable=new Runnable() {
			
			@Override
			public void run() {
				/*
				 * 上传访问的地址  
				 *   true 有File文件要上传  
				 *   false无File文件要上传
				 */
				HttpClientService svr = new HttpClientService(AppConstants.IncomeCommentUpLoadAction,false);
				//参数
				svr.addParameter("incomeComment",GsonUtil.getGson().toJson(ic));
				
				HttpAndroidTask task = new HttpAndroidTask(context, svr,
						new HttpResponseHandler() {
							// 响应事件
							public void onResponse(Object obj) {
								//pdialog.stop();
								JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
										obj,context,false);
								if (jsonEntity.getStatus() == 1) {
									//Toast.makeText(context, "提交数据失败", 2).show();
								} else if (jsonEntity.getStatus() == 0) {
									//Toast.makeText(context, "提交成功", 2).show();
								}else
								{
									//Toast.makeText(context, "服务器出错", 2).show();
								}
							}
						}, new HttpPreExecuteHandler() {
							public void onPreExecute(Context context) {
								/*pdialog = new MyProgressDialog(context);
								pdialog.start("提交数据中...");*/
							}
						});
				task.execute(new String[] {});
			}
		};
		
		//评论点击按钮实现函数
		private void leaveANote() {
			if(isWriteNote)
			{
				//第一次点击就是写信息 编辑框出现
				leaveANoteEditText.setVisibility(View.VISIBLE);
				isWriteNote=false;
				leaveANoteButton.setText(R.string.consume_detail_leave_a_note_button_button);
			}else {
				//不写的时候就是提交写的数据 同时隐藏编辑框
				String sNoteString=leaveANoteEditText.getText().toString();			
				isWriteNote=true;
				leaveANoteEditText.setText("");
				leaveANoteButton.setText(R.string.consume_detail_leave_a_note_button);
				leaveANoteEditText.setVisibility(View.GONE);
				if(sNoteString!=null&&sNoteString!=""&&!sNoteString.equals(null)&&!sNoteString.equals(""))
				{
					ic=new IncomeComment();
					ic.setIid(iid);
					ic.setComment(sNoteString);
					incomeCommentList.add(0,ic);//把评论加载到最定列表
					incomeCommentAdapter.notifyDataSetChanged();
					//为了让ScrollView里面的List显示完整
					//Utils.setListViewHeightBasedOnChildren(leaveANoteListView);			
					//这个评论信息的保存到数据库
					handler.postDelayed(uploadCommitRunnable, 2000);
				}
				
			}
		}
	
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
	
	//评论列表显示数据的adapter
		private final class IncomeCommentAdapter extends BaseAdapter
		{
			private LayoutInflater inflater;
			private List<IncomeComment> cclist;
			
			/**
			 * 构造函数
			 * @param context
			 */
			public IncomeCommentAdapter(List<IncomeComment> cclist)
			{
				inflater=LayoutInflater.from(context);
				this.cclist=cclist;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return cclist.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return cclist.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				 ViewHolder holder;        
		         if (convertView == null) {
		        	 
			          convertView = inflater.inflate(R.layout.simple_list_item_comment,null);	
			          holder = new ViewHolder();
			          
			         /*获取Item组件*/                    
			         holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
			         
			         convertView.setTag(holder);//给holder设置tag                   
		         }
		         else
		         {
		             holder = (ViewHolder)convertView.getTag();//获取holder                
		         }

		         /*Item组件赋值*/            
		         holder.title.setText(cclist.get(position).getComment());
		         
		         /**
		          * 给Item附上样式
		          */
		         if (cclist.size() == 1) {
		             convertView.setBackgroundResource(R.drawable.circle_list_single);
		         } else if (cclist.size() > 1) {
		             if (position == 0) {
		                 convertView.setBackgroundResource(R.drawable.circle_list_top);
		             } else if (position == (cclist.size() - 1)) {
		                 convertView.setBackgroundResource(R.drawable.circle_list_bottom);
		             } else {
		                 convertView.setBackgroundResource(R.drawable.circle_list_middle);
		             }
		         }
		           
		         return convertView;
		       }
			
			/**
			 * 组件内部类
			 * */
			public final class ViewHolder{
				public TextView title;   //列表显示的单项
			}
		}

}
