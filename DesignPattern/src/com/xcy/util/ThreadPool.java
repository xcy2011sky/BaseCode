package com.xcy.util;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
	private static ThreadPool instance=null;
	private static final int SYSTEM_BUS_TASK__COUNT=150;
	private static int worker_num=5;
	private static int taskCounter=0;
	
	public static boolean systemIsBus=false;
	
	private  static List<Task>taskQueue=Collections.synchronizedList(new LinkedList<Task>());
	
	public ThreadWorker[] workers;
	
	private ThreadPool(){
		System.out.println("ThreadPool create");
		
		workers=new ThreadWorker[worker_num];
		for(int i=0;i<workers.length;i++)
		{
			System.out.println("ThreadPool::worker create_index="+i);
			
			workers[i]=new ThreadWorker(i);
		}
	}
	
	private ThreadPool(int pool_worker_num)
	{
		worker_num=pool_worker_num;
		workers=new ThreadWorker[worker_num];
		for(int i=0;i<workers.length;i++)
		{
			workers[i]=new ThreadWorker(i);
		}
	}
	
	public static synchronized ThreadPool getInstance()
	{
		if(instance==null)
			return new ThreadPool();
		return instance;
	}
	
	public void addTask(Task newTask)
	{
		System.out.println("ThreadPool addTask");
		synchronized (taskQueue) {
			newTask.setTaskId(++taskCounter);
			newTask.setSubmitTime(new Date());
			taskQueue.add(newTask);
			taskQueue.notifyAll();
			
		}
	}
	public void batchAddTask(Task[] taskes){
		if(taskes==null|| taskes.length==0) return;
		synchronized(taskQueue){
			for(int i=0;i<taskes.length;i++){
				if(taskes[i]==null) continue;
				taskes[i].setTaskId(++taskCounter);
				taskes[i].setSubmitTime(new Date());
				taskQueue.add(taskes[i]);
			}
			taskQueue.notifyAll();
		}
	}
	
	public synchronized void destory(){
		for(int i=0;i<worker_num;i++){
			workers[i].stopWorker();
			workers[i]=null;
		}
		taskQueue.clear();
	}
	class ThreadWorker extends Thread{
		
		private int index=-1;
		private boolean isRunning=true;
		private boolean isWaiting=true;
		
		
		public ThreadWorker(int index)
		{
			System.out.println("ThreadWorker index= "+index);
			this.index=index;
			start();
		}
		public void stopWorker(){
			this.isRunning=false;
		}
		public boolean isWaiting()
		{
			return this.isWaiting;
		}
		
		public void run(){
			while(isRunning){
				Task r=null;
				synchronized(taskQueue){
					while(taskQueue.isEmpty()){
						try {
							taskQueue.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					r=(Task)taskQueue.remove(0)	;
				}
				if(r!=null){
					isWaiting=false;
					if(r.needExecuteImmediate()){
						new Thread(r).start();
					}else
					{
						r.run();
					}
					isWaiting=true;
					r=null;
				}
			}
		}
		
	}
}




