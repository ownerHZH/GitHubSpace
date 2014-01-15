package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.List;

import com.owner.adapter.ConsumeAndIncomeAdapter;
import com.owner.constant.AppConstants;
import com.owner.domain.Consume;
import com.owner.domain.Embarrass;
import com.owner.domain.JsonEntity;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.pull.list.XListView;
import com.owner.pull.list.XListView.IXListViewListener;
import com.owner.tools.DialogUtil;
import com.owner.tools.GsonUtil;
import com.owner.tools.MyProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EmbarrassListActivity extends Activity implements IXListViewListener {

	private XListView embarrassListView;
	private Context context;
	private EmbarrassAdapter adapter;
	private List<Embarrass> datalist=new ArrayList<Embarrass>();
	private MyProgressDialog pdialog;
	private Handler mHandler;
	
	private int pageno=0;
	private int pagesize=5;
	private int pageindex=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_embarrass_list);
		embarrassListView=(XListView) findViewById(R.id.xListView);
		embarrassListView.setPullLoadEnable(true);
        context=EmbarrassListActivity.this;
		//获取网络数据并显示列表
        getData(0,pagesize,true);
		embarrassListView.setOnItemClickListener(listener);
		embarrassListView.setXListViewListener(this);
		mHandler = new Handler();
	}
	
	/* 获取网络数据函数
	 * pageno  起始位置
	 * pagesize 每次请求的条数
	 * first   是否为第一次请求数据
	 */
	public void getData(int pageno,int pagesize,final boolean first)
	{
		HttpClientService svr = new HttpClientService(AppConstants.EmbarrassRequestAction);
		//参数
		svr.addParameter("did","15923258890");
		svr.addParameter("pageno",pageno);
		svr.addParameter("pagesize",pagesize);
		
		HttpAndroidTask task = new HttpAndroidTask(context, svr,
				new HttpResponseHandler() {
					// 响应事件
					public void onResponse(Object obj) {
						if(first)
						{
							pdialog.stop();	
						}						
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,context,false);
						if (jsonEntity.getStatus() == 1) {
							Toast.makeText(context, "没有可查看的数据", 2).show();
						} else if (jsonEntity.getStatus() == 0) {
							List<Embarrass> tempList=GsonUtil.getGson().fromJson(jsonEntity.getData(), AppConstants.type_embarrassList);
							if(first)
							{
								pageindex=0;
								datalist.clear();
								copyToList(tempList);//把tempList的数据保存到datalist当中	
								adapter=new EmbarrassAdapter(context, datalist);
								embarrassListView.setAdapter(adapter);
							}else{
								copyToList(tempList);//把tempList的数据保存到datalist当中
								adapter.notifyDataSetChanged();
							}
							
						}else
						{
							Toast.makeText(context, "服务器数据出错", 2).show();
						}
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
						if(first)
						{
							pdialog = new MyProgressDialog(context);
							DialogUtil.setAttr4progressDialog(pdialog);	
						}						
					}
				});
		task.execute(new String[] {});
	}
	
	private OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(id == -1) {  
		        // 点击的是headerView或者footerView  
		        return;  
		    }  
		    int realPosition=(int)id;  
			Intent intent=new Intent(context,EmbarrassDetailActivity.class);
			intent.putExtra("data", datalist.get(realPosition));
			startActivity(intent);
		}
	};
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getData(0,pagesize,true);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		pageindex++;
		pageno=pageindex*pagesize;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getData(pageno,pagesize,false);
				//geneItems();
				//mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	private void onLoad() {
		embarrassListView.stopRefresh();
		embarrassListView.stopLoadMore();
		embarrassListView.setRefreshTime("刚刚");
	}
	
	//把一个list中的数据拷贝到另外一个list当中
	private void copyToList(List<Embarrass> tempList) {
		for(int i=0;i<tempList.size();i++)
		{
			datalist.add(tempList.get(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.embarrass_list, menu);
		return false;
	}
	
	//糗事儿列表的adapter
	public class EmbarrassAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Embarrass> list;
		
		/**
		 * 构造函数
		 * @param context
		 */
		public EmbarrassAdapter(Context context,List<Embarrass> datalist)
		{
			inflater=LayoutInflater.from(context);
			this.list=datalist;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
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
	        	 
		          convertView = inflater.inflate(R.layout.simple_list_item,null);	
		          holder = new ViewHolder();
		          
		         /*获取Item组件*/                    
		         holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
		         
		         convertView.setTag(holder);//给holder设置tag                   
	         }
	         else
	         {
	             holder = (ViewHolder)convertView.getTag();//获取holder                
	         }

	         /*Item组件赋值
	          * 
	          * */            
	         holder.title.setText(list.get(position).getEtitle());
	         
	         /**
	          * 给Item附上样式
	          */
	         if (list.size() == 1) {
	             convertView.setBackgroundResource(R.drawable.circle_list_single);
	         } else if (list.size() > 1) {
	             if (position == 0) {
	                 convertView.setBackgroundResource(R.drawable.circle_list_top);
	             } else if (position == (list.size() - 1)) {
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
