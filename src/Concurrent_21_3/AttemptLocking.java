package Concurrent_21_3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
	private ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 尝试获取锁 如果是锁定则解锁 无等待时间
	 */
	public void untimed(){
		boolean captured = lock.tryLock();//获取锁是否锁定
		try {
			System.out.println("try lock():"+captured);
		}finally{
			if(captured){
				lock.unlock();
			}
		}
	}
	
	/**
	 *  尝试获取锁 如果是锁定则解锁 等待2秒的时间释放
	 */
	public void timed(){
		boolean captured = false;
		try {
			captured = lock.tryLock(2,TimeUnit.SECONDS);//尝试获取锁并等待两秒 
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			System.out.println("tryLock(2,TimeUnit.SECOND):"+captured);
		} finally {
			if(captured){
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		final AttemptLocking al = new AttemptLocking();
		al.untimed();
		al.timed();
		
		new Thread(){
			{setDaemon(true);}
			
			public void run(){
				al.lock.lock();
				System.out.println("acquired");
			}
		}.start();
		Thread.yield();
		al.untimed();
		al.timed();
	}
	
}
