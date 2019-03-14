package Concurrent_21_3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 使用显示的LOCK对象
 * @author Administrator
 *
 */
public class MutexEventGenerator extends IntGenerator{
	private int currentEvenValue = 0;
	private Lock lock = new ReentrantLock();
	
	@Override
	public int next() {
		lock.lock();//资源锁定
		try {
			++currentEvenValue;//danger point here
			System.out.println("call one value:"+currentEvenValue);
			Thread.yield();
			++currentEvenValue;
			System.out.println("call two value:"+currentEvenValue);
			return currentEvenValue;
		} finally {
			lock.unlock(); //必须在return后且 要在finally里保证能够释放锁的行为能够被执行。
		}
		
	}
	
	public static void main(String[] args) {
		EvenChecker.test(new MutexEventGenerator()); 
	}

}

