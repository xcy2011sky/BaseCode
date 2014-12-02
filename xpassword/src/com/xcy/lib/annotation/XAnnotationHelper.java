/*
 * Copyright (c) 2014 xcy
 */
package com.xcy.lib.annotation;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;

/**
 * 
 * 
 * @author xcy
 */
public class XAnnotationHelper
{
	/**
	 * 
	 * 
	 * @param target
	 */
	public static void findView(Activity target)
	{
		findView(target, ViewFinder.create(target));
	}

	/**
	 *
	 * 
	 * @param target
	 */
	public static void findView(Object target, View view)
	{
		findView(target, ViewFinder.create(view));
	}

	
	public static void findView(Object target, ViewFinder viewFinder)
	{
		Class<?> clazz = target.getClass();

		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				findView(target, field, viewFinder);
			}
		}
	}


	public static void findView(Object target, Field field, ViewFinder viewFinder)
	{
		if (field.isAnnotationPresent(FindViewById.class))
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}

			int id = field.getAnnotation(FindViewById.class).value();
			View view = viewFinder.FindViewById(id);

			checkView(field.getName(), view, field.getType());

			try
			{
				field.set(target, view);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void checkView(String msg, View targetView, Class<?> bindType)
	{
		if (targetView == null)
		{
			throw new NullPointerException("\"" + msg + "\" targetview not null !!");
		}
		else if (!bindType.isInstance(targetView))
		{
			String error = "targetview \"" + msg + "\"is not match" + bindType + "" + targetView.getClass();
			throw new ClassCastException(error);
		}
	}
}
