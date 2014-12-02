package com.xcy.xpassword.app;



import android.util.Log;

public class XDebug  {

	private String TAG=null;
	private boolean Flag=false;
	public XDebug(String tag,boolean flag)
	{
		this.TAG=tag;
		this.Flag=flag;
	}
	public  void LogV(String msg){
		
		if(this.Flag)
		{
			Log.v(TAG, msg);
			
		}
	}
	
	public  void LogI(String msg){
		
		if(this.Flag)
		{
			Log.i(TAG, msg);
			
		}
	}
	public  void LogE(String msg){
		
		if(this.Flag)
		{
			Log.e(TAG, msg);
			
		}
	}
	
	

}
