package com.xcy.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import javax.swing.plaf.multi.MultiButtonUI;

public class UDPServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		MulticastSocket socket=new MulticastSocket();
		try {
			DatagramSocket ds=new DatagramSocket(1989);
			ds.setSoTimeout(10);
			while(true)
			{
				byte[]buf =new byte[1024];
				DatagramPacket dp=new DatagramPacket(buf,0,buf.length);
				ds.receive(dp);
				System.out.println("udp server !!!!!!!!!!!!!!!!!1");
				String data=new String(dp.getData(),0,dp.getLength());
				System.out.println(data +" send by "+dp.getAddress().getHostName()+":"+dp.getPort());
				String feedback=data+" from server";
				DatagramPacket cdp=new DatagramPacket(feedback.getBytes(), feedback.getBytes().length,InetAddress.getByName("127.0.0.1"),1988);
				ds.send(cdp);
				
				
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
