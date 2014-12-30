package com.xcy.xdeservice;

import com.xcy.net.ConnListener;
import com.xcy.net.Connector;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.MotionEvent;

public class XDEService extends Service implements ConnListener{
private Instrumentation simulate;
private Connector conn;
private Context mContext;
private ConnListener mListener;
	
public XDEService()
{
	simulate=new Instrumentation();
	mContext=this;
	mListener=this;
	new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			conn=new Connector(mContext,mListener);
			conn.Loop();
		}
	}).start();
}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void OnFind(String ips) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddClient(String client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveClient(String client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HandlerKey(int keycode, int action) {
		// TODO Auto-generated method stub
		simulate.sendKeyDownUpSync(keycode);
	}

	@Override
	public void HandlerTouch(int x,int y, int action) {
		// TODO Auto-generated method stub
		MotionEvent event=MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0); 
		simulate.setInTouchMode(true);
		simulate.sendPointerSync(event);
	}

}
