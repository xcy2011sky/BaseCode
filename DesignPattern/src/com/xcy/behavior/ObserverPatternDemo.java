package com.xcy.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式：
 * @author xuchongyan
 *
 */

abstract class XObserver{
	abstract void update(String state);
}

class ConreteXObserver extends XObserver{

	@Override
	void update(String state) {
		// TODO Auto-generated method stub
		System.out.println("observer1 current state="+state);
	}
	
}
class ConreteXObserver2 extends XObserver{

	@Override
	void update(String state) {
		// TODO Auto-generated method stub
		System.out.println("observer2 motime state="+state);
	}
	
}

abstract class Subject{
	
	 List<XObserver>observers=new ArrayList<XObserver>();
	public void attch(XObserver observer)
	{
		observers.add(observer);
	}
	public void dettch(XObserver ob)
	{
		observers.remove(ob);
	}
	abstract public void notify(String state);
	
}
class ConreteSubject extends Subject{

	@Override
	public void notify(String state) {
		// TODO Auto-generated method stub
		for(XObserver ob:this.observers)
		{
			ob.update(state);
		}
		
	}
	
}

/*=======================================client====================*/
public class ObserverPatternDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XObserver observer=new ConreteXObserver();
		XObserver observer2=new ConreteXObserver2();
		Subject subject=new ConreteSubject();
		subject.attch(observer);
		subject.attch(observer2);
		
		subject.notify("sub1");
	}

}
