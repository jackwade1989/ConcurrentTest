package Concurrent_21_5;

import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Car2{
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private boolean waxOn = false;
	
	/**
	 * 抛光操作
	 */
	public void waxed(){
		lock.lock();
		try {
			waxOn = true;
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 打蜡操作
	 */
	public void buffed(){
		lock.lock();
		try {
			waxOn = false;
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 等待抛光
	 * @throws InterruptedException
	 */
	public void waitForWaxing() throws InterruptedException{
		lock.lock();
		try {
			while(!waxOn){
				condition.await();
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 等待打蜡
	 */
	public void waitForBuffed()throws InterruptedException{
		lock.lock();
		try {
			while (waxOn){
				condition.await();
			}
		} finally {
			lock.unlock();
		}
		
	}
	
}

class WaxOn2 implements Runnable {
	
	private Car2 car;
	
	public WaxOn2(Car2 car) {
		this.car = car;
	}
	
	/**
	 * 開始拋光方法
	 */
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				System.out.println("抛光开始");
				TimeUnit.MILLISECONDS.sleep(200);
				car.waxed();
				car.waitForBuffed();
			}
		} catch (Exception e) {
			System.out.println("waxed interrupted");
		}
		System.out.println("ending WaxOn task!");
	}
}

class WaxOff2 implements Runnable {
	private Car2 car;
	
	public WaxOff2(Car2 car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				car.waitForWaxing();
				System.out.println("抛光结束");
				TimeUnit.MILLISECONDS.sleep(200);
				car.buffed();
			}
		} catch (Exception e) {
			System.err.println("waxOff2 err!");
		}
		System.out.println("ending WaxOff task!");
	}
	
}


public class WaxOMatic2 {
	public static void main(String[] args) throws InterruptedException {
		Car2 car = new Car2();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff2(car));
		exec.execute(new WaxOn2(car));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
