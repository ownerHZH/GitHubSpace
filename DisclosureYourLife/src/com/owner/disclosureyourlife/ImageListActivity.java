/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.owner.disclosureyourlife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.owner.constant.AppConstants;
import com.owner.constant.AppConstants.Extra;
import com.owner.domain.Embarrass;
import com.owner.domain.JsonEntity;
import com.owner.domain.PlainLook;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.tools.DialogUtil;
import com.owner.tools.GsonUtil;
import com.owner.tools.MyProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageListActivity extends AbsListViewBaseActivity {

	DisplayImageOptions options;

	List<String> imageUrls=new ArrayList<String>();
	List<Integer> ids=new ArrayList<Integer>();
	private List<PlainLook> datalist=new ArrayList<PlainLook>();
	private Context context=ImageListActivity.this;
	private MyProgressDialog pdialog;

	private ItemAdapter adapter;
	private int pageindex;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_list);

		//Bundle bundle = getIntent().getExtras();
		//imageUrls = AppConstants.IMAGES;
		getData(0,20,true);

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20))
			.build();

		listView = (ListView) findViewById(android.R.id.list);
		adapter=new ItemAdapter();
		((ListView) listView).setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startImagePagerActivity(position);
			}
		});
	}
	
	/* 获取网络数据函数
	 * pageno  起始位置
	 * pagesize 每次请求的条数
	 * first   是否为第一次请求数据
	 */
	public void getData(int pageno,int pagesize,final boolean first)
	{
		HttpClientService svr = new HttpClientService(AppConstants.PlainLookRequestAction);
		//参数
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
							List<PlainLook> tempList=GsonUtil.getGson().fromJson(jsonEntity.getData(), AppConstants.type_plainLookList);
							if(tempList!=null&&tempList.size()>0)
							{
								if(first)
								{
									pageindex=0;
									datalist.clear();
									imageUrls.clear();
									ids.clear();
								}
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
	
	//把一个list中的数据拷贝到另外一个list当中
	private void copyToList(List<PlainLook> tempList) {
		for(int i=0;i<tempList.size();i++)
		{
			datalist.add(tempList.get(i));
			imageUrls.add(tempList.get(i).getPath());
			ids.add(tempList.get(i).getId());
		}
		
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putIntegerArrayListExtra(Extra.IDS, (ArrayList<Integer>) ids);
		intent.putStringArrayListExtra(Extra.IMAGES,(ArrayList<String>) imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return datalist.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(datalist.get(position).getTitle());

			imageLoader.displayImage(datalist.get(position).getPath(), holder.image, options, animateFirstListener);

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}