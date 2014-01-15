package com.owner.disclosureyourlife;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.owner.constant.AppConstants;
import com.owner.db.DatabaseContext;
import com.owner.db.MyDatabaseHelper;
import com.owner.disclosureyourlife.GalleryPage.ImageAdapter;
import com.owner.domain.Embarrass;
import com.owner.domain.JsonEntity;
import com.owner.httpgson.HttpAndroidTask;
import com.owner.httpgson.HttpClientService;
import com.owner.httpgson.HttpPreExecuteHandler;
import com.owner.httpgson.HttpResponseHandler;
import com.owner.pic.Bimp;
import com.owner.tools.DownloadPicture;
import com.owner.tools.GetImgFromServer;
import com.owner.tools.GsonUtil;
import com.owner.tools.ImageTools;
import com.owner.tools.MyProgressDialog;
import com.owner.tools.DownloadPicture.IPictureHandle;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class EmbarrassDetailActivity extends Activity {

	private Button back;
	private TextView title;
	private Context context;
	
	private ViewStub viewStub;
	private EditText embarrassTitle;
	private EditText embarrassDesc;
	private Button embarrassButton;
	private Button uploadPic;
	private PopupWindow mPopupWindow;
	private Handler handler;
	
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private boolean FLAG = false;
	private int REQUEST_CODE;
	private static final int SCALE = 5;//照片缩小比例
	private LinearLayout linear_uploads;
	
	private List<String> picLists=new ArrayList<String>();//存储选择或者拍照的照片list
	private MyProgressDialog pdialog;
	
	private TextView stitle;
	private TextView sdesc;
	private ImageView picture;
	private int eid=0;
	
	
	private Embarrass data;
	private MyDatabaseHelper dbHelper; //本地数据库类
	private Bitmap bitmap;//显示上传的第一张图片
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		setContentView(R.layout.activity_embarrass_detail);
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.embarrassTitle);
		title.setText(getString(R.string.embarrass_title));
		handler=new Handler();
		
		context=EmbarrassDetailActivity.this;
		
		back.setOnClickListener(l);
		
		Intent intent=getIntent();
		if(intent.hasExtra("data"))
		{			
			FLAG=false;
			data=(Embarrass) intent.getSerializableExtra("data");
			title.setText(R.string.embarrass_detail);
			viewStub=(ViewStub) findViewById(R.id.embarrassDetailLayout);
			viewStub.inflate();
			stitle=(TextView) findViewById(R.id.title);
			sdesc=(TextView) findViewById(R.id.describe);
			picture=(ImageView) findViewById(R.id.pictureShow);
			stitle.setText(data.getEtitle());
			sdesc.setText(data.getEdesc());
			eid=data.getEid();//保存传递过来的实体id，为了后面查询更多的图片
			//以下两句是为了把数据库保存在SD卡上
			DatabaseContext dbContext = new DatabaseContext(this);
			dbHelper = new MyDatabaseHelper(dbContext);
			
			String ppath=data.getEpic();
			if(ppath!=null&&!ppath.equals(null)&&!ppath.equals(""))
			{
				if(selectData()&&bitmap!=null)
				{
					picture.setImageBitmap(bitmap);
				}else
				{
					try {
						new GetImgFromServer(picture).execute(ppath);//异步下载图片
					} catch (Exception e) {
						Toast.makeText(context, "图片地址解析有误", 2).show();
						e.printStackTrace();
					}
				}				
			}			
			picture.setOnClickListener(l);
			
		}else{
			FLAG=true;
			viewStub=(ViewStub) findViewById(R.id.embarrassUploadLlayout);
			viewStub.inflate();
			embarrassTitle=(EditText) findViewById(R.id.embarrassDetailTitle);
			embarrassDesc=(EditText) findViewById(R.id.embarrassEditText);
			embarrassButton=(Button) findViewById(R.id.embarrassMakeSureButton);
			uploadPic=(Button) findViewById(R.id.uploadPicButton);
			linear_uploads=(LinearLayout) findViewById(R.id.linear_uploads);
			embarrassButton.setOnClickListener(l);
			uploadPic.setOnClickListener(l);
		}
	}
	
	//图片上传完成清空所有项
	public void clearAll()
	{
		embarrassTitle.setText("");
		embarrassDesc.setText("");
		linear_uploads.removeAllViews();
		picLists.clear();	
		Bimp.drr.clear();
	}
	
	//点击确定按钮上传字段和照片
	public void upLoadData()
	{
		String title=embarrassTitle.getText().toString();//标题字段
		String desc=embarrassDesc.getText().toString();//描述字段
		if(title!=null&&title!=""&&!title.equals("")&&!title.equals(null))
		{
			HttpClientService svr = new HttpClientService(AppConstants.RequestUpLoadAction,true);//上传访问的地址
			//参数
			Embarrass embarrass=new Embarrass();
			embarrass.setEdesc(desc);
			embarrass.setEtitle(title);
			embarrass.setUid(AppConstants.USER_ID);
			svr.addParameter("embarrass",GsonUtil.getGson().toJson(embarrass));
			/*svr.addParameter("title",title);
			svr.addParameter("desc",desc);
			svr.addParameter("picCount",picLists.size());*/
			if(picLists.size()>0)
			{
				Log.e("图片张数", picLists.size()+"");
				for(int i=0;i<picLists.size();i++)
				{
					String fpath=picLists.get(i);
					//String path=picLists.get(i).substring(0,picLists.get(i).lastIndexOf("/"));
					//String name=fpath.substring(fpath.lastIndexOf("/")+1, fpath.length());
					File ffff=new File(fpath);
					//Log.e("上传图片的路径", path);
					//Log.e("上传图片的名字", name);
					svr.addParameter("pictures",ffff);
				}
			}
			
			
			HttpAndroidTask task = new HttpAndroidTask(context, svr,
					new HttpResponseHandler() {
						// 响应事件
						public void onResponse(Object obj) {
							pdialog.stop();
							JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
									obj,context,false);
							if (jsonEntity.getStatus() == 1) {
								Toast.makeText(context, "没有可查看的数据", 2).show();
							} else if (jsonEntity.getStatus() == 0) {
								Toast.makeText(context, "提交成功", 2).show();
								/*datalist=GsonUtil.getGson().fromJson(jsonEntity.getData(), AppConstants.type_movieList);
								adapter=new ConsumeAndIncomeAdapter(context, datalist);
								consumeListView.setAdapter(adapter);*/
								clearAll();
								finish();
							}else
							{
								Toast.makeText(context, "服务器数据出错", 2).show();
							}
						}
					}, new HttpPreExecuteHandler() {
						public void onPreExecute(Context context) {
							pdialog = new MyProgressDialog(context);
							pdialog.start("上传数据中...");
						}
					});
			task.execute(new String[] {});
		}else
		{
			Toast.makeText(context, "标题是必须的", 2).show();
		}
		
	}
	
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				if(FLAG)
				{
					clearAll();
				}				
				finish();
				break;
			case R.id.embarrassMakeSureButton:
				upLoadData();
				break;
			case R.id.uploadPicButton:
				showPopWindow();
				break;
				//弹窗点击事件
			case R.id.selectAlbumButton:
				//choosePicInAlbum();
				Intent intent=new Intent(context,SelectPicAlbumActivity.class);
				startActivityForResult(intent, 0x222);
				break;
			case R.id.takePhotoButton:
				takePhoto();
				break;
			case R.id.cancelButton:
				mPopupWindow.dismiss();
				break;
				//弹窗点击事件完
			case R.id.pictureShow:
				/*for(int i=0;i<data.getPics().size();i++)
				{
					loadMorePictures(data.getPics().get(i).getPpic());
				}*/	
				if(data.getPics().size()>0)
				{
					Intent i=new Intent(context,GalleryPage.class);
					Bundle b=new Bundle();
					b.putSerializable("pics", (Serializable) data.getPics());
					b.putString("eid", eid+"");
					i.putExtras(b);
					startActivity(i);
				}else
				{
					Toast.makeText(context, "无可查看的图片", 2).show();
				}
				
				break;
			default:
				break;
			}
		}				
	};
	
	
	
	//显示弹出窗口
	private void showPopWindow() {
		View view=getLayoutInflater().inflate(R.layout.popupwindow_layer,null);
		if(mPopupWindow==null)
		{
			mPopupWindow=new PopupWindow(view,
					LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		}
		mPopupWindow.setFocusable(true);//设置获取焦点		
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//为了让在外点击消失弹出框
		mPopupWindow.setOutsideTouchable(true);// 设置允许在外点击消失 
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);//设置弹窗进入与退出的动画效果
		mPopupWindow.showAtLocation(viewStub, Gravity.BOTTOM, 0, 0);
		
		//按钮
		Button selectAlbum_btn=(Button) view.findViewById(R.id.selectAlbumButton);
		Button takePhoto_btn=(Button) view.findViewById(R.id.takePhotoButton);
		Button cancel_btn=(Button) view.findViewById(R.id.cancelButton);
		
		cancel_btn.setOnClickListener(l);
		selectAlbum_btn.setOnClickListener(l);
		takePhoto_btn.setOnClickListener(l);
	}
	
	//选择相册启动函数
	private void choosePicInAlbum()
	{
		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
	    REQUEST_CODE = CHOOSE_PICTURE;
		openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(openAlbumIntent, REQUEST_CODE);
	}

	//启动拍照界面
	private void takePhoto()
	{
		Uri imageUri = null;
		String fileName = null;
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		REQUEST_CODE = TAKE_PICTURE;
		fileName = "image.jpg";
		imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, REQUEST_CODE);
	}
	
	//图片选择回调处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				//将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				
				//将处理过的图片显示在界面上，并保存到本地
				//iv_image.setImageBitmap(newBitmap);
				String path=Environment.getExternalStorageDirectory().getAbsolutePath();
				String photoName=String.valueOf(System.currentTimeMillis());
				ImageTools.savePhotoToSDCard(newBitmap,path,photoName);
				picLists.add(path+"/"+photoName+".png");//保存到本地的图片全名
				//显示图片
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						linear_uploads.removeAllViews();
						ImageTools.showImageInLinearLayout(context, picLists, linear_uploads);
					}
				});
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				//照片的原始资源地址
				Uri originalUri = data.getData(); 
	            try {
	            	//使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
						//释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();
						
						//iv_image.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;						
			default:
				break;
			}
		}else if(resultCode==0x222)
		{
			if(requestCode==0x222)
			{
				mPopupWindow.dismiss();
				final ArrayList<String> list=data.getStringArrayListExtra("picPath");
				
				if(list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						picLists.add(list.get(i));
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							linear_uploads.removeAllViews();
							ImageTools.showImageInLinearLayout(context, picLists, linear_uploads);
						}
					});
				}
				
			}
		}
	}
	
	//下面这两个方法是为了把选择和拍照的都放在同一个列表中
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String[] files = (String[]) savedInstanceState
				.getCharSequenceArray("picLists");
		picLists.addAll(Arrays.asList(files));
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharSequenceArray("picLists",
				picLists.toArray(new String[picLists.size()]));
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.embarrass_detail, menu);
		return false;
	}

	@Override
	public void finish() {
		if(FLAG)
		{
			clearAll();
		}
		super.finish();
	}
	
	//在数据库中查找是否有数据，有就保存到List当中显示，没有就下载
	private boolean selectData()
	{
		ArrayList<Map<String, String>> result=new ArrayList<Map<String,String>>();
		try {
			// 执行查询
			Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select eid,pic from picture where eid=?",
				new String[] { eid+"" });
			result = converCursorToList(cursor);
		} catch (Exception e) {
			e.printStackTrace();
			return false;			
		}		
		if(result!=null&&result.size()>0)
		{
			bitmap=ImageTools.stringtoBitmap(result.get(0).get("pic"));
		}else
		{
			return false;
		}
		return true;
	}
	
	//通过游标把数据加载到mapList当中去
	protected ArrayList<Map<String, String>> converCursorToList(Cursor cursor)
	{
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// 遍历Cursor结果集
		while (cursor.moveToNext())
		{
			// 将结果集中的数据存入ArrayList中
			Map<String, String> map = new HashMap<String, String>();
			// 取出查询记录中第2列、第3列的值
			map.put("eid", cursor.getString(cursor.getColumnIndex("eid")));
			map.put("pic", cursor.getString(cursor.getColumnIndex("pic")));
			result.add(map);
		}
		return result;
	}
}
