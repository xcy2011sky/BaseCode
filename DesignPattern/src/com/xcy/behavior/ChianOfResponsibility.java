package com.xcy.behavior;

/**
 * 责任连模式：
 * 使多个对象都有机会处理请求，避免请求的发送者合接受着的耦合关系。
 * 
 * @author chongyan_xu
 *
 */


class Request {
	private int   leave;
	private String  StrValue;
	

	public String getStrValue() {
		return StrValue;
	}
	public void setStrValue(String strValue) {
		StrValue = strValue;
	}
	public Request(int leave)
	{
		this.leave=leave;
	}
	public int getLeave() {
		return leave;
	}

	public void setLeave(int  leave) {
		this.leave = leave;
	}
	
}


abstract class Handler{
	private Handler nextHandler;

	public Handler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(Handler nextHandler) {
		this.nextHandler = nextHandler;
	}
	
	public String ProcessRequest(Request request)
	{
		if(AssertLeave(request))
		{
			return this.HandleRequest(request);
		}else if(this.nextHandler!=null)
		{
			 return this.nextHandler.ProcessRequest(request);
		}
		
		return "request in not avalible";
		
	}
	
	abstract public boolean  AssertLeave(Request request);
	abstract public String HandleRequest(Request request);
	
}

// leave <2 
class FirstHandler extends Handler
{

	@Override
	public boolean AssertLeave(Request request) {
		// TODO Auto-generated method stub
	  if(request.getLeave()<2){
		  return true;
	  }
		return false;
	}

	@Override
	public String HandleRequest(Request request) {
		String result=null;
		result=request.getStrValue()+" on handler FirstHandler";
		return result;
	}
	
}
//     2<leave<5
class SecoundHandler extends Handler {

	@Override
	public boolean AssertLeave(Request request) {
		// TODO Auto-generated method stub
		if(request.getLeave()>2 && request.getLeave()<5) return true;
		return false;
	}

	@Override
	public String HandleRequest(Request request) {
		// TODO Auto-generated method stub
		return request.getStrValue()+"onHandler SecoundHandler";
	}
	
}
//leave >5
class ThirdHandler extends Handler
{

	@Override
	public boolean AssertLeave(Request request) {
		if(request.getLeave()>5)return true;
		return false;
	}

	@Override
	public String HandleRequest(Request request) {
		// TODO Auto-generated method stub
		return request.getStrValue()+"onHandler Third handler";
	}
	
}





/*======================================client=============================*/
public class ChianOfResponsibility {
	
	public static  void main(String []args)
	{
		Request request =new Request(2);
		request.setStrValue("Requeset from client and ");

		 Handler handlerFirst=new FirstHandler();
		 Handler handlerSecound=new SecoundHandler();
		 Handler handlerThird=new ThirdHandler();
		 
		 handlerFirst.setNextHandler(handlerSecound);
		 handlerSecound.setNextHandler(handlerThird);
		 
		 String result=handlerFirst.ProcessRequest(request);
		 
		 System.out.println("result="+result);
		 
		
		
	}

}
