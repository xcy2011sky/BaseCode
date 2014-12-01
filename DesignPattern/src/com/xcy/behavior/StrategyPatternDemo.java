package com.xcy.behavior;
/**
 * ����ģʽ�������һ���㷨����ÿһ���㷨��װ�����й�ͬ�ӿڵĶ��������У��Ӷ�ʹ�����ǿ����໥�滻������ģʽʹ���㷨�����ڲ�Ӱ�쵽�ͻ��˵�����·����仯
 * @author xuchongyan
 *
 *��ɫ��
 *������strategy ������
 *������ԣ�����Ĳ���
 *������ԣ�����ʵ�ֵĲ���
 *
 *
 */
class Context {
	private Strategy strategy;
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public void put()
	{
		this.strategy.PutOut();
	}
}
abstract class Strategy{
	abstract public void PutOut();
}

class ConreteStrategyA extends Strategy{

	@Override
	public void PutOut() {
		// TODO Auto-generated method stub
		System.out.println("into strage A ");
	}
	
}
class ConreteStrategyB extends Strategy{

	@Override
	public void PutOut() {
		// TODO Auto-generated method stub
		System.out.println("into strage B ");
	}
	
}
class ConreteStrategyC extends Strategy{

	@Override
	public void PutOut() {
		// TODO Auto-generated method stub
		System.out.println("into strage C ");
	}
	
}

/*========================================================client========================================*/
public class StrategyPatternDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Context context=new Context();
		context.setStrategy(new ConreteStrategyA());
		context.put();
		
		context.setStrategy(new ConreteStrategyB());
		context.put();
		
		
	}

}
