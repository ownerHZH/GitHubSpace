package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.List;

import com.owner.adapter.ConsumeAndIncomeAdapter;
import com.owner.constant.AppConstants;
import com.owner.domain.Consume;
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
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ConsumeListActivity extends Activity implements IXListViewListener {

	private ConsumeAndIncomeAdapter adapter;
	private Context context;
	private XListView consumeListView;
	private List<Consume> datalist=new ArrayList<Consume>();
	private Handler mHandler;
	private MyProgressDialog pdialog;
	
	private int pageno=0;
	private int pagesize=5;
	private int pageindex=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_consume);
		context=ConsumeListActivity.this;
		consumeListView=(XListView) findViewById(R.id.xListView);
		consumeListView.setPullLoadEnable(true);
		//获取网络数据并显示在ListView上
		getData(0,pagesize,true);//获取网络数据
		
		consumeListView.setOnItemClickListener(listener);
		consumeListView.setXListViewListener(this);
		mHandler = new Handler();
	}
	
	public void getData(int pageno,int pagesize,final boolean first)
	{
		HttpClientService svr = new HttpClientService(AppConstants.RequestAction);
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
							List<Consume> tempList=GsonUtil.getGson().fromJson(jsonEntity.getData(), AppConstants.type_consumeList);
							if(first)
							{
								pageindex=0;
								datalist.clear();
								copyToList(tempList);//把tempList的数据保存到datalist当中	
								adapter=new ConsumeAndIncomeAdapter(context, datalist);
								consumeListView.setAdapter(adapter);
							}else {
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
			Intent intent=new Intent(context,ConsumeDetailActivity.class);
			intent.putExtra("data", datalist.get(realPosition));
			startActivity(intent);
		}
	};
	
	private void onLoad() {
		consumeListView.stopRefresh();
		consumeListView.stopLoadMore();
		consumeListView.setRefreshTime("刚刚");
	}
	
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
	
	//把一个list中的数据拷贝到另外一个list当中
	private void copyToList(List<Consume> tempList) {
		for(int i=0;i<tempList.size();i++)
		{
			datalist.add(tempList.get(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consume, menu);
		return false;
	}

	

}
