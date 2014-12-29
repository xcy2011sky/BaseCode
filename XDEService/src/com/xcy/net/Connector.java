package com.xcy.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class Connector {
	private Context mContext;
	private UDPConnector broadConn;
	private TCPConnector tcpConn;
	private ConnListener listener;
	private String localIP=null;
	private XDebug debug=new XDebug("Connector");
	
	

	public Connector(Context context,ConnListener mConnListener){
		this.mContext=context;
		this.listener=mConnListener;
		try {
			localIP=getLocalIpAddress().getHostAddress();
		} catch (UnknownHostException e) {
			debug.LogE("get LocalIP failed ");
			return;
			//e.printStackTrace();
		}
		broadConn=new UDPConnector(localIP,listener);
		tcpConn=new TCPConnector(localIP, mConnListener);
	}
	
	
	public void Connect(String client){
		String[] result=client.split(":");
		if(result.length!=2) return;
		tcpConn.Connect(result[0], Integer.parseInt(result[1]));
	}
	public void Disconnect(String client){
		
	}
	public void FindServer(){
		broadConn.FindService();
	}
	
	public void Loop(){
		broadConn.Loop();
		tcpConn.Loop();
		
	}
	private InetAddress getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager)mContext.getSystemService(android.content.Context.WIFI_SERVICE );
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return InetAddress.getByName(String.format("%d.%d.%d.%d",
                        (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                        (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
}
}
