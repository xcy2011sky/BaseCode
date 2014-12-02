package com.xcy.xpassword.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.xcy.lib.annotation.FindViewById;
import com.xcy.lib.annotation.XAnnotationHelper;
import com.xcy.xpassword.R;
import com.xcy.xpassword.app.XDebug;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.Password;
import com.xcy.xpassword.model.PasswordItem;
import com.xcy.xpassword.service.Mainbinder;
import com.xcy.xpassword.ui.XPasswordActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class XBaseAdapter  extends  BaseAdapter{
	
	private Context context;
	
	private XDebug debug=null;
	
	public List<PasswordItem> passwords = new ArrayList<PasswordItem>();

	private int padding;
	private Mainbinder mainbinder;
	
	public XPasswordActivity activity;
	
	public String  name="XBaseAdapterFactory";
	
	public XBaseAdapter(Context mContext,String mName,XPasswordActivity mActivity)
	{
		 context=mContext;
		 name=mName;
		 debug=new XDebug(name,true);
		 activity=mActivity;
		 
	}
	
	private Comparator<PasswordItem> comparator = new Comparator<PasswordItem>()
			{
				@Override
				public int compare(PasswordItem lhs, PasswordItem rhs)
				{
					
					if (lhs.password.isTop() || rhs.password.isTop())
					{
						if (lhs.password.isTop() && rhs.password.isTop())
						{
							return (int) (rhs.password.getCreateDate() - lhs.password.getCreateDate());
						}
						else if (lhs.password.isTop())
						{
							return -1;
						}
						else
						{
							return 1;
						}
					}
					return (int) (rhs.password.getCreateDate() - lhs.password.getCreateDate());
				}
			};

	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public void setData(List<Password> passwords, Mainbinder mainbinder)
	{
		this.mainbinder = mainbinder;
		this.passwords.clear();
		for (Password password : passwords)
		{
			this.passwords.add(new PasswordItem(this.context,password));
		}
		Collections.sort(this.passwords, comparator);
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return passwords.size();
	}

	@Override
	public PasswordItem getItem(int position)
	{
		return passwords.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public void notifyDataSetChanged()
	{
		for (PasswordItem passwordItem : passwords)
		{
			passwordItem.initDataString();
		}
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		 ViewHolder viewHolder;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.main_password_item, null);
			convertView.setTag(viewHolder);
			XAnnotationHelper.findView(viewHolder, convertView);
			
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0)
		{
			convertView.setPadding(padding, padding, padding, padding);
		}
		else
		{
			convertView.setPadding(padding, 0, padding, padding);
		}

		PasswordItem passwordItem = getItem(position);

		viewHolder.bindView(passwordItem);

		return convertView;
	}
	private class ViewHolder implements android.view.View.OnClickListener
	{
		@FindViewById(R.id.main_item_icon)
		public ImageView iconView;
		
		@FindViewById(R.id.main_item_title)
		public TextView titleView;
		
		@FindViewById(R.id.main_item_category)
		public TextView categoryView;

		@FindViewById(R.id.main_item_date)
		public TextView dateView;

		@FindViewById(R.id.main_item_top)
		public View topIconView;

		private PasswordItem passwordItem;

		void bindView(PasswordItem passwordItem)
		{
			this.passwordItem = passwordItem;
	  
			iconView.setImageResource(passwordItem.password.getIconID());
			
			titleView.setText(passwordItem.password.getTitle());
			categoryView.setText(activity.getResources().getStringArray(R.array.password_category)[passwordItem.password.getCategory().getIndex()]);
			
			dateView.setText(passwordItem.dataString);

			if (passwordItem.password.isTop())
			{
				topIconView.setVisibility(View.VISIBLE);
				dateView.setTextColor(context.getResources().getColor(R.color.title_color));
			}
			else
			{
				topIconView.setVisibility(View.GONE);
				dateView.setTextColor(context.getResources().getColor(R.color.text_color));
			}
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public void onNewPassword(Password password)
	{
		passwords.add(0, new PasswordItem(context,password));
		Collections.sort(this.passwords, comparator);
	
		notifyDataSetChanged();
	}

	public void onDeletePassword(int id,Category c)
	{
		for (int i = 0; i < passwords.size(); i++)
		{
			PasswordItem passwordItem = passwords.get(i);
			if (passwordItem.password.getId() == id)
			{
				passwords.remove(i);
				break;
			}
		}
		notifyDataSetChanged();
	}

	public void onUpdatePassword(Password newPassword)
	{
		boolean needSort = false;
		for (int i = 0; i < passwords.size(); i++)
		{
			Password oldPassword = passwords.get(i).password;
			if (oldPassword.getId() == newPassword.getId())
			{
				if (newPassword.getCreateDate() != 0)
					oldPassword.setCreateDate(newPassword.getCreateDate());
				if (newPassword.getTitle() != null)
					oldPassword.setTitle(newPassword.getTitle());
				if (newPassword.getIconID() != 0)
					oldPassword.setIconID(newPassword.getIconID());
				if(newPassword.getCategory()!=null)
				{
					oldPassword.setCategory(newPassword.getCategory());
				}
				if (newPassword.getAccount() != null)
					oldPassword.setAccount(newPassword.getAccount());
				if (newPassword.getPassword() != null)
					oldPassword.setPassword(newPassword.getPassword());
				if(newPassword.getUrl()!=null)
				{
					oldPassword.setUrl(newPassword.getUrl());
				}
				if (newPassword.getNote() != null)
					oldPassword.setNote(newPassword.getNote());
				if (oldPassword.isTop() != newPassword.isTop())
				{
					oldPassword.setTop(newPassword.isTop());
					needSort = true;
				}
				break;
			}
		}
		if (needSort)
			Collections.sort(this.passwords, comparator);
		notifyDataSetChanged();
	}
}
