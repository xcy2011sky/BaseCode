package com.xcy.xpassword.app;

import android.app.Fragment;
import android.os.Bundle;

public class BaseFragment extends Fragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	protected BaseActivity getBaseActivity()
	{
		return (BaseActivity) getActivity();
	}
	
	protected void showToast(int resId)
	{
		getBaseActivity().showToast(resId);
	}
}
