package com.xcy.xpassword.service;

import java.util.ArrayList;
import java.util.List;

import com.xcy.xpassword.database.PasswordDatabase;
import com.xcy.xpassword.model.AsyncResult;
import com.xcy.xpassword.model.AsyncSingleTask;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.Password;



import android.content.Context;
import android.os.Binder;


public class Mainbinder extends Binder
{
	private PasswordDatabase passwordDatabase;
	private List<OnPasswordListener> onPasswordListeners = new ArrayList<OnPasswordListener>();

	public Mainbinder(Context context)
	{
		passwordDatabase = new PasswordDatabase(context);
	}

	void onDestroy()
	{
		passwordDatabase.close();
		new AsyncSingleTask<Void>()
		{
			@Override
			protected AsyncResult<Void> doInBackground(AsyncResult<Void> asyncResult)
			{
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Void> asyncResult)
			{
				onPasswordListeners.clear();
			}
		}.execute();
	}

	public void registOnPasswordListener(final OnPasswordListener onPasswordListener)
	{
		new AsyncSingleTask<Void>()
		{
			@Override
			protected AsyncResult<Void> doInBackground(AsyncResult<Void> asyncResult)
			{
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Void> asyncResult)
			{
				onPasswordListeners.add(onPasswordListener);
			}
		}.execute();
	}

	public void unregistOnPasswordListener(final OnPasswordListener onPasswordListener)
	{
		new AsyncSingleTask<Void>()
		{
			@Override
			protected AsyncResult<Void> doInBackground(AsyncResult<Void> asyncResult)
			{
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Void> asyncResult)
			{
				onPasswordListeners.remove(onPasswordListener);
			}
		}.execute();
	}

	public void getAllPassword(OnGetAllPasswordCallback onGetAllPasswordCallback)
	{
		GetAllPasswordTask getAllPasswordTask = new GetAllPasswordTask(passwordDatabase, onGetAllPasswordCallback);
		getAllPasswordTask.execute();
	}

	/**
	 * ɾ������
	 * 
	 * @param id
	 *            ����ID
	 * @param onDeletePasswordResultListener
	 *            ���������
	 */
	public void deletePassword(final int id,final Category category)
	{
		new AsyncSingleTask<Void>()
		{
			@Override
			protected AsyncResult<Void> doInBackground(AsyncResult<Void> asyncResult)
			{
				int result = passwordDatabase.deletePasssword(id);
				asyncResult.setResult(result);
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Void> asyncResult)
			{
				for (OnPasswordListener onPasswordListener : onPasswordListeners)
				{
					onPasswordListener.onDeletePassword(id,category);
				}
			}
		}.execute();
	}

	public void getPassword(final int id, final OnGetPasswordCallback onGetPasswordCallback)
	{
		new AsyncSingleTask<Password>()
		{
			@Override
			protected AsyncResult<Password> doInBackground(AsyncResult<Password> asyncResult)
			{
				Password password = passwordDatabase.getPassword(id);
				asyncResult.setData(password);
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Password> asyncResult)
			{
				onGetPasswordCallback.onGetPassword(asyncResult.getData());
			}
		}.execute();
	}

	public void updatePassword(final Password password)
	{
		new AsyncSingleTask<Void>()
		{
			@Override
			protected AsyncResult<Void> doInBackground(AsyncResult<Void> asyncResult)
			{
				int result = passwordDatabase.updatePassword(password);
				asyncResult.setResult(result);
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Void> asyncResult)
			{
				for (OnPasswordListener onPasswordListener : onPasswordListeners)
				{
					onPasswordListener.onUpdatePassword(password);
				}
			}
		}.execute();
	}

	public void insertPassword(final Password password)
	{
		new AsyncSingleTask<Password>()
		{
			@Override
			protected AsyncResult<Password> doInBackground(AsyncResult<Password> asyncResult)
			{
				int result = (int) passwordDatabase.insertPassword(password);
				password.setId(result);
				asyncResult.setData(password);
				return asyncResult;
			}

			@Override
			protected void runOnUIThread(AsyncResult<Password> asyncResult)
			{
				for (OnPasswordListener onPasswordListener : onPasswordListeners)
				{
					onPasswordListener.onNewPassword(asyncResult.getData());
				}
			}
		}.execute();
	}
}
