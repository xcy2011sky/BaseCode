package com.xcy.xpassword.web;

import com.xcy.xpassword.app.XApplication;

import android.content.Context;
import android.widget.Toast;

public class Feedback {
	
	private Context mContext=null;
	private String appName=null;
	private String content=null;
	private String deviceInfo=null;
	
	public Feedback(Context context)
	{
		this.mContext=context;
	}
	
	public void setAppName(String string) {
		// TODO Auto-generated method stub
		this.appName=string;
	}

	public void setFeedback(String string) {
		// TODO Auto-generated method stub
		this.content=string;
	}

	public void setDeviceInfo(String deviceInfo) {
		// TODO Auto-generated method stub
		this.deviceInfo=deviceInfo;
	}

	public boolean commit() {
		// TODO Auto-generated method stub
		if(appName==null ||content==null||deviceInfo==null)
		{
			Toast.makeText(mContext, "请填写您的宝贵建议！！", 200).show();
			return false;
		}
		if(XApplication.NetState==false)
		{
			Toast.makeText(mContext, "请链接网络后，再次提交", 200).show();
			return false;
		}
		
		new Thread()
		{
			public void run() {
				XHttpClient client=new XHttpClient("http://1.xcy2011sky.sinaapp.com/xpassword/");
				String []param={appName,content,deviceInfo};
				client.Post(param);
				
			};
			
		}.start();
		
		return false;
	}

}
