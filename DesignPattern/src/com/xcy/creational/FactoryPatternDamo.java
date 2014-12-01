package com.xcy.creational;
/**
 * 问题：今天主要用Factory完成生产汽车的问题。
 * @author chongyan_xu
 *
 */

class Car{
 void PrintType()
	{
		System.out.println("This is base car===================");
	}
}

class SmartCar extends Car
{
	 void PrintType(){
		 System.out.println("This is  smart car ===============");
	 }
}

class BusCar extends Car
{
 void PrintType(){
		 System.out.println("This is  bus car ===============");
	 }
}

class SmallCar extends Car
{
	 void PrintType(){
		 System.out.println("This is  small car ===============");
	 }
}

/* =======================================Siample Factory Pattern====================================================*/
/**
 * 简单工厂模式：根据不同的需求创建不同的对象，属于静态创建。
 * 优点：清楚对象创建的过程，管理多个类型的创建过程。
 * 缺点：不便与扩展新的类，不满足　开闭原则。
 * @author chongyan_xu
 *
 */

class SimpleFactory
{
	public static final int TYPE_SMART=1;
	public static final int  TYPE_BUD=2;
		
	public Car CreateCar(int Type){
		Car car=null;
				switch(Type)
				{
				case SimpleFactory.TYPE_SMART:
						car=new SmartCar();
						break;
				case SimpleFactory.TYPE_BUD:
					   car=new BusCar();
					   break;
				default:
					 car=new Car();
				}
				return car;
	}
}

/*=========================================Factory Method Pattern================================================*/

/**
 * 工厂方法模式：将工厂抽象化，每个产品创建一个工厂，根据产品的需要创建对应的工厂。没增加一个产品，需要增加一个工厂。
 * 优点：方便扩展，客户不需要知道对象的创建过程。
 * 
 * @author chongyan_xu
 *
 */

interface IFactoryCar{
	Car CreateCar();
}

class SmartCarFactory implements IFactoryCar
{

	@Override
	public Car CreateCar() {
		return new SmartCar();
	}
	
}

class BusCarFactory implements IFactoryCar
{

	@Override
	public Car CreateCar() {
		// TODO Auto-generated method stub
		return new BusCar();
	}
	
}

class SmallCarFactory implements IFactoryCar
{

	@Override
	public Car CreateCar() {
		// TODO Auto-generated method stub
		return new SmallCar();
	}
	
}
/*===========================================Abstract Factory==================================================*/

interface IAirCondition{
	 IAirCondition CreateAirC();
	 void PrintPerformence();
}

class NormalAirC  implements IAirCondition{

	@Override
	public IAirCondition CreateAirC() {
		System.out.println("This is Normally AirCondition    ");
		return null;
	}
	@Override
	public
	void PrintPerformence()
	{
		System.out.println(" Normally AirCondition    ");
	}
	
}

class AdvancedAirC implements IAirCondition{

	@Override
	public IAirCondition CreateAirC() {
		System.out.println("This is Advanced  AirCondition    ");
		return null;
	}
	
	public void PrintPerformence()
	{
		System.out.println(" Advanced  AirCondition    ");
	}
}
interface IAFactory {
	Car CreateCar();
	IAirCondition CreateAirC();
}

class SmartACarFactory implements IAFactory{

	@Override
	public Car CreateCar() {
		// TODO Auto-generated method stub
		return new SmartCar();
	}

	@Override
	public IAirCondition CreateAirC() {
		// TODO Auto-generated method stub
		return new NormalAirC();
	}
	
}

class BusACarFactory implements IAFactory
{

	@Override
	public Car CreateCar() {

		return new BusCar();
	}

	@Override
	public IAirCondition CreateAirC() {
		// TODO Auto-generated method stub
		return new AdvancedAirC();
	}
	
}



/*============================================Factory Pattern Client==============================================*/

public class FactoryPatternDamo {
	
	private final String TAG="FactoryPattern";
	
	/**
	 * Factory Pattern Test Client
	 * @param arg
	 * @param args
	 */
	public static void main(String []args)
	{
		
		/**
		 *  SimpleFactory Test 
		 *
			SimpleFactory sFactory=new SimpleFactory();
			Car car=null;
			car=sFactory.CreateCar(SimpleFactory.TYPE_BUD); //SimpleFactory.TYPE_SMAR　OR  OTHER  
			car.PrintType();
		*
		**/

		/**
		 *  Factory Method Test
		 * 
		 * IFactoryCar iFactory=new  SmartCarFactory();//SmartCarFactory ,SmallCarFactory.
		
		Car car=iFactory.CreateCar();
		
		car.PrintType();
		//方便扩展新的工厂方法，又不修改原来的代码。符合　　开闭原则　

		 */
		
		Car car=null;
		IAirCondition AirC=null;
		
		IAFactory aFactory=new SmartACarFactory();
		car=aFactory.CreateCar();
		AirC=aFactory.CreateAirC();
		car.PrintType();
		AirC.PrintPerformence();

		
		
	}
	

	
}

