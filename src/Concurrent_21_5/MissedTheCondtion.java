package Concurrent_21_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Condition{
	private boolean flag = true;

	public synchronized boolean isFlag() {
		return flag;
	}

	public synchronized void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
}



class TaskOne_1 implements Runnable{
	private Condition c;
	
	public TaskOne_1(Condition c) {
		this.c = c;
	}


	public synchronized void T1(){
		 c.setFlag(false);
		 notifyAll();
	}


	@Override
	public void run() {//T1死循环 因为在等待条件变为true 但是没有改变这样一直等待下去
		System.out.println("T1_RUN");
		System.out.println("wating!");
		synchronized (this) {
			while(c.isFlag()){
				T1();
			}
		}
		
	}
}

class TaskTwo_2 implements Runnable{
	
	private Condition c;
	
	public TaskTwo_2(Condition c) {
		this.c = c;
	}
	
	public synchronized void T2() throws InterruptedException{
		c.setFlag(true);
		wait();
	}
	
	@Override
	public void run() {
		System.out.println("T2_RUN");
		try {
			while(!Thread.interrupted()){
				while(!c.isFlag()){
					T2();
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}

public class MissedTheCondtion {
	public static void main(String[] args) throws InterruptedException {
		Condition con = new Condition();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new TaskTwo_2(con));
		exec.execute(new TaskOne_1(con));
		
//		TimeUnit.SECONDS.sleep(3);
//		exec.shutdownNow();
	}
}
