package com.xcy.creational;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单例模式
 * 优点：
 * １．唯一实例受控访问，可以控制客户怎么样或何时访问。
 * ２．缩小命令空间，Singleton　模式时对于全局变量的改进。
 * ３．允许对操作和Ｓingleton类可以子类化。
 * 
 * @author chongyan_xu
 *
 */

class Singleton{
	private static Singleton instance=null;
	private Singleton()
	{
		System.out.println("create new singleton !!");
	}
	public static Singleton getInstance()
	{
		if(instance!=null)
			return instance;
		instance=new Singleton();
		return instance;
	}
	public void  Print()
	{
		System.out.println("singleton  print   ");
	}
}

/*============================================================多例模式===================================*/

class SingletonMore{
	private static Map<Integer,SingletonMore> instances=new HashMap<Integer, SingletonMore>() ;

	private  int uid;
	
	private SingletonMore(int uid)
	{
		System.out.println("create new SingletonMore !!");
		this.uid=uid;
	}
	
	public static  SingletonMore  getInstance(int uid)
	{
	
		if(instances==null||instances.get(uid)==null)
		{
			SingletonMore s=new SingletonMore(uid);
			instances.put(uid, s);
			return instances.get(uid);

		}
		return instances.get(uid);

	}
	
	public void  Print()
	{
		System.out.println("singletonMore  print   ");
	}
	
}


/*================================================SingletonPatternDemo==============================*/

public class SingletonPatternDamo {
	 public static void main(String []args)
	 {
		 Singleton  instace=Singleton.getInstance();
		 
		 instace.Print();
		 
		 SingletonMore sm=SingletonMore.getInstance(0);
		 sm.Print();
		 SingletonMore sm1=SingletonMore.getInstance(1);
		 sm1.Print();
			 
	 }
}
