package com.xcy.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;



import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class UDPConnector {
	private Context mContext;
	private static final String BROADCAST_IP="224.0.0.4";
	private static final int BROADCAST_PORT=1988;
	private InetAddress group;
	private int TTL=4;
	private static final int  DATA_LEN=1024;
	private boolean isRunning=true;
	private boolean isServer;
	
	private String localIP=null;
	private int localPort=1989;

	private MulticastSocket  mSocket;
	private XDebug debug=new XDebug("UDPConnector", true);
	private ConnListener listener=null;
	
	public UDPConnector(String localip,ConnListener mListener){
		this.isServer=true;
		this.listener=mListener;
		try {
			debug.LogI("udpConnector init ");
			
			group=InetAddress.getByName(BROADCAST_IP);
			mSocket=new MulticastSocket(BROADCAST_PORT);	
			mSocket.setTimeToLive(TTL);
			localIP=localip;
			
			debug.LogI("localIP="+localIP);
			
		} catch (UnknownHostException e) {
			debug.LogE("init group inetadress failed ");
			e.printStackTrace();
		} catch (IOException e) {
			debug.LogE("init mSocket MulticastSocket  failed");
			e.printStackTrace();
		}
	}



	public UDPConnector(String localip,ConnListener mListener,boolean isServer){
	
			this.listener=mListener;
		try {
			debug.LogI("udpConnector init ");
			
			group=InetAddress.getByName(BROADCAST_IP);
			mSocket=new MulticastSocket(BROADCAST_PORT);	
			mSocket.setTimeToLive(TTL);
			localIP=localip;
			
			debug.LogI("localIP="+localIP);
			
		} catch (UnknownHostException e) {
			debug.LogE("init group inetadress failed ");
			e.printStackTrace();
		} catch (IOException e) {
			debug.LogE("init mSocket MulticastSocket  failed");
			e.printStackTrace();
		}
	}

	public void Loop(){
		try {
			mSocket.joinGroup(group);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(isRunning){
						
					DatagramPacket dp=new DatagramPacket(new byte[DATA_LEN], DATA_LEN);
					try {
						mSocket.receive(dp);
						debug.LogI("data:"+new String(dp.getData(),0,dp.getLength()));
						
						String result[]=new String (dp.getData(),0,dp.getLength()).split("@");
						
					
						if(result.length<4||localIP.equalsIgnoreCase(result[1])){
							//debug.LogI("localIP="+localIP+"  result[1]="+result[1] +" igone from self");
						}else{
							if("find".equalsIgnoreCase(result[0])){
								debug.LogI("find :: sender confirm");
								if(isServer) 	SendData("confirm");
								
							}else if("confirm".equalsIgnoreCase(result[0])){
								String client=result[1]+":"+result[2];
								listener.OnFind(client);
								debug.LogI("find service: "+client);
							}else if("off".equalsIgnoreCase(result[0])){
								String client=result[1]+":"+result[2];
								debug.LogI("offLine: "+client);
							}
						}
						
					
						
					} catch (IOException e) {
						debug.LogE("onUPD::Loop recevie dp failed");
						e.printStackTrace();
					}
					
				}
			}
				
			}).start();
		
		} catch (IOException e) {
			debug.LogE("mSocket.join Group failed");
			e.printStackTrace();
		}
	}

	public void FindService(){
		SendData("find");
	}
	private  void SendData(String type){
		String  cmd=type+"@"+localIP+"@"+localPort+"@"+"msg";
		DatagramPacket dp =new DatagramPacket(cmd.getBytes(), cmd.getBytes().length,group,BROADCAST_PORT);
		try {
			mSocket.send(dp);
		} catch (IOException e) {
			debug.LogE("send data failed");
			e.printStackTrace();
		}
	}

	
}
