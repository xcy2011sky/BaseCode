package com.xcy.structural;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式：又称部分-整体模式，将对象组织到树的结构中，描述整体与部分的关系
 * 角色：
 * 抽象构件（component）：它给参与组合的对象规定一个接口
 * 树叶构件（leaf）：代表参加组合的树叶对象，一个树叶没有下级子对象。
 * 树枝构件（composite）：代表参加组合，有子对象的对象，并给出树枝对象的行为。
 * 
 * ２种实现形式：
 * 透明方式：component 声明管理子类的方法合对象,add,remove,getChild。这样树叶合树枝类都有相同的方法。
 * 缺点：不够安全，树叶类和　树枝类　有本质的区别，树叶类没有获取子类的方法。
 * 
 * 安全方式：　在composite 里面声明管理子类的方法，这样可以安全的区分树枝合树叶类。
 * 缺点：不够透明，树枝合树叶有不同的接口。
 * @author chongyan_xu
 *
 */

interface IComponent{
	public void Operation();
}


class Leaf implements IComponent{

	private String name;
	public Leaf(String name)
	{
		this.name=name;
	}
	@Override
	public void Operation() {
		// TODO Auto-generated method stub
		System.out.println(" Leaf .name="+this.name);
	}
	
}


class Composite implements IComponent{
	

	private List<IComponent>Components=new ArrayList<IComponent>();
	private String name;
	
	
	public List<IComponent>getComponentList()
	{
		return this.Components;
	}
	public Composite(String name)
	{
		this.name=name;
	    this.Components.add(this);
	}
	@Override
	public void Operation() {

		System.out.println("Composite.name="+this.name);
	
		
	}
	
	public  void add(IComponent component){
		Components.add(component);
	}
	
	
	public void remove(IComponent component)
	{
		Components.remove(component);
	}
	
	
	public IComponent getChild(int index)
	{
		IComponent component=null;
		
		component=Components.get(index);
		return component;
	}
	
	
	public void Display(Composite root)
	{
		for(IComponent p:root.getComponentList())
		{
			
		
				p.Operation();
			
		}
	}
	
}








/*================================================================client===========================================*/
public class CompositePatternDamo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Composite root=new Composite("root");
		
		Leaf leafA=new Leaf("A");
		
		Leaf leafB=new Leaf("B");
		
		Composite cp=new Composite("a");
		cp.add(leafA);
		
		root.add(cp);
		root.add(leafB);
     
		root.Display(root);

		

		

		
	}

}
