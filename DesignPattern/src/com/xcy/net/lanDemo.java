package com.xcy.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

class lanSend{
	private static final String BROADCAST_IP="230.0.0.1";
	private static final int BROADCAST_PORT=1988;
	MulticastSocket broadSocket;
	InetAddress broadAddress;
	DatagramSocket sender;
	
	public lanSend(){
		System.out.println("lanSend onCreate ");
		try {
			broadSocket=new MulticastSocket( BROADCAST_PORT);
			broadAddress=InetAddress.getByName(BROADCAST_IP);
			sender=new DatagramSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void join(){
		System.out.println("lanSend onJoin ");
		try {
			broadSocket.joinGroup(broadAddress);
			broadSocket.setTimeToLive(1);
			new Thread(new GetPacket()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void SendGetUserMsg(){
		System.out.println("lanSend onFind  ");
		byte[]b=new byte[1024];
		DatagramPacket packet;
		b=("find@ "+lanDemo.msg).getBytes();
		packet=new DatagramPacket(b, b.length,broadAddress,BROADCAST_PORT);
		try {
			sender.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void returnUserMs(String ip) throws IOException{
		System.out.println("lanSend onReturn  ");
		byte[]b=new byte[1024];
		DatagramPacket packet ;
		b=("return@"+lanDemo.msg).getBytes();
		packet=new DatagramPacket(b, b.length,broadAddress,BROADCAST_PORT);
		sender.send(packet);
	}
	
	void offLine() throws IOException{
		System.out.println("lanSend onffline ");
		byte[]b=new byte[1024];
		DatagramPacket packet ;
		b=("off@"+lanDemo.msg).getBytes();
		packet=new DatagramPacket(b, b.length,broadAddress,BROADCAST_PORT);
        broadSocket.setTimeToLive(0);
		sender.send(packet);
	}
	class GetPacket implements Runnable{

	
		
		@Override
		public void run() {
			DatagramPacket packet;
			String [] message;
			while(true){
				packet=new DatagramPacket(new byte[1024], 1024);
				try {
					broadSocket.receive(packet);
					message=new String (packet.getData(),0,packet.getLength()).split("@");
			//		if(message[1].equals(lanDemo.ip)) continue;
					if(message[0].endsWith("find")){
						System.out.println("new user online: ip" +message[1]+" host "+message[2]);
						returnUserMs(message[1]);
					}else if(message[0].equals("return")){
						System.out.println(" user onlined : ip" +message[1]+" host "+message[2]);
					}else if(message[0].equals("off")){
						System.out.println(" user off line : ip" +message[1]+" host "+message[2]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
}

public class lanDemo {

	public static String msg;
	public static String ip;
	public static String hostName;
	public static void main(String [] args) throws InterruptedException{
		lanSend newSend;
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			ip=addr.getHostAddress().toString();
			hostName="xcy8";
			msg=ip+"@"+hostName;
			newSend=new lanSend();
			newSend.join();
			newSend.SendGetUserMsg();
			Thread.sleep(4000);
			try {
				newSend.offLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("on offline fail ");
			}
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
}
