package Concurren_21_4_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class practice_21_4_2_18 {

	public static void main(String[] args) throws InterruptedException {
		int count = 0;
		notTask s = new notTask(count);
//		ExecutorService exec = Executors.newCachedThreadPool();
		Thread t = new Thread(s);
		t.setDaemon(true);
		t.start();
		TimeUnit.SECONDS.sleep(2);
		t.interrupt();
//		exec.execute(s);
//		if(!exec.awaitTermination(3000, TimeUnit.MILLISECONDS)){
//			exec.shutdownNow();
//		}
		
	}
}

class notTask implements Runnable {
	int count = 0;
	Sleep s = new Sleep();

	@Override
	public void run() {
		System.out.println("Thread.interrupted():"+Thread.interrupted());
		while (true) {
			++count;
			s.sleep();
		}
	}

	notTask(int count) {
		this.count = count;
	}
	
	public int getValue(){
		return count;
	}
	
}

class Sleep {
	public void sleep() {
		System.out.println("调用开始！");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			System.out.println("interrupted:"+this.getClass().getName());
		}
	}
}
