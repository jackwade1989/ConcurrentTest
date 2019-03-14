package Concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 后台线程
 * @author Administrator
 *
 */
public class SimpleDeamon implements Runnable{

	@Override
	public void run() {
		try {
			while(true){
				TimeUnit.MICROSECONDS.sleep(10000);
				System.out.println(Thread.currentThread()+""+this);
			}
		} catch (Exception e) {
			System.out.println("sleep() interrupted");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		for(int i=0;i<10;i++){
//			Thread deamon = new Thread(new SimpleDeamon());
			Thread deamon = new Thread(new DaemonThreadFactory().newThread(new SimpleDeamon()));
			//必须在线程启动之前调用setDeamon方法，才能把它设置为后台线程
			deamon.setDaemon(true);
			deamon.start();
		}
		System.out.println("all deamon started");
		TimeUnit.MILLISECONDS.sleep(10000);
	}

}
