package com.xcy.structural;

/**
 * 适配器模式：将一个类的接口转换为客户端期待的接口
 * 类适配和对象适配器模式
 * 角色：
 * 目标：客户期待的接口
 * 源：需要适配的类
 * 适配器：转换接口实现
 * 
 * 
 * @author chongyan_xu
 *
 */

interface ITarget{
	public void Print(String ver);
}

class Adaptee{
	public void PutOut(int value)
	{
		System.out.println("you enter value="+value);
	}
}
/*----------------------------------------------------------------------------------------------------------类适配-----------------------------------------------------------------------*/
class Adapter extends Adaptee implements ITarget
{

	@Override
	public void Print(String var) {
	   int value=Integer.parseInt(var);
		this.PutOut(value);
	}
	
}


/*------------------------------------------------------------------------------------------------------对象适配-------------------------------------------------------------------------*/


class AdapterObject implements ITarget {

	 private Adaptee adaptee=new Adaptee();
	@Override
	public void Print(String ver) {
	
		 adaptee.PutOut(Integer.parseInt(ver));
		
	}
	
}




/*==================================================================client==========================================*/

public class AdapterPatternDamo {

	public static void main(String []args){
		ITarget target=new Adapter();
		target.Print("123");
		ITarget oTarget = new AdapterObject();
		oTarget.Print("234");
		
	}
	
}
