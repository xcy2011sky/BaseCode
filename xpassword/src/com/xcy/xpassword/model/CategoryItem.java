package com.xcy.xpassword.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;


public class CategoryItem {
	public Category category;
	
	public List<PasswordItem>passworditems=new ArrayList<PasswordItem>();
	
	
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<PasswordItem> getPassworditems() {
		return passworditems;
	}
	public void setPassworditems(List<PasswordItem> passworditems) {
		this.passworditems = passworditems;
	}
	
	public CategoryItem(Context context)
	{
		this.category=new Category();
		this.passworditems=null;
	}
	public CategoryItem(Category mCategory,List<PasswordItem> list) {
		// TODO Auto-generated constructor stub
		this.category=mCategory;

		this.passworditems=list;
		
	}
}
