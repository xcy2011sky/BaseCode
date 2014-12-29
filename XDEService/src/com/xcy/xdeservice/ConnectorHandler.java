package com.xcy.xdeservice;


import com.xcy.net.ConnListener;
import com.xcy.net.Connector;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ConnectorHandler extends Handler{
	public static final int INIT_CONN=1;
	public static final int CONN_LOOP=2;
	public static final int FIND_SERVER=3;
	public static final int CONNECT=4;
	private Connector Conn;
	private ConnListener mListener;
	private Context mContext;

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
			break;
		case CONN_LOOP:
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
		}
		super.handleMessage(msg);
	}

}
