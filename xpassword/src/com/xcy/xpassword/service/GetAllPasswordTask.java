package com.xcy.xpassword.service;

import java.util.List;

import com.xcy.xpassword.database.PasswordDatabase;
import com.xcy.xpassword.model.AsyncResult;
import com.xcy.xpassword.model.AsyncSingleTask;
import com.xcy.xpassword.model.Password;



public class GetAllPasswordTask extends AsyncSingleTask<List<Password>>
{
	private PasswordDatabase passwordDatabase;
	private OnGetAllPasswordCallback onGetAllPasswordCallback;

	public GetAllPasswordTask(PasswordDatabase passwordDatabase,
			OnGetAllPasswordCallback onGetAllPasswordCallback)
	{
		this.passwordDatabase = passwordDatabase;
		this.onGetAllPasswordCallback = onGetAllPasswordCallback;
	}

	@Override
	protected AsyncResult<List<Password>> doInBackground(AsyncResult<List<Password>> asyncResult)
	{
		List<Password> passwords = passwordDatabase.getAllPassword();
		asyncResult.setData(passwords);
		return asyncResult;
	}

	@Override
	protected void runOnUIThread(AsyncResult<List<Password>> asyncResult)
	{
		onGetAllPasswordCallback.onGetAllPassword(asyncResult.getData());
	}

}
