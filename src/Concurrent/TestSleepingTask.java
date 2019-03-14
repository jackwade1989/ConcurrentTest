package Concurrent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestSleepingTask implements Runnable{
	private static int taskCount = 0;
	private final int taskId = taskCount++;
	@Override
	public void run() {
		Random random = new Random();
		int num = random.nextInt(10000);
		System.out.println("taskId:"+taskId);
		try {
			TimeUnit.MILLISECONDS.sleep(num);
			System.out.println("休眠时间："+num/1000*0.6);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
			ExecutorService exec = Executors.newCachedThreadPool();
			for(int i=0;i<10;i++){
				exec.execute(new TestSleepingTask());
				System.out.println();
			}
			exec.shutdown();
	}

}

