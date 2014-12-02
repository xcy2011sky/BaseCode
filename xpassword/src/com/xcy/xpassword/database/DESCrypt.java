package com.xcy.xpassword.database;

public class DESCrypt {

	static {
		System.loadLibrary("descrypt");
	}
	
	public native String encrypt (String text);
	
	public native String  decrypt(String value);
	
}
