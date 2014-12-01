package com.xcy.structural;
/**
 * 外观模式：　为子系统中的一组接口提供一个统一的界面，facade 定义一个高层是接口。
 * 
 * 适用性：
 * @author chongyan_xu
 *
 */
class Cpu{
	public void Startup()
	{
		System.out.println("Cpu is startup ");
	}
	public void Shutdown()
	{
		System.out.println("Cpu is shutdown ");
	}
}

class Memery{
	public void Startup()
	{
		System.out.println("Memery  is startup ");
	}
	public void Shutdown()
	{
		System.out.println("Memery is shutdown ");
	}
}

class Disk{
	public void Startup()
	{
		System.out.println("Disk is startup ");
	}
	public void Shutdown()
	{
		System.out.println("Disk  is shutdown ");
	}
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

//统一接口
class Computer{
	private Cpu cpu=null;
	private Memery men=null;
	private Disk disk=null;
	
	public Computer()
	{
		cpu=new Cpu();
		men=new Memery();
		disk=new Disk();
		
	}
	public void Startup()
	{
		System.out.println("computer  is startup ");
		cpu.Startup();
		men.Startup();
		disk.Startup();
		
	}
	public void Shutdown()
	{
		
		System.out.println("computer is startup ");
		cpu.Shutdown();
		men.Shutdown();
		disk.Shutdown();
		
	}
}


/*===========================================================client===========================================*/

public class FacadePatternDamo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Computer computer=new Computer();
		computer.Startup();
		computer.Shutdown();

	}

}
