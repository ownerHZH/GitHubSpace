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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.owner.constant.AppConstants;
import com.owner.constant.AppConstants.Extra;
import com.owner.domain.JsonEntity;
import com.owner.domain.PlainLook;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.tools.DialogUtil;
import com.owner.tools.GsonUtil;
import com.owner.tools.MyProgressDialog;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";
    Context context=ImagePagerActivity.this;
	DisplayImageOptions options;

	ViewPager pager;
	List<String> imageUrls=new ArrayList<String>();
	List<Integer> ids=new ArrayList<Integer>();
	List<Integer> counts=new ArrayList<Integer>();
	List<Integer> bcounts=new ArrayList<Integer>();
	private Button back;

	private TextView title;
	private int isChoose=0;//赞与踩限制在一次，不可多次提交

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
        
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.embarrassTitle);
		title.setText(getString(R.string.plain_look_view_pager_title));
		back.setOnClickListener(l);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		imageUrls = bundle.getStringArrayList(Extra.IMAGES);
		ids=bundle.getIntegerArrayList(Extra.IDS);
		counts=bundle.getIntegerArrayList(Extra.COUNTS);
		bcounts=bundle.getIntegerArrayList(Extra.BCOUNTS);
		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imageUrls));
		pager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private List<String> images;
		private LayoutInflater inflater;

		ImagePagerAdapter(List<String> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			final int we=position;
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			final RadioButton good=(RadioButton) imageLayout.findViewById(R.id.goodRadio);
			final RadioButton bad=(RadioButton) imageLayout.findViewById(R.id.badRadio);
			final RadioGroup radioGroup=(RadioGroup) imageLayout.findViewById(R.id.goodOrBadRadioGroup);

			imageLoader.displayImage(images.get(position), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
					good.setText(counts.get(we)+"");
					bad.setText(bcounts.get(we)+"");
					radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch(checkedId)
							{
								case R.id.goodRadio:
									good.setText(counts.get(we)+1+"");
									isChoose++;
									push(ids.get(we),"good");							
									break;
								case R.id.badRadio:
									bad.setText(bcounts.get(we)+1+"");
									isChoose++;
									push(ids.get(we),"bad");									
									break;
							}
							
						}
					});
				}
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
	
	//赞与不赞更新数据库函数
    public void push(int id,String good_or_bad)
	{
    	if(isChoose==1||isChoose==2)
    	{
    		HttpClientService svr = new HttpClientService(AppConstants.UpdatePlainLookCount);
    		//参数
    		svr.addParameter("gobid",id+"+"+good_or_bad);
    		
    		HttpAndroidTask task = new HttpAndroidTask(context, svr,
    				new HttpResponseHandler() {
    				
    					// 响应事件
    					public void onResponse(Object obj) {						
    						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
    								obj,context,false);
    					}
    				}, new HttpPreExecuteHandler() {
    					public void onPreExecute(Context context) {												
    					}
    				});
    		task.execute(new String[] {});
    	}
		
	}
	
private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:				
				finish();				
			default:
				break;
			}
		}				
	};
	
}