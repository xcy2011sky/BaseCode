package com.xcy.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class CalculatorTask extends Task{

	private int a;
	private int b;
	public CalculatorTask(int a,int b)
	{
		this.a=a;
		this.b=b;
	}
	@Override
	public boolean needExecuteImmediate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int Count() {
		int value=a+b;
		System.out.println("a+b="+value);
		return a+b;
	}
}

public class ClinetThread {

	public static void main(String []args)
	{
		System.out.println("client main ");
		Task task1=new CalculatorTask(1, 2);
		Task task2=new CalculatorTask(5, 2);
		Task task3=new CalculatorTask(6, 2);
		Task task4=new CalculatorTask(7, 2);
		Task tasks[]={task1,task2,task3,task4};
		ThreadPool.getInstance().batchAddTask(tasks);
		
		////////////////////////////////////////////////////////////
		ThreadPoolExecutor threadpool=new ThreadPoolExecutor(5, 6, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		threadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("java threadpool  start");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("java threadpool  end");
			}
		});
	threadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("java threadpool2  start");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("java threadpool2  end");
			}
		});
	threadpool.execute(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("java threadpool3  start");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("java threadpoo3  end");
		}
	});
	threadpool.execute(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("java threadpool4  start");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("java threadpool4  end");
		}
	});
	
		System.out.print("Active count="+threadpool.getActiveCount());
		
		System.out.print("completed Task count="+threadpool.getCompletedTaskCount());
	}
}
