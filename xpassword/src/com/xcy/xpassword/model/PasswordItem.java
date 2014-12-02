package com.xcy.xpassword.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;

import com.xcy.xpassword.R;

public class PasswordItem {

	private Context context;
	private SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM-dd", Locale.getDefault());

	private SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	private static final long DAY = 1000 * 60 * 60 * 24;

	
		public String dataString;
		public Password password;

		public PasswordItem(Context context,Password password)
		{
			this.password = password;
			this.context=context;
			initDataString();
		}

		public void initDataString()
		{
			dataString = fomartDate(password.getCreateDate());
		}
		private String fomartDate(long createDate)
		{
			String result = "";
			long currentTime = System.currentTimeMillis();
			long distance = currentTime - createDate;
			if (createDate > currentTime)
			{
				result = simpleDateFormatYear.format(createDate);
			}
			else if (distance < 1000 * 60)
			{
				result = context.getString(R.string.just);
			}
			else if (distance < 1000 * 60 * 60)
			{
				String dateString = context.getString(R.string.minute_ago);
				result = String.format(Locale.getDefault(), dateString, distance / (1000 * 60));
			}
			else if (distance < DAY)
			{
				String dateString = context.getString(R.string.hour_ago);
				result = String.format(Locale.getDefault(), dateString, distance / (1000 * 60 * 60));
			}
			else if (distance < (DAY * 7))
			{
				String dateString = context.getString(R.string.day_ago);
				result = String.format(Locale.getDefault(), dateString, distance / (DAY));
			}
			else if (distance < DAY * 30)
			{
				String dateString = context.getString(R.string.week_ago);
				result = String.format(Locale.getDefault(), dateString, distance / (DAY * 7));
			}
			else if (distance < DAY * 365)
			{
				result = simpleDateFormatMonth.format(createDate);
			}
			else
			{
				result = simpleDateFormatYear.format(createDate);
			}

			return result;
		}
		
}
