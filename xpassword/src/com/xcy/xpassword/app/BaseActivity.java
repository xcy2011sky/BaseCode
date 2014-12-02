package com.xcy.xpassword.app;

import java.lang.reflect.Field;

import com.xcy.lib.annotation.ViewFinder;
import com.xcy.lib.annotation.XAnnotationHelper;
import com.xcy.xpassword.model.SettingKey;



import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;



public class BaseActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	public BaseActivity getActivity()
	{
		return this;
	}

	public void showToast(int id)
	{
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	public XApplication getMyApplication()
	{
		return (XApplication) getApplication();
	}

	public String getSetting(SettingKey key, String defValue)
	{
		return getMyApplication().getString(key, defValue);
	}

	public void putSetting(SettingKey key, String value)
	{
		getMyApplication().putString(key, value);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		initAnnotation();
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		initAnnotation();
	}

	@Override
	public void setContentView(View view, LayoutParams params)
	{
		super.setContentView(view, params);
		initAnnotation();
	}

	private void initAnnotation()
	{
		ViewFinder viewFinder = ViewFinder.create(this);
		Class<?> clazz = getClass();
		do
		{
			findView(clazz, viewFinder);
		}
		while ((clazz = clazz.getSuperclass()) != BaseActivity.class);
	}

	/** {@link FindViewById} */
	private void findView(Class<?> clazz, ViewFinder viewFinder)
	{
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				XAnnotationHelper.findView(this, field, viewFinder);
			}
		}
	}
}
