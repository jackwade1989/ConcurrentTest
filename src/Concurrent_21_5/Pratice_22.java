package Concurrent_21_5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pratice_22 {
	public static void main(String[] args) throws InterruptedException {
		BusiWaitStatus busi = new BusiWaitStatus();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new BusiWaitOne(busi));
		exec.execute(new BusiWaitTwo(busi));
		TimeUnit.SECONDS.sleep(10);
		exec.shutdownNow();

	}
}

class BusiWaitStatus{
	private boolean busiWait = false;

	public boolean isBusiWait() {
		return busiWait;
	}

	public void setBusiWait(boolean busiWait) {
		this.busiWait = busiWait;
	}
}

class BusiWaitOne implements Runnable{
	private BusiWaitStatus flag = null;
	BusiWaitOne(BusiWaitStatus flag){
		this.flag = flag;
	}

	@Override
	public void run() {
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

class BusiWaitTwo implements Runnable{
	Random random = new Random();
	private BusiWaitStatus flag = null;
	BusiWaitTwo(BusiWaitStatus flag){
		this.flag = flag;
	}
	
	
	@Override
	public void run() {
		while(!Thread.interrupted()){
			int num = random.nextInt(10000);
			if(flag.isBusiWait()){
				flag.setBusiWait(false);
				System.out.println("休眠时间："+num/1000*0.6);
				System.out.println("now busiWait is false");
			}
		}
	}
}


