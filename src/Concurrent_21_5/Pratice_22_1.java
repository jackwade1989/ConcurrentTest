package Concurrent_21_5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pratice_22_1 {
	public static void main(String[] args) throws InterruptedException {
		BusiWaitStatus_1 busi = new BusiWaitStatus_1();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new BusiWaitOne_1(busi));
		exec.execute(new BusiWaitTwo_1(busi));
		TimeUnit.SECONDS.sleep(10);
		exec.shutdownNow();

	}
}

class BusiWaitStatus_1{
	private boolean busiWait = false;

	public boolean isBusiWait() {
		return busiWait;
	}

	public void setBusiWait(boolean busiWait) {
		this.busiWait = busiWait;
	}
}

class BusiWaitOne_1 implements Runnable{
	private BusiWaitStatus_1 flag = null;
	BusiWaitOne_1(BusiWaitStatus_1 flag){
		this.flag = flag;
	}

	@Override
	public void run() {
		System.out.println("one start!");
		try {
			while(!Thread.interrupted()){
				TimeUnit.SECONDS.sleep(5);
				flag.setBusiWait(true);
			}
		} catch (InterruptedException e) {
			System.out.println("BusiOne Exiting");
		}
	}
}

class BusiWaitTwo_1 implements Runnable{
	Random random = new Random();
	private BusiWaitStatus_1 flag = null;
	BusiWaitTwo_1(BusiWaitStatus_1 flag){
		this.flag = flag;
	}
	
	public synchronized void waitingForBusiOne() throws InterruptedException{
		wait();
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()){
			int num = random.nextInt(10000);
			if(flag.isBusiWait()){
				flag.setBusiWait(false);
				System.out.println("休眠时间："+num/1000*0.6);
				System.out.println("now busiWait is false");
			}else{
				try {
					System.out.println("now two waiting");
					waitingForBusiOne();
				} catch (InterruptedException e) {
					System.out.println("now two exiting");
				}
			}
		}
	}
}


