package Concurrent_21_6;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable{
	
	private Chopstick left;
	
	private Chopstick right;

	private final int id;
	
	private final int ponderFactor;
	
	private Random rand = new Random(47);
	
	public Philosopher(Chopstick left, Chopstick right, int ident, int ponder) {
		this.left = left;
		this.right = right;
		this.id = ident;
		this.ponderFactor = ponder;
	}

	public void pause() throws InterruptedException{
		if(ponderFactor == 0){
			return;
		}
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				System.out.println(this + "" + "thinking");
				pause();
				//哲学家马上要饿了
				System.out.println(this+"" + "grabbing right");
				right.take();
				System.out.println(this + "" + "grabbing left");
				left.take();
				System.out.println(this+""+"eating");
				pause();
				right.drop();
				left.drop();
				
			}
		} catch (Exception e) {
			System.out.println(this+""+"exiting via interrupt");
		}
	}

	@Override
	public String toString() {
		return "Philosopher [id=" + id + "]";
	}
	
//	public static void main(String[] args) throws InterruptedException {
//		ExecutorService exec = Executors.newCachedThreadPool();
//		exec.execute(new Philosopher(new Chopstick(),new Chopstick(),1,3));
//		TimeUnit.SECONDS.sleep(5);
//		exec.shutdownNow();
//	}
}



