package com.owner.tools;

import java.util.List;

import com.owner.disclosureyourlife.R;

import android.content.Context;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Utils {
	 /** 创建上传的组件
	 * @param position
	 */
	public static  void createModule(Context context,List<String> items,int position,final View itemContainer,int flag) {
		final LinearLayout linearLayout=new LinearLayout(context);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER);
		
		TextView textView=new TextView(context);
		textView.setText(items.get(position)+context.getString(R.string.upload_consume_income_unit_char));
		
		EditText editText=new EditText(context);
		if(flag==0)
		{
			editText.setHint(context.getString(R.string.consume_module_hint));
		}else if (flag==1) {
			editText.setHint(context.getString(R.string.income_module_hint));
		}else
		{
			editText.setHint(context.getString(R.string.consume_module_hint));
			editText.setText(items.get(1));//自定义数据显示 items.get(0==name)  items.get(1==money)
		}
		
		//控制输入数字
		DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
		editText.setKeyListener(numericOnlyListener);
		
		TextView textView1=new TextView(context);
		textView1.setText(context.getString(R.string.upload_consume_income_unit));
		
		Button button=new Button(context);
		button.setBackgroundResource(R.drawable.onebit_32);
		//button.setText(context.getString(R.string.upload_consume_income_unit_delete));
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ViewGroup) itemContainer).removeView(linearLayout);
			}
		});
		
		linearLayout.addView(textView);//项名称
		linearLayout.addView(editText);//项钱数
		linearLayout.addView(textView1);//钱的单位
		linearLayout.addView(button);//删除按钮
			
		//itemContainer.bringChildToFront(linearLayout);
		((ViewGroup) itemContainer).addView(linearLayout,0);//倒序插入位置
		
	}
	
	//为了让ScrollView里面的List显示完所有的数据
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
    }
}
