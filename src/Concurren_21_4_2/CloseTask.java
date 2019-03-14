package Concurren_21_4_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class CloseTaskT implements Runnable{

	@Override
	public void run() {
		try {
			SleepTask.sleepTask();
		} catch (InterruptedException e) {
			System.out.println("InterruptedException!");
		}
		System.out.println("Exiting Task of "+ this.getClass().getName());
	}
}

class SleepTask{
	public static void sleepTask() throws InterruptedException{
		TimeUnit.SECONDS.sleep(10);
	}
}

public class CloseTask {
	public static void main(String[] args) throws InterruptedException {
		CloseTaskT task = new CloseTaskT();
		Thread t = new Thread(task);
		t.start();
		System.out.println("interrupting!");
		TimeUnit.MILLISECONDS.sleep(1000);
		t.interrupt();
//		ExecutorService exec = Executors.newCachedThreadPool();
//		Future<?> f = exec.submit(task);
		
//		System.out.println("isShutdown:"+exec.isShutdown());
////		f.cancel(true);
//		exec.shutdownNow();
		System.out.println(" End Task of " + task.getClass().getName());
	}
}
