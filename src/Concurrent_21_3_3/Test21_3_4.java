package Concurrent_21_3_3;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test21_3_4 implements Runnable{
	public Timer createTimer(){
		Timer t = new Timer();
		return t;
	}
	
	public void testTimer(){
		Test21_3_4 t = new Test21_3_4();
		t.createTimer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("timer running");
			}
		}, 10000);
	}
	
	@Override
	public void run() {
		System.out.println("start!");
		testTimer();
	}
	
	public static void main(String[] args) {
		int k = 10;
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0 ; i <= k; i++){
			exec.execute(new Test21_3_4());
		}
		
	}
}
