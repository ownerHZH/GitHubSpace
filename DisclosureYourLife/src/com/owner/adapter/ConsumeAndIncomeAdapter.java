package com.owner.adapter;

import java.util.List;

import com.owner.disclosureyourlife.R;
import com.owner.domain.Consume;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConsumeAndIncomeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Consume> list;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public ConsumeAndIncomeAdapter(Context context,List<Consume> datalist)
	{
		inflater=LayoutInflater.from(context);
		this.list=datalist;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;        
         if (convertView == null) {
        	 
	          convertView = inflater.inflate(R.layout.simple_list_item,null);	
	          holder = new ViewHolder();
	          
	         /*获取Item组件*/                    
	         holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
	         
	         convertView.setTag(holder);//给holder设置tag                   
         }
         else
         {
             holder = (ViewHolder)convertView.getTag();//获取holder                
         }

         /*Item组件赋值*/            
         holder.title.setText(list.get(position).getName());
         
         /**
          * 给Item附上样式
          */
         if (list.size() == 1) {
             convertView.setBackgroundResource(R.drawable.circle_list_single);
         } else if (list.size() > 1) {
             if (position == 0) {
                 convertView.setBackgroundResource(R.drawable.circle_list_top);
             } else if (position == (list.size() - 1)) {
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
		public TextView title;   //列表显示的单项
	}
}
	
	

