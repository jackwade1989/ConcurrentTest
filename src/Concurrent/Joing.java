package Concurrent;

public class Joing {
	public static void main(String[] args) {
		Sleeper 
			sleeper = new Sleeper("sleepy", 1500),
			grumpy = new Sleeper("grumpy", 1500);
		Joiner
			dopey = new Joiner("Dopey",sleeper),//线程加入
			doc = new Joiner("Doc", grumpy);
		grumpy.interrupt();
	}
}

class Sleeper extends Thread{
	private int duration;
	
	public Sleeper (String name,int sleepTime){
		super(name);
		duration = sleepTime;
		start();
		System.out.println(getName()+"\t is alive \t"+Thread.currentThread().isAlive());
	}
	
	public void run(){
		try {
			sleep(duration);
		} catch (InterruptedException e) { //在异常捕获时这个interrup会被清除 在异常捕获中isInterrupted()总是返回false
			System.out.println(getName()+"\t was interrupted." + "isInterrupted(): "+isInterrupted());
			return;
		}
		System.out.println(getName()+"has awaked");
	}
}

class Joiner extends Thread{
	private Sleeper sleeper;
	
	public Joiner(String name,Sleeper sleeper){
		super(name);
		this.sleeper = sleeper;
		start();
	}
	
	public void run(){
		try {
			sleeper.join();
		} catch (InterruptedException e) {
			System.out.println("Interruped");
		}
		System.out.println(getName()+"join completed");
	}
}