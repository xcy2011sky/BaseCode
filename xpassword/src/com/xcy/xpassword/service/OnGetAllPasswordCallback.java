package com.xcy.xpassword.service;

import java.util.List;

import com.xcy.xpassword.model.Password;



public interface OnGetAllPasswordCallback
{
	public void onGetAllPassword(List<Password> passwords);
}
