package com.xcy.behavior;
/**
 * 备忘录模式：在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。
 * 这样就可以将该对象恢复到原先保存的状态
 * 角色：
 * 发起人：记录当前时刻的内部状态，负责定义那些属于备份范围内的状态，负责创建合恢复数据。
 * 备忘录：负责存储发起人对象内部状态。
 * 管理角色：对备忘录管理，保存合提供备忘录
 * @author chongyan_xu
 *
 */

class Originator {
	private String state="";
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Memento CreateMemento()
	{
		return new Memento(this.state);
	}
	public void RestoreMemento(Memento memento)
	{
		this.state=memento.getState();
	}
}

class Memento{
	
	private String state=null;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Memento(String state){
		this.state=state;
	}
}

class Caretaker{
	private Memento mMemento;

	public Memento getmMemento() {
		return mMemento;
	}

	public void setmMemento(Memento mMemento) {
		this.mMemento = mMemento;
	}
	
}

/*==================================================================client======================================*/

public class MementoPatternDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Originator originator=new Originator();
		originator.setState("State 1");
		System.out.println("Init state ="+originator.getState());
		
		Caretaker caretaker=new Caretaker();
		caretaker.setmMemento(originator.CreateMemento());
		originator.setState("State 2");
		System.out.println("change state="+originator.getState());
		
		
		originator.RestoreMemento(caretaker.getmMemento());
		System.out.println("resore state="+originator.getState());
		
	}

}
