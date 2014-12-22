package com.xcy.util;

import java.util.Date;

public  abstract class  Task  implements Runnable{

	private Date genrateTime=null;
	
	private Date submitTime=null;
	
	private Date beginExceuteTime=null;
	
	private Date finishTime=null;
	
	private long taskId;
	
	public Task(){
		this.genrateTime=new Date();
	}
	
	public Date getGenrateTime() {
		return genrateTime;
	}

	public void setGenrateTime(Date genrateTime) {
		this.genrateTime = genrateTime;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getBeginExceuteTime() {
		return beginExceuteTime;
	}

	public void setBeginExceuteTime(Date beginExceuteTime) {
		this.beginExceuteTime = beginExceuteTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	@Override
	public void run() {

		Count();
		
	}
	public abstract boolean needExecuteImmediate();
	
	public abstract int Count();

}
