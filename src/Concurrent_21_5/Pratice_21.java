package Concurrent_21_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TaskStasus{
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}

class TaskOne implements Runnable{
	private TaskStasus t = null;
	TaskOne(TaskStasus ts){
		t = ts;
	}

	@Override
	public void run() {
		System.out.println("task one running");
			while(!Thread.interrupted()){
				while(null == t.getMsg()||"".equals(t.getMsg())){
					try {
						waitingForTaskTowSetMsg();
					} catch (InterruptedException e) {
						System.out.println("taskOneExiting");
					}
				}
					System.out.println(t.getMsg());
					t.setMsg(null);
			}
			System.out.println("taskOneEnding");

			
		
		
	}
	
	public synchronized void waitingForTaskTowSetMsg() throws InterruptedException{
		System.out.println("task one wating");
		wait();
	}
}

class TaskTwo implements Runnable{
	private TaskStasus t = null;
	TaskTwo(TaskStasus t){
		this.t = t;
	}
	
	public synchronized void setTaskStatusMsg() throws InterruptedException{
		System.out.println("Now set taskMsg");
		t.setMsg("Hi Im jack");
		System.err.println("msg set finsh");
		wait();
//		notifyAll();
	}

	@Override
	public void run() {
		System.out.println("task two running");

		try {
			while(!Thread.interrupted()){
				TimeUnit.SECONDS.sleep(2);
				setTaskStatusMsg();
				taskTwoFinshed();
//				watingForTaskTwo();
			}
			
		} catch (InterruptedException e) {
			System.out.println("taskTwoExiting");

		}
	}
	
	public synchronized void watingForTaskTwo() throws InterruptedException{
		System.out.println("task two wating");
		wait();
	}
	
	public synchronized void taskTwoFinshed(){
		System.out.println("Task two execute notifyAll()");
		notifyAll();
	}
}

public class Pratice_21{
	public static void main(String[] args) throws InterruptedException {
		TaskStasus t = new TaskStasus();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new TaskOne(t));
		exec.execute(new TaskTwo(t));
		TimeUnit.SECONDS.sleep(8);
		exec.shutdownNow();

	}
}
