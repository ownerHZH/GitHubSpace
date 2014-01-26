
package com.owner.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
	final String CREATE_TABLE_SQL =
		"create table picture(_id integer primary " +
		"key autoincrement , eid , pic)";
	final String CREATE_TABLE_SPINNER_ITEM_SQL =
			"create table spinner(_id integer primary " +
			"key autoincrement , sid integer, value)";
	
	final String CREATE_TABLE_LIST_DATA_SQL =
			"create table ci_list(_id integer primary " +
			"key autoincrement ,ciid integer, uid integer, name,money,date,flag integer)";
	
	public MyDatabaseHelper(Context context)
	{
		super(context, "myPicture.db3", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.beginTransaction();
		try
		{
			db.execSQL(CREATE_TABLE_SQL);
			db.execSQL(CREATE_TABLE_SPINNER_ITEM_SQL);
			db.execSQL(CREATE_TABLE_LIST_DATA_SQL);
			//设置事务标志为成功，当结束事务时就会提交事务
			db.setTransactionSuccessful();
		}
		finally
		{
			//结束事务
			db.endTransaction();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db
		, int oldVersion, int newVersion)
	{
		System.out.println("--------onUpdate Called--------"
			+ oldVersion + "--->" + newVersion);
	}
}
