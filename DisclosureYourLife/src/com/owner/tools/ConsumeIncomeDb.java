package com.owner.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.owner.db.DatabaseContext;
import com.owner.db.MyDatabaseHelper;
import com.owner.domain.Consume;
import com.owner.domain.Income;

@SuppressLint("SimpleDateFormat")
public class ConsumeIncomeDb {
	private MyDatabaseHelper dbHelper=null;
	private Context context;
	
	public ConsumeIncomeDb(){}
	
	public ConsumeIncomeDb(Context context)
	{
		this.context=context;
		//以下两句是为了把数据库保存在SD卡上
		if(dbHelper==null)
		{
			DatabaseContext dbContext = new DatabaseContext(context);
			dbHelper = new MyDatabaseHelper(dbContext);
		}		
	}
	
	public void insert(int flag,List<?> list) {
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		if(flag==1)
		{
			@SuppressWarnings("unchecked")
			List<Consume> consumes=(List<Consume>) list;
			Consume consume=null;
			db.beginTransaction();
		    try {
		    	// 执行插入语句
		    	for(int i=0;i<consumes.size();i++)
				{
		    		consume=consumes.get(i);
					db.execSQL("insert into ci_list values(null ,?, ?,?,?,?,?)"
							, new Object[] {consume.getCid(),consume.getUid(),consume.getName(),
									consume.getMoney(),
									new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(consume.getDate()),flag});
				}
		    	db.setTransactionSuccessful();	    	
		    } finally {
		    	db.endTransaction();
		        db.close();
		    }
		}else if(flag==2)
		{
			@SuppressWarnings("unchecked")
			List<Income> incomes=(List<Income>) list;
			Income income=null;
			db.beginTransaction();
		    try {
		    	// 执行插入语句
		    	for(int i=0;i<incomes.size();i++)
				{
		    		income=incomes.get(i);
					db.execSQL("insert into ci_list values(null ,?, ?,?,?,?,?)"
							, new Object[] {income.getIid(),income.getUid(),income.getName(),
									income.getMoney(),
									new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(income.getDate()),flag});
				}
		    	db.setTransactionSuccessful();	    	
		    } finally {
		    	db.endTransaction();
		        db.close();
		    }
		}
				
	}
	
	/**
	 * 查找本地数据库的数据
	 * @param flag  1为消费列表  2为收入列表
	 * @param uid   用户ID
	 * @param date  日期
	 * @return  返回相应的数据List
	 */
	public List<?> select(int flag,int uid,Date date)
	{
		// 执行查询
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
			"select * from ci_list where flag=? and uid=? and date=?",
			   new String[]{flag+"",uid+"",
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)});
		if(flag==1)
		{
			ArrayList<Consume> result=new ArrayList<Consume>();
			try {
				
				while (cursor.moveToNext())
				{
					Consume consume=new Consume();
					consume.setCid(cursor.getInt(cursor.getColumnIndex("ciid")));
					consume.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
					consume.setName(cursor.getString(cursor.getColumnIndex("name")));
					consume.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
					consume.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex("date"))));
					result.add(consume);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;			
			}finally{
				cursor.close();
			}
		}else if(flag==2)
		{
			ArrayList<Income> result=new ArrayList<Income>();
			try {
				while (cursor.moveToNext())
				{
					Income income=new Income();
					income.setIid(cursor.getInt(cursor.getColumnIndex("ciid")));
					income.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
					income.setName(cursor.getString(cursor.getColumnIndex("name")));
					income.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
					income.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex("date"))));
					result.add(income);
				}
				
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;			
			}finally{
				cursor.close();
			}
		}
		return null;				
	}
	
	public List<?> select(int flag)
	{
		// 执行查询
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
			"select * from ci_list where flag=? GROUP BY date ",
			   new String[]{flag+""});
		if(flag==1)
		{
			ArrayList<Consume> result=new ArrayList<Consume>();
			try {
				
				while (cursor.moveToNext())
				{
					Consume consume=new Consume();
					consume.setCid(cursor.getInt(cursor.getColumnIndex("ciid")));
					consume.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
					consume.setName(cursor.getString(cursor.getColumnIndex("name")));
					consume.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
					consume.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex("date"))));
					result.add(consume);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;			
			}finally{
				cursor.close();
			}
		}else if(flag==2)
		{
			ArrayList<Income> result=new ArrayList<Income>();
			try {
				while (cursor.moveToNext())
				{
					Income income=new Income();
					income.setIid(cursor.getInt(cursor.getColumnIndex("ciid")));
					income.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
					income.setName(cursor.getString(cursor.getColumnIndex("name")));
					income.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
					income.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex("date"))));
					result.add(income);
				}
				
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;			
			}finally{
				cursor.close();
			}
		}
		return null;				
	}
	
	public void clearDb(int flag)
	{
		if(tabbleIsExist("ci_list"))
		{
			SQLiteDatabase db=dbHelper.getReadableDatabase();
		    try {
					db.execSQL("delete from ci_list where flag=?", new Object[] {flag});   	
		    } finally {
		        db.close();
		    }
		}		
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public boolean tabbleIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			String sql = "select count(*) as c from sqlite_master"
					+ " where type ='table' and name ='" + tableName.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
