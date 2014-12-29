package com.xcy.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class TCPClient {

	public static void main(String[] args) throws UnknownHostException {

		Socket s = new Socket();
		SocketAddress address;
		BufferedReader reader;
		BufferedWriter writer;
		byte[] fbuf = new byte[1024 * 4];
		int count = 0;


		File f1 = new File("./test.txt");
		if (!f1.exists()) {
			System.out.println("test.apk this file is not exists");
		}

		address = new InetSocketAddress(InetAddress.getLocalHost(), 6000);

		try {
			s.connect(address, 2000);
			try {
				FileInputStream fin = new FileInputStream(f1);
				OutputStream os = s.getOutputStream();
				while ((count = fin.read()) != -1) {
					os.write(count);
					os.flush();

				}
				s.shutdownOutput();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("File not Found Exception");
				return;
			}
			reader = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				// String line=reader.readLine();
				System.out.println(line);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
