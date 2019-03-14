package Concurrent;

import java.util.concurrent.TimeUnit;

public class Deamons{
	public static void main(String[] args) throws InterruptedException {
		Thread d = new Thread(new Deamon());
		d.setDaemon(true);
		d.start();
		System.out.println("d.isDeamon()="+d.isDaemon()+",");
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Main isDeamin()="+Thread.currentThread().isDaemon());
	}
}