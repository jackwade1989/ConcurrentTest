package Concurren_21_4_2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 
 * 
	lock ： 在锁上等待，直到获取锁；

	tryLock：立即返回，获得锁返回true,没获得锁返回false；

	tryInterruptibly：在锁上等待，直到获取锁，但是会响应中断，这个方法优先考虑响应中断，而不是响应锁的普通获取或重入获取。 


 * @author chenchao.huang
 * 
 */
class BlockedMutex{
	private Lock lock = new ReentrantLock();

	public BlockedMutex() {
		
		System.out.println("now is BlockedMutex construct " );
		lock.lock();
		System.out.println("lock state :" + lock.tryLock());
	}
	
	public void f(){
		try {
			//This will never be avaliable to a second task
			lock.lockInterruptibly();//在锁上等待，直到获取锁，但是会响应中断，这个方法优先考虑响应中断，而不是响应锁的普通获取或重入获取。 
			System.out.println("lock acquired in f()");
		} catch (InterruptedException e) {
			//從f()的鎖中獲取中斷
			System.out.println("Interrupted from lock acquisition in f()");
		}
	}
	
}

class Blocked2 implements Runnable{
	BlockedMutex blocked = new BlockedMutex();
	@Override
	public void run() {
		System.out.println("Wating for f() in BlockedMutex");
		blocked.f();
		System.out.println("Broken out of blocked call ");
	}
	
}

public class Interrupting2 {
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new Blocked2());
		t.start();
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Issuing t.interrupt()");
		t.interrupt();
	}
}
