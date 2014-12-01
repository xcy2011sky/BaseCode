package com.xcy.behavior;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * 访问者模式：施加于某种数据结构元素之上的操作。一旦这些操作需要修改的话，接受这个操作的数据结构则可以保持不变
 * 
 * @author xuchongyan
 *
 */
abstract class XElement{

	abstract public void Accept(Visitor visitor);
}
class ConreteElementA extends XElement{
	
	int elementA=1;

	@Override
	public void Accept(Visitor visitor) {
		// TODO Auto-generated method stub
		visitor.VisitorConreteElementA(this);
	}
	
}
class ConreteElementB extends XElement
{

	int elementB=-1;
	@Override
	public void Accept(Visitor visitor) {
		// TODO Auto-generated method stub
		visitor.VisitorConreteElementB(this);
	}
	
}
abstract class Visitor{
	abstract public void VisitorConreteElementA(ConreteElementA  elementa);
	abstract public void VisitorConreteElementB(ConreteElementB elementb);
}
class VisitorA extends Visitor
{

	@Override
	public void VisitorConreteElementA(ConreteElementA elementa) {
		// TODO Auto-generated method stub
		int value=elementa.elementA+1;
		System.out.println("visitor.a="+value);
		
	}

	@Override
	public void VisitorConreteElementB(ConreteElementB elementb) {
		// TODO Auto-generated method stub
		int value=elementb.elementB-1;
		System.out.println("visitor.b="+value);
	}
	
}
class VisitorB extends Visitor
{

	@Override
	public void VisitorConreteElementA(ConreteElementA elementa) {
		// TODO Auto-generated method stub
		int value=elementa.elementA*2;
		System.out.println("visitor.a="+value);
	}

	@Override
	public void VisitorConreteElementB(ConreteElementB elementb) {
		// TODO Auto-generated method stub
		int value=elementb.elementB/2;
		System.out.println("visitor.b="+value);
	}
	
}

class DataStract{
	List<XElement>elements=new ArrayList<XElement>();
	public void attach(XElement e)
	{
		elements.add(e);
	}
	public void detach(XElement e)
	{
		elements.remove(e);
		
	}
	public void Accept(Visitor visitor)
	{
		for(XElement e:elements)
			e.Accept(visitor);
	}
}




/*=======================================================client==============================*/
public class VistorPatternDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		VisitorA va=new VisitorA();
		VisitorB vb=new VisitorB();
		DataStract stract=new DataStract();
		XElement elementaElement=new ConreteElementA();
		XElement elementbElement=new ConreteElementB();
		
		stract.attach(elementaElement);
		stract.attach(elementbElement);
		stract.Accept(vb);	
		stract.Accept(va);
		
	}

}
