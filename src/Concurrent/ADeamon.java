package Concurrent;

import java.util.concurrent.TimeUnit;

public class ADeamon implements Runnable{

	@Override
	public void run() {
		
		try {
			System.out.println("isDeamon1:"+Thread.currentThread().isDaemon());
			System.out.println("Staring ADeamon");
			TimeUnit.SECONDS.sleep(1);
			System.out.println("isDeamon2:"+Thread.currentThread().isDaemon());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("任务 终止！");
		} finally {
			System.out.println("任务应该结束吗");
		}
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new ADeamon());
		t.setDaemon(true);//设置为后台线程 注释此句代码后finally将会执行 否则将不会执行finally  	当把setDeamon设置为true时 即使main结束了 创建的线程并未结束
		t.start();
		System.out.println("main End！");
		System.out.println("alive:"+t.isAlive());
	}
}

