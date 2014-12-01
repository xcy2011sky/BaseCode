package com.xcy.structural;

/**
 * 桥接模式：就是把事务和具体实现分开，可以让他们独立变化。将抽象合实现　解耦
 * @author chongyan_xu
 *
 */

abstract class CarBridge{
	
	 IEngine mEngine;
	 void CreateEngine(){
		 mEngine.CreateEngine();
	 }
	abstract void setEngine(IEngine engine);
}



class Truck extends CarBridge{

	@Override
	void setEngine(IEngine engine) {
		// TODO Auto-generated method stub
		mEngine=engine;
	}
	
}
class Bus extends CarBridge{

	@Override
	void setEngine(IEngine engine) {
		// TODO Auto-generated method stub
		mEngine=engine;
	}
	
}


abstract class IEngine{
	abstract void CreateEngine();
}

class LowEngine extends IEngine
{

	@Override
	void CreateEngine() {
		// TODO Auto-generated method stub
		System.out.println("This is low Engine");
	}
	
}

class NormalEngine extends IEngine
{

	@Override
	void CreateEngine() {
		// TODO Auto-generated method stub
		System.out.println("This is normal  Engine");
	}
	
}


/*=============================================client=====================================*/
public class BridgePatternDamo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CarBridge truck =new Truck();
		IEngine lowEngine =new LowEngine();
		truck.setEngine(lowEngine);
		truck.CreateEngine();
		
		IEngine normlEngine =new NormalEngine();
		truck.setEngine(normlEngine);
		truck.CreateEngine();
		
		
		
	}

}
