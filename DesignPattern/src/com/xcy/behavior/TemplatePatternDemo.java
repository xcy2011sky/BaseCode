package com.xcy.behavior;
/**
 * 模板模式：将部分逻辑以具体方法以及具体构造子的形式实现，然后声明一些抽象方法来迫使子类实现剩余的逻辑。
 * 不同的子类可以以不同的方式实现这些抽象方法，从而对剩余的逻辑有不同的实现。这就是模版方法模式的用意。
 * @author xuchongyan
 *
 */

abstract class TemplateCount{
	public void count(int a,int b)
	{
		System.out.println("result="+HandleCount(a,b));
	}
	abstract int HandleCount(int a,int b);
}
class ConreteTemplate extends TemplateCount{

	@Override
	int HandleCount(int a, int b) {
		// TODO Auto-generated method stub
		return a+b;
	}
	
}
class ConreteTempate2 extends TemplateCount
{

	@Override
	int HandleCount(int a, int b) {
		// TODO Auto-generated method stub
		return a-b;
	}
	
}



/*===========================================================client================================*/
public class TemplatePatternDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TemplateCount addCount=new ConreteTemplate();
		TemplateCount delCount=new ConreteTempate2();
		
		addCount.count(3, 5);
		
		delCount.count(5, 1);
	}

}
