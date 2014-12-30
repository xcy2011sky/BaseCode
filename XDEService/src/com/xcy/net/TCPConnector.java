package com.xcy.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;




public class TCPConnector {
	private Socket mSocket;
	private ServerSocket mServer;
	private ConnListener mListener;
	private String localIP=null;
	private int port=1989;
	private XDebug debug=new XDebug("TCPConnector",true);
	private boolean isRunning =true;
	
	BufferedReader reader=null;
	BufferedWriter writer=null;
	

	public TCPConnector(String localip,ConnListener listener)
	{
		this.localIP=localip;
		this.mListener=listener;
		mSocket=new Socket();
		try {
			mSocket.setKeepAlive(true);
		} catch (SocketException e1) {
			debug.LogE("msocket set keep alive failed ");
			//e1.printStackTrace();
		}
		try {
			mServer=new ServerSocket(port);
			mServer.bind(new InetSocketAddress(localIP, port));
		} catch (IOException e) {
			debug.LogE("init mServer failed ");
			return;
			//e.printStackTrace();
		}
		
		
	}
	public void Connect(String ip,int port){
		debug.LogI("ip="+ip+"port="+port);
		SocketAddress address =new InetSocketAddress(ip, port);
		if(mSocket.isConnected()){
			return;
		}
		if(mSocket.isClosed()){
			mSocket=new Socket();
			try {
				mSocket.setKeepAlive(true);
			} catch (SocketException e1) {
				debug.LogE("msocket set keep alive failed ");
				//e1.printStackTrace();
			}
		}
		try {
			mSocket.connect(address, 2000);
			if(mSocket.isConnected()){
				this.mListener.OnConnected();
			}
		} catch (IOException e) {
			debug.LogE("mSocket connect time out");
			
			//e.printStackTrace();
		}
	}
	public void Disconnect(){
	
		try {
			mSocket.close();
			if(mSocket.isClosed()){
				this.mListener.OnDisconnected();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Loop(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isRunning){
					
					if(mServer!=null){
						try {
							debug.LogI("tcp recv starting ");
							Socket 	c = mServer.accept();
							mListener.AddClient(c.getInetAddress().getHostAddress()+":"+c.getPort());
							HandlerSocket(c);
				
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else
					{
						try {
							mServer=new ServerSocket(port);
							mServer.bind(new InetSocketAddress(localIP, port));
						} catch (IOException e) {
							debug.LogE("init mServer failed ");
							return;
							//e.printStackTrace();
						}
					}
					
			
				}
			}
		}).start();
		
		
	}
	public void SendData(String cmd){
		try {
			OutputStreamWriter writer=new OutputStreamWriter(mSocket.getOutputStream());
			writer.write(cmd);
			writer.write("\n");
			writer.write("@bye");
			writer.write("\n");
			writer.flush();
			//writer.close();
			//mSocket.shutdownOutput();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void SendKey(int keycode,int action){
		String cmd="keyboard"+"@"+keycode+"@"+action+"@"+"other";
		SendData(cmd);
	}
	public void SendTouch(float x,float y,int action){
		String cmd="touch"+"@"+x+"@"+y+"@"+action;
		SendData(cmd);
	}
	
	private void HandlerCMD(String cmd){
		String result[]=cmd.split("@");
		if(result.length<4)return;
		if("keyboard".equalsIgnoreCase(result[0])){
			int keycode=Integer.parseInt(result[1]);
			int action=Integer.parseInt(result[2]);
			debug.LogI("keycode="+keycode+" action= " +( (action== 1) ?"down":"up "));
			this.mListener.HandlerKey(keycode, action);
			
		}else if("touchscreen".equalsIgnoreCase(result[0])){
			int x=Integer.parseInt(result[1]);
			int  y=Integer.parseInt(result[2]);
			int action=Integer.parseInt(result[3]);
			debug.LogI("x="+x+" y="+y+"action= " +( (action== 1) ?"down":"up "));
			this.mListener.HandlerTouch(x, y, action);
		}
	}

	private void HandlerSocket(final Socket c){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!(c.isClosed())){
					String str;
					StringBuffer result=new StringBuffer();
					try {
						reader=new BufferedReader(new InputStreamReader(c.getInputStream()));
						debug.LogI("tcp recv starting ");
						while(!((str=reader.readLine()).equalsIgnoreCase("@bye"))){
								result.append(str);
								debug.LogI("tcp recv str"+str);
						}
						debug.LogI("tcp recv done "+result.toString());
						HandlerCMD(result.toString());	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						writer=new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
		}).start();
	}
}
