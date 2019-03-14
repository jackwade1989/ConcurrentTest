package Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 优先级
 * @author Administrator
 *
 */
public class Priority implements Runnable{
	private int countDown = 5;
	private volatile double d;
	private int priority;
	private int count=0;
	public Priority(int priority) {
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return Thread.currentThread()+":"+countDown;
	}

	@Override
	public void run() {
		Thread.currentThread().setPriority(priority);
		count++;
		while(true){
			for(int i=1;i<10000;i++){
				d+=(Math.PI+Math.E) / (double) i;
				if(i%1000==0){
					Thread.yield();//通知具有相同优先级的程序运行
					System.out.println(this);
					if(--countDown==0){
						return;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++){
			exec.execute(new Priority(Thread.MIN_PRIORITY));
			exec.execute(new Priority(Thread.MAX_PRIORITY));
		}
		exec.shutdown();
	}
	
}
