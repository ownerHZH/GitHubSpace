package com.owner.disclosureyourlife;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.owner.adapter.CatalogSpinnerAdapter;
import com.owner.constant.AppConstants;
import com.owner.domain.Consume;
import com.owner.domain.ConsumeComment;
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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
    private TextView clauses;
    private TextView money;
    private MyProgressDialog pdialog;
    
    private Button leaveANoteButton;    //评论按钮
    private ListView leaveANoteListView;//评论显示列表
    private View leaveANoteListViewHeader;//评论显示列表的头
    private EditText leaveANoteEditText;//评论信息输入框
    private boolean isWriteNote=true;//初始化点击就是写信息
    private List<ConsumeComment> consumeCommentList;
    private ConsumeCommentAdapter consumeCommentAdapter;
    private int cid=0;
    private Handler handler;//用于上传发布的评论
    private ConsumeComment cc;
    //private Handler rHandler;//用于初始时下载所对应的评论信息
    
    private CatalogSpinnerAdapter catalogSpinnerAdapter;
    private PopupWindow mPopupWindow=null;
    String zItemName,zItemMoney;//自定义的名称与金钱数
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
			handler=new Handler();
			//detail.setText(data);
			title.setText(getString(R.string.consume_detail));
			viewStub=(ViewStub) findViewById(R.id.consumeDetailLayout);
			viewStub.inflate();
			leaveANoteListView=(ListView) findViewById(R.id.leave_a_note_listView);
			leaveANoteListViewHeader=LayoutInflater.from(this).inflate(R.layout.consume_income_detail_list_header, null);
			cid=data.getCid();//这个详细页面所显示的数据id号
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
			consumeCommentList=new ArrayList<ConsumeComment>();	
			consumeCommentAdapter=new ConsumeCommentAdapter(consumeCommentList);
			leaveANoteListView.setAdapter(consumeCommentAdapter);
			//获取网络评论信息
			handler.postDelayed(receiveCommentRunnable, 100);			
			
		}else {//上传自己的页面显示
			viewStub=(ViewStub) findViewById(R.id.uploadLayout);
			viewStub.inflate();
			
			catalogSpinner=(Spinner) findViewById(R.id.catalogspinner);
			itemContainer=(LinearLayout) findViewById(R.id.itemContainer);
			submit=(Button) findViewById(R.id.submitButton);
			
			items=new ArrayList<String>();
			items.add("月消费项目");
			items.add("+自定义+");
			items.add("买衣物");
			items.add("买食物");
			items.add("送礼物");
			items.add("交通费");
			items.add("总消费");
			catalogSpinnerAdapter=new CatalogSpinnerAdapter(context,items,FLAG);
			catalogSpinner.setAdapter(catalogSpinnerAdapter);			
			catalogSpinner.setOnItemSelectedListener(itemSelected);
			submit.setOnClickListener(l);
		}
				
		
	}
	
	//Spinner组件选择监视器
	private OnItemSelectedListener itemSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			catalogSpinner.setSelection(0);
			if(position!=0)				
			{   
				if(items.get(position).equals("+自定义+"))
				{
					showDialog();					
				}else
				{
					//根据Spinner的选择动态创建组建
					Utils.createModule(context,items,position,itemContainer,FLAG);						
				}	
				submit.setVisibility(View.VISIBLE);
			}
		}		

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void showDialog()
	{
		View view=getLayoutInflater()
				.inflate(R.layout.consume_spinner_add_item_layout,null);
		final EditText nameEditText=(EditText) view.findViewById(R.id.add_item_name_editText);
		final EditText numberEditText=(EditText) view.findViewById(R.id.add_item_number_editText);
		final Builder alertDialogBuilder=new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("自定义信息")
          .setView(view)
          .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
        	  
           @Override
           public void onClick(DialogInterface dialoginterface, int i){ 
        	   List<String> datas=new ArrayList<String>();
			   datas.add(0, nameEditText.getText().toString());
			   datas.add(1, numberEditText.getText().toString());
			   Utils.createModule(context,datas,0,itemContainer,0x11);
            } 
           }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	//显示弹出窗口
	/*private void showPopWindow() {
		
		View view=getLayoutInflater()
				.inflate(R.layout.consume_spinner_add_item_layout,null);
		if(mPopupWindow==null)
		{
			mPopupWindow=new PopupWindow(view,
					LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		}
		mPopupWindow.setFocusable(true);//设置获取焦点		
		//mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//为了让在外点击消失弹出框
		//mPopupWindow.setOutsideTouchable(true);// 设置允许在外点击消失 
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);//设置弹窗进入与退出的动画效果
		mPopupWindow.showAtLocation(catalogSpinner, Gravity.CENTER, 0, 0);//showAtLocation(null, Gravity.CENTER, 0, 0);
		
		//按钮
		Button okBtn=(Button) view.findViewById(R.id.add_item_button_ok);
		Button okCancel=(Button) view.findViewById(R.id.add_item_button_cancel);
		EditText nameEditText=(EditText) view.findViewById(R.id.add_item_name_editText);
		EditText numberEditText=(EditText) view.findViewById(R.id.add_item_number_editText);
		okCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				mPopupWindow=null;
			}
		});
	}*/
	
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
			HttpClientService svr = new HttpClientService(AppConstants.ConsumeCommentRequestAction,false);
			//参数
			svr.addParameter("cid",cid);
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
								List<ConsumeComment> ccl=GsonUtil.getGson().fromJson( jsonEntity.getData(),
										  AppConstants.type_consumeCommentList);
								if(ccl!=null&&ccl.size()>0)
								{
									for(int i=0;i<ccl.size();i++)
									{
										consumeCommentList.add(ccl.get(i));
									}									
									//ListView更新数据
									
									consumeCommentAdapter.notifyDataSetChanged();
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
			HttpClientService svr = new HttpClientService(AppConstants.ConsumeCommentUpLoadAction,false);
			//参数
			svr.addParameter("consumeComment",GsonUtil.getGson().toJson(cc));
			
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
				cc=new ConsumeComment();
				cc.setCid(cid);
				cc.setComment(sNoteString);
				cc.setDate(new Date());
				consumeCommentList.add(0,cc);//把评论加载到最定列表
				consumeCommentAdapter.notifyDataSetChanged();
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

	//评论列表显示数据的adapter
	private final class ConsumeCommentAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		private List<ConsumeComment> cclist;
		
		/**
		 * 构造函数
		 * @param context
		 */
		public ConsumeCommentAdapter(List<ConsumeComment> cclist)
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
	         Date date=cclist.get(position).getDate();
	         date=(date==null?new Date():date);
	         DateFormat d = DateFormat.getDateTimeInstance();
	         String str = d.format(date);
	         String htmlStr="<span style='color: red; text-align: center;font-size: 10'>"+str+"</span>";
	         holder.title.setText(Html.fromHtml(htmlStr)+"\r\n"
	        		 +cclist.get(position).getComment());
	         
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
		
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			//Utils.setListViewHeightBasedOnChildren(leaveANoteListView);
		}

		/**
		 * 组件内部类
		 * */
		public final class ViewHolder{
			public TextView title;   //列表显示的单项
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consume_detail, menu);
		return false;
	}

}
