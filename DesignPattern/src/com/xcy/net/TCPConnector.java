package com.xcy.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class TCPConnector {
	private Socket cSocket;
	private ServerSocket sSocket;
	private boolean isRunning=true;
	private TCPConnListener listener;
	private StringBuffer strContent;
	private List<Socket>clients=new ArrayList<Socket>();
	
	public  void  setListener(TCPConnListener listener){
		this.listener=listener;
	}
	
	interface TCPConnListener{
		public void addClient(String ip,int port);
	}
	
	public TCPConnector(int port) throws IOException{
		
		sSocket=new ServerSocket(port);
		
		cSocket=new Socket();
		
		strContent=new StringBuffer();
	}
	
	public void ConnectServer(String ip,int port){
		SocketAddress address=new InetSocketAddress(ip, port);
		try {
			cSocket.connect(address,2000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Connect "+ip+":"+port+" failed ");
			return ;
		}
		
	}
	
	public void SendData(byte[]data,int off ,int length){
		try {
			cSocket.setSoTimeout(2000);
			OutputStream ops=cSocket.getOutputStream();
			ops.write(data,off,length);
			ops.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("send data failed ");
		}
		
		
	}

	public void RecvData(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				StringBuffer tmpBuffer=new StringBuffer();;
				try {
					BufferedReader reader=new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
				    String str;
				    while((str=reader.readLine())!=null){
				    	tmpBuffer.append(str);
				    }
				    HandlerRecv(tmpBuffer.toString());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
	
	public void Loop(){
		new Thread(new Runnable() {
			BufferedReader reader=null;
			String ip;
			int port;
			@Override
			public void run() {
				while(isRunning){
					Socket c;
					try {
						c = sSocket.accept();
						ip=c.getInetAddress().getHostAddress().toString();
						port=c.getPort();
						listener.addClient(ip, port);
						clients.add(c);
						reader=new BufferedReader(new InputStreamReader(c.getInputStream()));
						String str;
						while((str=reader.readLine())!=null){	
							strContent.append(str);
						}
						HandlerCMD(strContent.toString());
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				}
				
			}
		}).start();
	}

	private void HandlerCMD(String cmd){
		System.out.println(cmd);
		
	}
   private void HandlerRecv(String cmd){
	   
   }
	
}
