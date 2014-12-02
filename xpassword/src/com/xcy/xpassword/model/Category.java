package com.xcy.xpassword.model;

import android.content.Context;

import com.xcy.xpassword.R;
import com.xcy.xpassword.database.PasswordDatabase;
import com.xcy.xpassword.service.GetAllPasswordTask;


/**
 * R.drawable.type_bank, 
            R.drawable.type_email, 
            R.drawable.type_login,
            R.drawable.type_membership,
            R.drawable.type_database,
            R.drawable.type_password,
            R.drawable.type_software,
            };
 * @author xcy
 *
 */
public 	class Category{

	public int id;
	public String name;
	public  int index;
	public  int iconID;

	public int getIconID() {
		return iconID;
	}

	public void setIconID(int iconID) {
		this.iconID = iconID;
	}

	public Category(String name,int index,int icon)
	{
		this.name=name;
		this.index=index;
		this.iconID=icon;

		
	}
	
	public Category() {
		// TODO Auto-generated constructor stub
		this.name=null;
		this.index=-1;
		this.iconID=0;

	}
	

	public String getCategoryName(Category category){
		return category.name;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}



	
	
	
}
