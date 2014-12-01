package com.xcy.behavior;
/**
 * ģ��ģʽ���������߼��Ծ��巽���Լ����幹���ӵ���ʽʵ�֣�Ȼ������һЩ���󷽷�����ʹ����ʵ��ʣ����߼���
 * ��ͬ����������Բ�ͬ�ķ�ʽʵ����Щ���󷽷����Ӷ���ʣ����߼��в�ͬ��ʵ�֡������ģ�淽��ģʽ�����⡣
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
