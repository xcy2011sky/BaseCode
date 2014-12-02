package com.xcy.xpassword.service;

import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.Password;



public interface OnPasswordListener
{
	public void onNewPassword(Password password);
	public void onDeletePassword(int id,Category c);
	public void onUpdatePassword(Password password);
}
