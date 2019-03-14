package Concurrent_21_3_3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicityTest implements Runnable{
	private int i = 0;
	public int getValue(){
		return i;
	}
	
	private synchronized void evenIncrement(){
		i++;
		i++;
	}
	
	@Override
	public void run() {
		while(true){
			evenIncrement();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		AtomicityTest at = new AtomicityTest();
		
		while(true){
			exec.execute(at);
			int val = at.getValue();
			if(val%2!=0){
				System.out.println(val);
				System.exit(0);
			}
		}
		
	}
	
}
