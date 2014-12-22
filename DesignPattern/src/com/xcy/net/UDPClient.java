package com.xcy.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

	public static void main(String[] args) throws IOException {
		
		DatagramSocket ds=null;
		try {
			ds=new DatagramSocket(1988);  //DatagramSocket //创建udp的服务器的socket
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str="udp client say";
		InetAddress destination=null;
		
		destination=InetAddress.getLocalHost();
		//DatagramPacket 数据封包，发送数据要有inetaddress, 
	DatagramPacket dp=new DatagramPacket(str.getBytes(), str.getBytes().length,InetAddress.getByName("127.0.0.1"),1989);
	
		try {
			ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[]buf=new byte[1024];
		//接受数据只需buf和长度
		DatagramPacket sdp=new DatagramPacket(buf,buf.length);
		ds.receive(sdp);
		System.out.println("recv data from "+sdp.getAddress().getHostAddress()+":"+sdp.getPort());
		System.out.println("receive2 data="+new String(sdp.getData()) +" send by "+sdp.getAddress().getHostAddress()+":"+sdp.getPort());
		
		ds.close();
		

	}
}
