package com.xcy.xdeservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;















import com.xcy.net.ConnListener;
import com.xcy.net.XDebug;

import android.app.Activity;
import android.app.Fragment;
import android.app.Instrumentation;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements ConnListener {
	 MulticastLock 	multicastLock;
	 public Handler mHandler;
	 HandlerThread ConnThread=new HandlerThread("Connector");
	
	static  XDebug debug=new XDebug(MainActivity.class.getSimpleName(),true);
	 public List<String>ipList=new ArrayList<String>();

    StringBuffer strIPS=new StringBuffer();
    Fragment mainFragment=null;

    private 
    class MainHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				debug.LogE("mainHandler.handlermsg.thread.id="+Thread.currentThread().getId());
				((PlaceholderFragment)mainFragment).setTv("test app ");
				break;
			}
			super.handleMessage(msg);
		}
    	
    }
    public MainHandler mainHandler;
   private Instrumentation simulate;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		allowMulticast();
		simulate=new Instrumentation();
		ConnThread.start();
		mHandler=new ConnectorHandler(this, this,ConnThread.getLooper());
		mHandler.sendEmptyMessage(ConnectorHandler.INIT_CONN);
		mainHandler=new MainHandler();
	
		if (savedInstanceState == null) {
			mainFragment=new PlaceholderFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, mainFragment).commit();
		}
	}

	@Override
	protected void onStop() {
		releaseMulticast();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		static int i=0;
		private MainActivity mActivty;
		public TextView tv;
	
		@Override
		public void onAttach(Activity activity) {
			mActivty=(MainActivity)activity;
			super.onAttach(activity);
		}
		public PlaceholderFragment() {
		}
		

		public void setTv(String str){
			tv.setText(str);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			Button btnFind=(Button)rootView.findViewById(R.id.button1);
			tv=(TextView)rootView.findViewById(R.id.textView1);
			Button btnConn=(Button)rootView.findViewById(R.id.button2);
			btnConn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Message msg=new Message();
					msg.what=ConnectorHandler.CONNECT;
					Bundle bundle=new Bundle();
					if(mActivty.ipList!=null)
						bundle.putString("target",mActivty.ipList.get(0));
					else{
						bundle.putString("target",null);
					}
					msg.setData(bundle);
				
					mActivty.mHandler.sendMessage(msg);
				}
			});
			
			btnFind.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Message msg=new Message();
					msg.what=ConnectorHandler.FIND_SERVER;
				    mActivty.mHandler.sendMessage(msg);
				}
			});
			Button btnKey=(Button)rootView.findViewById(R.id.button3);
			btnKey.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				Message msg=new Message();
				msg.what=ConnectorHandler.SEND_KEY;
				Bundle bundle=new Bundle();
				int keycode=KeyEvent.KEYCODE_MENU;
					switch(i%4){
					case 0:
					 keycode=KeyEvent.KEYCODE_VOLUME_DOWN;
						debug.LogI("send key volume down");
						break;
					case 1:
						keycode=KeyEvent.KEYCODE_VOLUME_UP;
						debug.LogI("send key volume up");
						break;
					case 2:
						keycode=KeyEvent.KEYCODE_VOLUME_MUTE;;
						debug.LogI("send keymute");
						break;
					case 3:
						keycode=KeyEvent.KEYCODE_CALL;;
						debug.LogI("send key left");
						break;
					}
					bundle.putInt("keycode", keycode);
					msg.setData(bundle);
					mActivty.mHandler.sendMessage(msg);
					i++;
				}
				
			});
			
			
			return rootView;
		}
	
	
}
	private void allowMulticast(){

		WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		multicastLock=wifiManager.createMulticastLock("multicast.test");
		
		multicastLock.acquire();
		
		}
private void releaseMulticast(){
    multicastLock.release();
}


@Override
public void OnFind(String ips) {
	// TODO Auto-generated method stub
	debug.LogI("OnFind::"+ips);
	strIPS.append(ips);
	strIPS.append("\n");
	ipList.add(ips);
	debug.LogI("OnFind::"+strIPS.toString());
	debug.LogE("OnFind::thread.id="+Thread.currentThread().getId());
	//mainHandler.sendEmptyMessage(1);
	mainHandler.post(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			debug.LogE("OnFind.mainHandler.post::thread.id="+Thread.currentThread().getId());
			((PlaceholderFragment)mainFragment).setTv(strIPS.toString());
		}
	});
}

@Override
public void OnConnected() {
	// TODO Auto-generated method stub
	debug.LogI("OnConnected");
}

@Override
public void OnDisconnected() {
	// TODO Auto-generated method stub
	
}

@Override
public void AddClient(String client) {
	debug.LogI("AddClient="+client);
	
}

@Override
public void RemoveClient(String client) {
	// TODO Auto-generated method stub
	
}

@Override
public void HandlerKey(int keycode, int action) {
	// TODO Auto-generated method stub
	debug.LogI("HandlerKey keycode="+keycode+"action="+action);
	simulate.sendKeyDownUpSync(keycode);
}

@Override
public void HandlerTouch(int x, int y, int action) {
	// TODO Auto-generated method stub
	debug.LogI("Handler touch x="+x+"  y ="+y+"  action="+action);
	MotionEvent event=MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0); 
	simulate.setInTouchMode(true);
	simulate.sendPointerSync(event);
}

}
