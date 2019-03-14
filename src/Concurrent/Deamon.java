package Concurrent;

import java.util.concurrent.TimeUnit;

public class Deamon implements Runnable{
	private Thread[] t = new Thread[10];
	
	@Override
	public void run() {
		for(int i=0;i<t.length;i++){
			
			t[i] = new Thread(new DeamonSpawn());
			System.out.println("t:"+i+":"+t[i]);
			t[i].start();
			System.out.println("DeamonSpawn "+i+" started ,");
		}
		for(int j=0;j<t.length;j++){
			System.out.println(("t[" +j+ "].isDeamon()= "+t[j].isDaemon()+" ,"));//是否后台线程
			while(true){
				Thread.yield();
			}
		}
	}
	
	
}





