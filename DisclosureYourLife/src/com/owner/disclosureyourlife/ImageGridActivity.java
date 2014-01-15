package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.owner.pic.AlbumHelper;
import com.owner.pic.Bimp;
import com.owner.pic.ImageGridAdapter;
import com.owner.pic.ImageItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	private EmbarrassDetailActivity em;
	private Button back,selected;
	private TextView title;
	
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", 400).show();
				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);
		
		back=(Button) findViewById(R.id.back);
		selected=(Button) findViewById(R.id.selectedButton);
		title=(TextView) findViewById(R.id.embarrassTitle);
		title.setText("选择照片");
		back.setOnClickListener(l);
		selected.setOnClickListener(l);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
	}
	
	//获取选择的图片路径函数
	public void getSelectedPicPath()
	{
		ArrayList<String> list = new ArrayList<String>();
		Collection<String> c = adapter.map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			list.add(it.next());
		}
		for (int i = 0; i < list.size(); i++) {
			if (Bimp.drr.size() < 9) {
				Bimp.drr.add(list.get(i));				
			}
		}
		Intent intent=new Intent();
		intent.putStringArrayListExtra("picPath", (ArrayList<String>) list);
		setResult(0x222,intent);
		finish();
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}
	
private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.selectedButton:
				getSelectedPicPath();
				break;
			default:
				break;
			}
		}		
	};
}
