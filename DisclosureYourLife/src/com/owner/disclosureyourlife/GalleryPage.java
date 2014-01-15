
package com.owner.disclosureyourlife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.owner.db.DatabaseContext;
import com.owner.db.MyDatabaseHelper;
import com.owner.domain.Picture;
import com.owner.tools.DownloadPicture;
import com.owner.tools.MyProgressDialog;
import com.owner.tools.DownloadPicture.IPictureHandle;
import com.owner.tools.ImageTools;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

@SuppressWarnings("deprecation")
public class GalleryPage extends Activity implements IPictureHandle {
	private Button back;//返回按钮
	private TextView title;//标题
	private Gallery gallery;//显示图片的通道
	private ArrayList<Picture> pics;  //装载activity传递过来的所有图片路径
	private MyDatabaseHelper dbHelper; //本地数据库类
	private int picTotle=0;//总的图片数
	private int picIndex=0;//记录处理的图片数
	private String eid;   //每一项数据的id,图片上传到数据库时用的一个id
	private List<String> pictures=new ArrayList<String>(); //保存所有图片下载后保存成的String型List
	private MyProgressDialog pdialog;///数据加载提示框
	private Context context=GalleryPage.this;//本类全局内容
	private ImageAdapter iAdapter;  //显示图片的adapter
	private boolean yesorno;//判断数据库中是否有数据
	private Handler handler; //用于网络数据加载完后，调用一个线程把数据保存到本地数据库
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallerypage);
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.embarrassTitle);
		title.setText(getString(R.string.embarrass_detail_pic));
		back.setOnClickListener(l);
		// 创建MyDatabaseHelper对象，指定数据库版本为1，此处使用相对路径即可，
		// 数据库文件自动会保存在程序的数据文件夹的databases目录下。
		//以下两句是为了把数据库保存在SD卡上
		DatabaseContext dbContext = new DatabaseContext(this);
		dbHelper = new MyDatabaseHelper(dbContext);
		
		gallery = (Gallery) findViewById(R.id.gallery);
		Bundle bundle=getIntent().getExtras();
		pics=(ArrayList<Picture>)bundle.getSerializable("pics");
		eid=bundle.getString("eid");
		picTotle=pics.size();
		pdialog = new MyProgressDialog(context);
		pdialog.start("图片加载中...");
        //加载网络上的数据或者本地数据库的数据
	    loadMorePictures(pics);	
		registerForContextMenu(gallery);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add("Action");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Toast.makeText(this, "Longpress: " + info.position, Toast.LENGTH_SHORT)
				.show();
		return true;
	}
	
	//加载更多的图片并显示
	private void loadMorePictures(ArrayList<Picture> pics2) {
		yesorno=selectData();//现在数据库中查找 数据库中没有就加载网络上的资源
		if(!yesorno)//数据库里面没有就下载网络上的
		{
			pictures.clear();
			for (int i = 0; i < pics2.size(); i++) {
				new DownloadPicture(GalleryPage.this, eid)
				     .execute(pics2.get(i).getPpic());
			}
		}
	}
	
	//把网络上的数据插入数据库
	private void insertData(SQLiteDatabase db, String eid
			, String pic)
	{
		// 执行插入语句
		db.execSQL("insert into picture values(null , ? , ?)"
			, new Object[] {eid, pic});
	}
	
	//在数据库中查找是否有数据，有就保存到List当中显示，没有就下载
	private boolean selectData()
	{
		ArrayList<Map<String, String>> result=new ArrayList<Map<String,String>>();
		try {
			// 执行查询
			Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select eid,pic from picture where eid=?",
				new String[] { eid });
			result = converCursorToList(cursor);
		} catch (Exception e) {
			e.printStackTrace();
			return false;			
		}
		
		if(result!=null&&result.size()>0)
		{
			pictures.clear();
			for(int i=0;i<result.size();i++)
			{
				pictures.add(result.get(i).get("pic"));
			}
			iAdapter=null;
			iAdapter=new ImageAdapter(this,pictures);
			gallery.setAdapter(iAdapter);
			pdialog.stop();
			//Log.e("数据库中的数据", "数据库中的数据");
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
	
	//异步加载网络数据的回调函数
	@Override
	public void onBitmapResult(String result,final String eid) {
		picIndex++;
		//insertData(dbHelper.getReadableDatabase(),eid,result);		
		pictures.add(result);
		//判断网络数据是否全部加载完成
		if(picIndex==picTotle)
		{		
			iAdapter=null;
			iAdapter=new ImageAdapter(this,pictures);
			gallery.setAdapter(iAdapter);
			pdialog.stop();
			//Log.e("网络上下载的数据", "网络上下载的数据");
			handler=new Handler();
			handler.postDelayed(new Runnable() {				
				@Override
				public void run() {
					for(int i=0;i<pictures.size();i++)
					{
						//保存到数据库
						insertData(dbHelper.getReadableDatabase(),eid,pictures.get(i));
					}
				}
			}, 2000);
		}		
	}
	
	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
        private List<String> pList;
		private List<Bitmap> bList;

		public ImageAdapter(Context context,List<String> pictures) {
			mContext = context;
            this.pList=pictures;
            bList=new ArrayList<Bitmap>();
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
			for(int i=0;i<pList.size();i++)
			{
				bList.add(ImageTools.stringtoBitmap(pList.get(i)));
			}
		}

		@Override
		public int getCount() {
			return pList.size();
		}

		@Override
		public Object getItem(int position) {
			return pList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);

			i.setImageBitmap(bList.get(position));
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));//300, 400

			// The preferred Gallery item background
			i.setBackgroundResource(mGalleryItemBackground);

			return i;
		}

	}
	//按钮单机监视器
		private OnClickListener l=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.back:
					finish();
					break;
				default:
					break;
				}
			}
		};
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			if(dbHelper!=null&&dbHelper.getReadableDatabase().isOpen())
			{
				dbHelper.getReadableDatabase().close();
				dbHelper.close();
			}
		}

}
