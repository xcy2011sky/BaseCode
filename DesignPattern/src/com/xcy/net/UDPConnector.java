package com.xcy.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPConnector {
	private static final String BROADCAST_IP="230.0.0.2";
	private static final int BROADCAST_PORT=1988;
	private InetAddress broadAddress;
	
	private boolean isRunning=true;
	
	private MulticastSocket broadSocket;
	private DatagramSocket sender;
	
	interface ConnectorListener{

		void onDiscover(String ip,String name);
		void onOffLine(String ip,String name);
	}
	private ConnectorListener listener;
	
	public UDPConnector(){
		
		try
		{
			broadSocket=new MulticastSocket(BROADCAST_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Binder socket failed ");
		}
		
		try {
			sender=new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			broadAddress=InetAddress.getByName(BROADCAST_IP);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setListener(ConnectorListener mListener){
		this.listener=mListener;
	}
	
	public void Loop(){
		try {
			broadSocket.joinGroup(broadAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("joinGroup failed");
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isRunning){
					DatagramPacket packet=new DatagramPacket(new byte[1024*8], 1024*8);
					try {
						broadSocket.receive(packet);
						HanderCmd(packet);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	private  void HanderCmd(DatagramPacket dp) throws UnknownHostException{
		DatagramPacket packet;
		String []msg=new String(dp.getData(),0,dp.getLength()).split("@");
		if(msg==null||msg.length!=3) return;
		String ip=msg[1];
		String hostname=msg[2];
		
		if(msg[0].equals("find")){
			
			String feedback="confirm@"+InetAddress.getLocalHost().getHostAddress().toString()+"@"+InetAddress.getLocalHost().getHostName();
			packet=new DatagramPacket(feedback.getBytes(), feedback.getBytes().length,broadAddress,BROADCAST_PORT);
			try {
				sender.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(msg[0].equalsIgnoreCase("confirm")){
			if(this.listener!=null)
			this.listener.onDiscover(ip,hostname);
			
		}else if(msg[0].equalsIgnoreCase("off")){
			if(this.listener!=null)
			this.listener.onOffLine(ip,hostname);
		}
		
	}

	public void FindService() throws UnknownHostException{
		String sql="find@"+InetAddress.getLocalHost().getHostAddress().toString()+"@"+InetAddress.getLocalHost().getHostName();
	   DatagramPacket	packet=new DatagramPacket(sql.getBytes(), sql.getBytes().length,broadAddress,BROADCAST_PORT);
		try {
			sender.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Destory(){
		isRunning=false;
	}
}
