package com.xcy.structural;

/**
 * 装饰模式：又称包装模式，该模式对客户端透明的方式扩展，是继承关系的一个替代方案。
 * 通过SetComponent　实现对装饰对象的加入。
 * 
 * 优点：装饰合继承一样都可以扩展类的功能，但是装饰模式可以更灵活的扩展功能。
 * 可以使用不同的类实现不同的装饰组合。
 * 
 * 
 */
//抽象构件
abstract class  Component
{
	 String com=null;

	public abstract void Show();
}

//具体构件
class ConcreteComponent extends Component{

	public ConcreteComponent()
	{
		this.com="jack";
	}
	@Override
	public void Show() {
		// TODO Auto-generated method stub
		System.out.println("name="+this.com);
	}
	
}


//抽象装饰
abstract class Decotor extends Component
{
      Component component;
     
     public void  setComponent(Component cp)
     {
    	 this.component=cp;
    	
     }
     
	@Override
	public void Show() {
	
		if(component!=null)
			component.Show();
		
	}
	
	
}

//add state 装饰
class ConcreteDecotorState  extends Decotor{

	private String state=null;
	
	public void addState() {
		// TODO Auto-generated method stub

		state="newState";
		
	}

	@Override
	public void Show() {
		super.Show();
		System.out.println("add state="+this.state);
	
	}
	
}


class ConcreteDecotorBeh extends Decotor{

	private String beh=null;
	public void addBeh()
	{
		beh="newBeh";
	}

	@Override
	public void Show() {
		super.Show();
		System.out.println("add beh="+this.beh);
	
	}
	
}


/*==================================================================client===================================*/

public class DecoratorPatternDamo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Component person=new ConcreteComponent();
		person.Show();
		
		/*
		 * 第一次装饰，增加状态
		 */
		ConcreteDecotorState state=new ConcreteDecotorState();
		state.setComponent(person);
		state.addState();
		
		state.Show();

		/*
		 * 第二次装饰
		 */
		ConcreteDecotorBeh  beh=new ConcreteDecotorBeh();
		beh.setComponent(state);
		beh.addBeh();
		
		beh.Show();
		
		
		
	}

}
