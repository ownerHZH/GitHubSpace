package com.owner.tools;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.owner.db.DatabaseContext;
import com.owner.db.MyDatabaseHelper;

public class SpinnerDb {
	private MyDatabaseHelper dbHelper=null;
	private Context context;
	
	public SpinnerDb(){}
	
	public SpinnerDb(Context context)
	{
		this.context=context;
		//以下两句是为了把数据库保存在SD卡上
		if(dbHelper==null)
		{
			DatabaseContext dbContext = new DatabaseContext(context);
			dbHelper = new MyDatabaseHelper(dbContext);
		}		
	}
	
	public void insert(int id,List<String> list) {
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		db.beginTransaction();
	    try {
	    	// 执行插入语句
	    	for(int i=0;i<list.size();i++)
			{
				db.execSQL("insert into spinner values(null , ? , ?)"
						, new Object[] {id, list.get(i)});
			}
	    	db.setTransactionSuccessful();	    	
	    } finally {
	    	db.endTransaction();
	        db.close();
	    }		
	}
	
	public List<String> select(int id)
	{
		ArrayList<String> result=new ArrayList<String>();
		try {
			// 执行查询
			Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select value from spinner where sid=?",new String[]{id+""});
			while (cursor.moveToNext())
			{
				result.add(cursor.getString(cursor.getColumnIndex("value")));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}
		return result;		
	}
	
	public void clearDb()
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
	    try {
				db.execSQL("delete from spinner", new Object[] {});   	
	    } finally {
	        db.close();
	    }
	}
	
	public void close()
	{
		dbHelper.close();
	}
}
