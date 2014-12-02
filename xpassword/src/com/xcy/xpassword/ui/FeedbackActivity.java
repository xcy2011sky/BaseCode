package com.xcy.xpassword.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.xcy.lib.annotation.FindViewById;
import com.xcy.xpassword.R;
import com.xcy.xpassword.app.BaseActivity;
import com.xcy.xpassword.web.Feedback;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


/**
 * 反馈
 * 
 * @author xcy
 * 
 */
public class FeedbackActivity extends BaseActivity
{
	@FindViewById(R.id.feedback_edittext)
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
	}

	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_feedback_send:
				onSendClick();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onSendClick()
	{
		String content = editText.getText().toString().trim();
		if (!content.equals(""))
		{
			final ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(getString(R.string.feedback_sending));
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(false);
			
			progressDialog.show();
			AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>()
			{
				@Override
				protected Boolean doInBackground(String... params)
				{
					Feedback feedback = new Feedback(FeedbackActivity.this);
					feedback.setAppName("XPassword");
					feedback.setFeedback(params[0]);
					feedback.setDeviceInfo(getDeviceInfo());

					boolean result=false;
					try
					{
						result=feedback.commit();
					
						Thread.sleep(600);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						result = false;
					}
					return result;
				}

				@Override
				protected void onPostExecute(Boolean result)
				{
					showToast(R.string.feedback_thanks);
					progressDialog.dismiss();
					finish();
				}
			};
			asyncTask.execute(content);
		}
	}

	public String getDeviceInfo()
	{
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("os", "Android");
			jsonObject.put("os_display", android.os.Build.DISPLAY);
			jsonObject.put("os_model", android.os.Build.MODEL);
			jsonObject.put("os_release", android.os.Build.VERSION.RELEASE);
			jsonObject.put("IMEI", tm.getDeviceId());
			jsonObject.put("wetwork_country_iso", tm.getNetworkCountryIso());
			jsonObject.put("version_code", getMyApplication().getVersionCode());
			jsonObject.put("version_name", getMyApplication().getVersionName());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
