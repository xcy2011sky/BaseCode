package com.xcy.behavior;
/**
 * 策略模式：是针对一组算法，将每一个算法封装到具有共同接口的独立的类中，从而使得它们可以相互替换。策略模式使得算法可以在不影响到客户端的情况下发生变化
 * @author xuchongyan
 *
 *角色：
 *环境：strategy 的引用
 *抽象策略：抽象的策略
 *具体策略：具体实现的策略
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
