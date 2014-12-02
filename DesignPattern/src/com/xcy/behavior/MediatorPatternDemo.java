package com.xcy.behavior;
/**
 * 中介模式：用一个中介者对象封装一系列的对象交互，中介者使各对象不需要显示地相互作用，
 * 从而使耦合松散，而且可以独立地改变它们之间的交互
 * @author chongyan_xu
 *
 */
abstract class AbstractMediator{
	public AbstractColleague A;
	public AbstractColleague B;
	public AbstractMediator(AbstractColleague a,AbstractColleague b){
		A=a;
		B=b;
		
	}
	public abstract void AaffectB();
	public abstract void BaffectA();
}

class Mediator extends AbstractMediator{

	public Mediator(AbstractColleague a, AbstractColleague b) {
		super(a, b);

	}

	@Override
	public void AaffectB() {
		// TODO Auto-generated method stub
		int number=A.getNumber();
		B.setNumber(number*100);
	}

	@Override
	public void BaffectA() {
		// TODO Auto-generated method stub
		int number=B.getNumber();
		A.setNumber(number/100);
	}
	
}

abstract class AbstractColleague{
	int number;
	public abstract void setNumber(int number,AbstractMediator am);
	public int getNumber()
	{
		return number;
	}
	public void setNumber(int number){
		this.number=number;
	}
}
class ColleagueA extends AbstractColleague{

	@Override
	public void setNumber(int number, AbstractMediator am) {
		// TODO Auto-generated method stub
		this.number=number;
		am.AaffectB();
	}
	
}
class ColleagueB extends AbstractColleague{

	@Override
	public void setNumber(int number, AbstractMediator am) {
		// TODO Auto-generated method stub
		this.number=number;
		am.BaffectA();
	}
	
}
/*================================================client===========================================*/
public class MediatorPatternDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AbstractColleague cA=new ColleagueA();
		AbstractColleague cB=new ColleagueB();
		
		AbstractMediator mediator =new Mediator(cA, cB);
		
		System.out.println("A affect B ");
		cA.setNumber(100,mediator);
		System.out.println("cA.number="+cA.number);
		System.out.println("cB.number="+cB.number);
		
		
	}

}
