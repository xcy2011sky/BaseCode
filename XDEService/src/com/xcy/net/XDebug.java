package com.xcy.net;

import android.util.Log;

public class XDebug {
	private String Tag="XDebug";
	private boolean debug=true;
	public XDebug(String tag,boolean flag){
		this.Tag=tag;
		this.debug=flag;
	}
	public XDebug(String tag){
		this.Tag=tag;
	}
	public void SetDebug(boolean debug){
		this.debug=debug;
	}
	public void LogI(String msg){
		if(debug){
			Log.i(Tag, msg);
		}
	}
	public void LogE(String msg){
		if(debug){
			Log.e(Tag, msg);
		}
	}
}
