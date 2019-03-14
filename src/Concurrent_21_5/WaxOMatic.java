package Concurrent_21_5;

import java.lang.reflect.Executable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Car {
	 private boolean waxOn = false;
	 public synchronized void waxed(){
		 waxOn = true;//准备打蜡
		 notify();
	 }
	 
	 public synchronized void bufferd(){
		 waxOn = false;
		 notify();
	 }
	 
	 public synchronized void watingForWaxing() throws InterruptedException{
		 while(!waxOn){//while循环判断条件 因为可能涉及多个任务出于相同原因在等待同一个锁 为了避免其他的任务来修改 
			 wait();
		 }
	 }
	 
	 public synchronized void watingForBuffing() throws InterruptedException{
		 while(waxOn){
			 wait();
		 }
	 }
}

class WaxOn implements Runnable{
	private Car car;
	public WaxOn(Car c){
		car = c;
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				System.out.println("Wax On!");
				car.waxed();
				car.watingForBuffing();
				TimeUnit.MICROSECONDS.sleep(200);
				} 
		}catch (InterruptedException e) {
			System.out.println("Exiting via interrupt");
		}
		System.out.println("Ending wax on task");
	}
}

class WaxOff implements Runnable{
	private Car car;

	public WaxOff(Car car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				car.watingForWaxing();
				System.out.println("Wax Off!");
				TimeUnit.MILLISECONDS.sleep(200);
				car.bufferd();
			}
		} catch (Exception e) {
			System.out.println("Exiting via interrupt");
		}
		System.out.println("Ending wax off task");
	}
	
}


public class WaxOMatic {
	public static void main(String[] args) throws InterruptedException {
		Car car = new Car();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));
		exec.execute(new WaxOn(car));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
