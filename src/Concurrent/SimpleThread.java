package Concurrent;

import java.util.concurrent.TimeUnit;

public class SimpleThread extends Thread{
	private int countDown = 5;
	private static int threadCount = 0;
	public SimpleThread(){
		super(Integer.toString(++threadCount));
//		setDaemon(true);
		start();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			System.out.println("done!");
		}
	}
	@Override
	public String toString() {
		return "#"+getName()+"("+countDown+"),";
	}
	
	public void run(){
		while(true){
			System.out.println(this);
			if(--countDown==0){
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		for(int i=0;i<5;i++){
			new SimpleThread();
			if(i==3)
				break;
		}
		System.out.println("main done");
	}
	
}
