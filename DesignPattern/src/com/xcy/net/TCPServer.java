package com.xcy.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) {

		BufferedReader reader;
		BufferedWriter writer;

		try {
			ServerSocket ss = new ServerSocket(6000);
			while (true) {
				Socket s = ss.accept();
				System.out.println("accept s.ip"
						+ s.getInetAddress().getHostAddress().toString() + " :"
						+ s.getPort());
				reader = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				FileOutputStream fout = new FileOutputStream(new File(
						"./test2.txt"));

				writer = new BufferedWriter(new OutputStreamWriter(
						s.getOutputStream()));
				String str;
				while ((str = reader.readLine()) != null) {
					System.out.println(str);
					// fout.write(str.getBytes());
					// out.flush();
				}
				int flag = 10;
				while (flag > 0) {
					Thread.sleep(6000);
					System.out.println(flag);
					flag--;
				}

				writer.write("this is value from ss");
				writer.flush();
				s.shutdownOutput();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("create ss failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("create ss failed");
			e.printStackTrace();
		}
	}
}
