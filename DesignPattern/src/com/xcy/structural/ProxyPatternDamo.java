package com.xcy.structural;

/**
 * 代理模式：
 * 
 * @author chongyan_xu
 *
 */


interface IRequest{
	public void Request();
}
class WebSite implements IRequest
{

	@Override
	public void Request() {
		// TODO Auto-generated method stub
		System.out.println("this is realWebsite");
	}
	
}

class ProxyWeb implements IRequest
{

	private  WebSite web;
	public ProxyWeb(WebSite webSite)
	{
		web=webSite;
	}
	@Override
	public void Request() {
		// TODO Auto-generated method stub
		System.out.println("this is proxy website ");
		web.Request();
	}
	
}
/*==============================================================client===================================*/


public class ProxyPatternDamo {
	
	public static void main(String []args){
		
		WebSite site=new WebSite();
		ProxyWeb proxy=new ProxyWeb(site);
		proxy.Request();
		
	}

}
