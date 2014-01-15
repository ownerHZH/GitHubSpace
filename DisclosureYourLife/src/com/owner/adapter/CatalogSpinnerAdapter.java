package com.owner.adapter;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.owner.disclosureyourlife.R;

public class CatalogSpinnerAdapter implements SpinnerAdapter
{
	private LayoutInflater inflater;
	private List<String> items;
	private int flag;
	//private Context context;
	
	public CatalogSpinnerAdapter(Context context,List<String> items,int flag) {
		this.items = items;
		//this.context=context;
		this.flag=flag;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	//返回选中项
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;        
         if (convertView == null) {
        	 
	          convertView = inflater.inflate(R.layout.item,null);	
	          holder = new ViewHolder();
	          
	        //获取Item组件                    
	         holder.item = (TextView) convertView.findViewById(R.id.itemTextView);
	         
	         convertView.setTag(holder);//给holder设置tag                   
         }
         else
         {
             holder = (ViewHolder)convertView.getTag();//获取holder                
         }
         //Item组件赋值            
         if(flag==0)
         {
        	 holder.item.setText("选择消费的小项");	
         }else if (flag==1) {
        	 holder.item.setText("选择收入的小项");	
		}              
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	//返回下拉列表项
	@Override
	public View getDropDownView(int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder;        
         if (convertView == null) {
        	 
	          convertView = inflater.inflate(R.layout.item,null);	
	          holder = new ViewHolder();
	          
	         /*获取Item组件*/                    
	         holder.item = (TextView) convertView.findViewById(R.id.itemTextView);
	         
	         convertView.setTag(holder);//给holder设置tag                   
         }
         else
         {
             holder = (ViewHolder)convertView.getTag();//获取holder                
         }
         /*Item组件赋值*/   
         if(position==0)
         {
        	 TextPaint tp = holder.item.getPaint(); 
        	 tp.setFakeBoldText(true);//字体加粗
        	 holder.item.setTextSize(25);
         }else {
        	 holder.item.setTextSize(20); 
		}	         
         String string=items.get(position).toString();
         holder.item.setText(string);
         
         /**
          * 给Item附上样式
          */
         if (items.size() == 1) {
             convertView.setBackgroundResource(R.drawable.circle_list_single);
         } else if (items.size() > 1) {
             if (position == 0) {
                 convertView.setBackgroundResource(R.drawable.circle_list_top);
             } else if (position == (items.size() - 1)) {
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
		public TextView item;   //列表显示的单项
	}
	
}
