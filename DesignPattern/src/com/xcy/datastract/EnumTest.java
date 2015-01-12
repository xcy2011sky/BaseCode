package com.xcy.datastract;

public class EnumTest {
	enum XResType{
		KEYBOARD,
		TOUCH,
		IMAGE
	}

	public static void main(String []args){
		System.out.println(XResType.KEYBOARD);
		System.out.println(XResType.KEYBOARD.ordinal());
		System.out.println(XResType.IMAGE.ordinal());
		System.out.println(XResType.valueOf("KEYBOARD"));
	}
}
