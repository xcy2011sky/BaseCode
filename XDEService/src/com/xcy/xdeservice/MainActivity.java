package com.xcy.xdeservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;











import com.xcy.net.ConnListener;
import com.xcy.net.XDebug;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements ConnListener {
	 MulticastLock 	multicastLock;
	 public ConnectorHandler mHandler;
	 HandlerThread ConnThread=new HandlerThread("Connector");
	
	 private XDebug debug=new XDebug(MainActivity.class.getSimpleName(),true);
	 public List<String>ipList=new ArrayList<String>();
	 TextView tv;
    StringBuffer strIPS=new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		allowMulticast();
		ConnThread.start();
		mHandler=new ConnectorHandler(this, this,ConnThread.getLooper());
		mHandler.sendEmptyMessage(ConnectorHandler.INIT_CONN);
		mHandler.sendEmptyMessage(ConnectorHandler.CONN_LOOP);
		
		
		debug.LogI("MainActivity :: onCreate");

		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
		

		private MainActivity mActivty;

		@Override
		public void onAttach(Activity activity) {
			mActivty=(MainActivity)activity;
			super.onAttach(activity);
		}

		public PlaceholderFragment() {
		
	
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			Button btnFind=(Button)rootView.findViewById(R.id.button1);
			TextView tv=(TextView)rootView.findViewById(R.id.textView1);
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
	// TODO Auto-generated method stub
	
}

@Override
public void RemoveClient(String client) {
	// TODO Auto-generated method stub
	
}

@Override
public void HandlerKey(int keycode, int action) {
	// TODO Auto-generated method stub
	debug.LogI("HandlerKey keycode="+keycode+"action="+action);
}

@Override
public void HandlerTouch(float x, float y, int action) {
	// TODO Auto-generated method stub
	
}

}
