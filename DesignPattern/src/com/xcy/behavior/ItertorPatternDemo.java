package com.xcy.behavior;
/**
 * 迭代器模式：
 * @author chongyan_xu
 *
 */
interface Iterator{
	public Object next();
	public boolean hasNext();
}


class ConreteItertor implements Iterator{

	private String []devices={"mobile","pc","tablet","car"};
	
	private int current=0;
	
	@Override
	public Object next() {
			if(hasNext()){
				return devices[current++];
			}
		return null;
	}

	@Override
	public boolean hasNext() {
		if(current<devices.length)
			return true;
		return false;
	}
	
}

abstract class IContainer{
	abstract Iterator CreateIterator();
}
class ConreteIterator  extends IContainer{

	@Override
	Iterator CreateIterator() {
		// TODO Auto-generated method stub
		return new ConreteItertor();
	}
	
}

/*=========================================================client===========================================*/
public class ItertorPatternDemo {

	public static void main(String[] args) {
	
		IContainer container=new ConreteIterator();
		Iterator iterator=container.CreateIterator();
		while(iterator.hasNext())
		{
			System.out.println("iterator: "+iterator.next());
		}
		

	}

}
