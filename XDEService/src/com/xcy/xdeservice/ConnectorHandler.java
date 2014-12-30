package com.xcy.xdeservice;


import com.xcy.net.ConnListener;
import com.xcy.net.Connector;
import com.xcy.net.XDebug;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ConnectorHandler extends Handler{
	public static final int INIT_CONN=1;
	public static final int CONN_LOOP=2;
	public static final int FIND_SERVER=3;
	public static final int CONNECT=4;
	public static final int SEND_KEY=5;
	public static final int SEND_TOUCH=6;
	private Connector Conn;
	private ConnListener mListener;
	private Context mContext;
	private XDebug debug=new XDebug("ConnctorHandler");

	public ConnectorHandler(ConnListener listener,Context context,Looper looper){
		super(looper);
		this.mListener=listener;
		this.mContext=context;

	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case INIT_CONN:
			Conn=new Connector(mContext, mListener);
			Conn.Loop();
			break;
		case FIND_SERVER:
			Conn.FindServer();
			break;
		case CONNECT:
				String target=msg.getData().getString("target");
				if(target!=null)
				Conn.Connect(target);
			break;
		case SEND_KEY:
			int keycode=msg.getData().getInt("keycode");
			debug.LogI("keycode="+keycode);
			Conn.SendKey(keycode, 0);
			
		break;
		case SEND_TOUCH:
//			String target=msg.getData().geti("touch");
//			if(target!=null)
//			Conn.Connect(target);
		break;
		}
		super.handleMessage(msg);
	}

}
