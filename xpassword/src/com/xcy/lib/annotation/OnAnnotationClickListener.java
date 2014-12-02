/*
 * Copyright (c) 2014 xcy
 */
package com.xcy.lib.annotation;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * ���÷��䷽ʽ����{@link OnClick}�󶨵ķ���
 * 
 * @author xcy
 * 
 */
public class OnAnnotationClickListener implements OnClickListener
{
	private Object target;
	private Method method;

	public OnAnnotationClickListener(Object target, Method method)
	{
		this.target = target;
		this.method = method;
	}

	@Override
	public void onClick(View v)
	{
		if (!method.isAccessible())
		{
			method.setAccessible(true);
		}
		try
		{
			method.invoke(target, v);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
