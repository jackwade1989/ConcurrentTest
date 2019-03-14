package Concurren_21_4_2;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;

class sleepBlocked implements Runnable{
	
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("InterupedException!");
		}
		System.out.println("Exiting SleepBlocked.run");
	}
}

class IOBlocked implements Runnable{
	
	private InputStream in;
	
	public IOBlocked(InputStream is){
		this.in = is;
	}

	@Override
	public void run() {
		try {
			System.out.println("Wating for read():");
			in.read();
		} catch (IOException e) {
			if(Thread.currentThread().isInterrupted()){
				System.out.println("Interupped from blocked I/O:"+e.getMessage());
			}else{
				throw new RuntimeException(e);
			}
		}
		System.out.println(  "Exiting IOBlocked.run");
	}
}

class SynchronizedBlocked implements Runnable{
	
	public SynchronizedBlocked() {
		new Thread(){
			public void run(){
				f();//Lock acquired by this thread
			}
		}.start();
	}


	public synchronized void f(){
		while(true){
			Thread.yield();//Never release Lock
		}
	}
	
	
	@Override
	public void run() {
		System.out.println("Trying to call f()");
		f();
		System.out.println("Exiting SychronizedBlocked.run");
	}
	
} 

public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();
	static void test(Runnable r) throws InterruptedException{
		Future<?> f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interruping :"+r.getClass().getName());
		f.cancel(true);//Interruping if running 
		System.out.println("Interruping sent to "+ r.getClass().getName());
	}
	
	public static void main(String[] args) throws Exception{
		test(new sleepBlocked());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		TimeUnit.SECONDS.sleep(100);
		System.out.println("Aborting will System.exit(0)");
		System.exit(0);//...since last 2 interrupts failed
	}
}
