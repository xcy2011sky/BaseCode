package com.xcy.xpassword.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.xcy.lib.annotation.FindViewById;
import com.xcy.lib.annotation.XAnnotationHelper;

import com.xcy.xpassword.R;
import com.xcy.xpassword.ui.XPasswordActivity;

import com.xcy.xpassword.app.XDebug;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.CategoryItem;
import com.xcy.xpassword.model.Password;
import com.xcy.xpassword.model.PasswordItem;
import com.xcy.xpassword.service.Mainbinder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author xcy
 * 
 */
public class CategoryAdapter extends BaseAdapter {
	
	private Context context;
	
	private XDebug debug=new XDebug("CategoryAdapter", true);
	public List<CategoryItem> categorys = new ArrayList<CategoryItem>(); 

	
	
	private int padding;
	private Mainbinder mainbinder;
	
	public XPasswordActivity activity;


	private Comparator<PasswordItem> comparator = new Comparator<PasswordItem>() {
		@Override
		public int compare(PasswordItem lhs, PasswordItem rhs) {
		
			if (lhs.password.isTop() || rhs.password.isTop()) {
				if (lhs.password.isTop() && rhs.password.isTop()) {
					return (int) (rhs.password.getCreateDate() - lhs.password
							.getCreateDate());
				} else if (lhs.password.isTop()) {
					return -1;
				} else {
					return 1;
				}
			}
			return (int) (rhs.password.getCreateDate() - lhs.password
					.getCreateDate());
		}
	};

	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public CategoryAdapter(Context context, XPasswordActivity mActivity) {
		this.context = context;
		padding = dip2px(6);
		this.activity = mActivity;

	}

	public void setData(List<Password> passwords, Mainbinder mainbinder) {
	
		this.mainbinder = mainbinder;
		boolean flag=false;
		
		for(Category c : activity.gCategorys)
		{
			List<PasswordItem> items = new ArrayList<PasswordItem>();
			for(Password password: passwords){
				if(password.getCategory()==c)
				{
					items.add(new PasswordItem(this.context,password));
					Collections.sort(items, comparator);
					flag=true;
				}
			}
			if(flag)
				{
					categorys.add(new CategoryItem(c, items));
					flag=false;
				}
			
		}

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return categorys.size();
	}

	@Override
	public CategoryItem getItem(int position) {
		return categorys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		for (CategoryItem categoryItem : categorys) {
			
			for(PasswordItem passwordItem: categoryItem.passworditems)
				passwordItem.initDataString();
		}
		
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.main_category_item, null);
			convertView.setTag(viewHolder);
			XAnnotationHelper.findView(viewHolder, convertView);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			convertView.setPadding(padding, padding, padding, padding);
		} else {
			convertView.setPadding(padding, 0, padding, padding);
		}

		CategoryItem categoryItem = getItem(position);

		
	
			viewHolder.bindView(categoryItem);

		return convertView;
	}

	public class ViewHolder implements android.view.View.OnClickListener {
		@FindViewById(R.id.main_category_icon)
		public ImageView iconView;

		@FindViewById(R.id.main_category_title)
		public TextView titleView;

		@FindViewById(R.id.main_category_count)
		public TextView countView;

		void bindView(CategoryItem categoryItem) {
			
			
			iconView.setImageResource(activity.gCategorys
					.get(categoryItem.category.getIndex()).iconID);

			titleView.setText(activity.getResources().getStringArray(R.array.password_category)[categoryItem.category.getIndex()]);
			
			countView.setText(String.valueOf(categoryItem.passworditems.size())+"子项");

			
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

		}
	}


	public void onNewPassword(Password password,Category category) {
		List<PasswordItem> items = new ArrayList<PasswordItem>();

		debug.LogV("onNewPassword");
			for(CategoryItem categoryItem :categorys)
			{
				if(categoryItem.category==category)
				{
					debug.LogV(" before item.passwords.size()="+categoryItem.passworditems.size());
					categoryItem.passworditems.add(new PasswordItem(this.context,password));
					Collections.sort(categoryItem.passworditems, comparator);
					notifyDataSetChanged();
					debug.LogV("after item.passwords.size()="+categoryItem.passworditems.size());
					return ;
				}
			}
			
		    items.add(new PasswordItem(this.context,password));
		    
			categorys.add(new CategoryItem(category, items));
		
			for(CategoryItem categoryItem :categorys)
			{
				if(categoryItem.category==category)
				{
					debug.LogV("new item.passwords.size()="+categoryItem.passworditems.size());
				}
			}
			
			notifyDataSetChanged();
		

	}

	public void onDeletePassword(int id,Category category) {
		
		
		for(int i=0;i<categorys.size();i++)
		{
			if(categorys.get(i).category==category){
				for (int j= 0; j< categorys.get(i).passworditems.size(); j++) {
					PasswordItem passwordItem = categorys.get(i).passworditems.get(j);
					if (passwordItem.password.getId() == id) {
						categorys.get(i).passworditems.remove(j);
						break;
					}
				}
				
			}
			if(categorys.get(i).passworditems.size()==0)
			{
				categorys.remove(categorys.get(i));
			}
		}
		
		notifyDataSetChanged();
	}

	
}
