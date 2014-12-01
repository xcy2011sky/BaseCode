package com.xcy.creational;

import java.util.ArrayList;
/**
 * 原型模式：用原型实例创建对象。
 * 条件：１．实现Cloneable interface 2.overwrite clone of Object method 
 * 优点：
 * 　　１．使用原型模式比ｎｅｗ一个对象性能上较好。
 * 		２．创建对象简单。
 * 注意点：１．原型创建的对象不会调用构造函数的方法。
 * 					２．深拷贝和前拷贝的问题
 * 	在ｊａｖａ中８中基本类型和Ｓｔｒｉｎｇ类型时深拷贝，其他的都是浅拷贝。
 * 例子中ArrayList 不是基本类型，因此需要深拷贝,但是在实际的操作过程，不单独拷贝也是可以的。
 * 看来java 1.6后默认都是深拷贝啦。这只是猜测。
 * @author chongyan_xu
 *
 */


class Prototype  implements Cloneable{
	
	private ArrayList<String> lists=new ArrayList<String>();
	

	public Prototype()
	{
		System.out.println("Prototype  is create ");
		lists.add("one");
		lists.add("two");
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Prototype prototype=null;
		
		prototype=(Prototype)super.clone();
//		prototype.lists=(ArrayList<String>) this.lists.clone();
	
		
		return prototype;
	}
	
	public void Show()
	{
		System.out.println("Prototype  implement Class");
		for (String v: this.lists)
			System.out.println("l ist:"+v);
	}
}


/*=========================================================PrototypePattern Client==============================*/


public class PrototypePatternDamo {

	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Prototype p=new Prototype();
		Prototype c=(Prototype) p.clone();
		c.Show();

	}

}
