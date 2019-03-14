package Concurrent_21_3_5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pair {//非线程安全的类 其中如x,y++的自增长操作是不安全的
	private int x , y;
	public Pair(int x ,int y){
		this.x = x;
		this.y = y;
	}
	
	public Pair(){
		this(0,0);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void incrementX(){
		x++;
	}
	
	public void incrementY(){
		y++;
	}

	@Override
	public String toString() {
		return "x=" + x + ", y=" + y ;
	}
	
	public class PairValuesNotEqualtExeption extends RuntimeException{
		
		public PairValuesNotEqualtExeption(){
			super("Pair values not equals : "+ Pair.this);
		}
	}
	
	//x,y必须相等 检查xy 因为是同步执行自增长 如果xy不相等可能是线程不安全
	public void checkState(){
		if(x!=y){
			throw new PairValuesNotEqualtExeption();
		}
		
	}
}

/**
 * Protect a Pair inside a thread safe class
 * @author Administrator
 * 设计模式 模板方法  在基类中实现一些功能，并且其中一个或多个抽象方法在派生类定义
 *
 */
abstract class PairManager{
	AtomicInteger checkCounter = new AtomicInteger(0);//原子类
	protected Pair p = new Pair();
	private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());//线程安全的list
	
	public synchronized Pair getPair(){
		//制作一份拷贝以保证原始的安全
		return new Pair(p.getX(),p.getY());
	}
	
	protected void store (Pair p){
		storage.add(p);
		try {
			TimeUnit.MICROSECONDS.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void increment();
	
}


	class PairManager1 extends PairManager{
		
		/**
		 * 同步整个方法 对对象的加锁时间长
		 */
		@Override
		public synchronized void increment() {
			p.incrementX();
			p.incrementY();
			store(getPair());
		}
		
	}
	
	class PairManager2 extends PairManager{
		/**
		 * 代码同步块控制  同步块控制的对象加锁的时间短 
		 */
		@Override
		public void increment() {
			Pair temp;
			synchronized (this) {
				p.incrementX();
				p.incrementY();
				temp = getPair();
			}
			store(temp);//在基类中的list是线程安全的可以不必防护
		}
	}
	/**
	 * 使用显示的锁 同步整个方法
	 * @author Administrator
	 *
	 */
	class ExplicPairManager1 extends PairManager{
		
		private Lock lock = new ReentrantLock();
		
		@Override
		public synchronized void increment() {
			lock.lock();
			try {
				p.incrementX();
				p.incrementY();
				store(getPair());
			} finally {
				lock.unlock();
			}
		}
		
	}

	/**
	 * 使用显示的锁 部分代码同步 
	 * @author Administrator
	 *
	 */
class ExplicPairManager2 extends PairManager{
		
		private Lock lock = new ReentrantLock();
		
		@Override
		public void increment() {
			Pair temp;
			lock.lock();
			try {
				p.incrementX();
				p.incrementY();
				temp = getPair();
			} finally {
				lock.unlock();
			}
			store(temp);
		}
		
	}
	
	/**
	 * 操纵器 分别执行pairManager1 和 pairManager2 
	 * @author Administrator
	 *
	 */
	class PairManipulator implements Runnable {
		
		private PairManager pm;
		
		public PairManipulator(PairManager pm) {
			this.pm = pm;
		}

		@Override
		public void run() {
			while(true){
				pm.increment();
			}
		}

		@Override
		public String toString() {
			return "Pair:"+pm.getPair() + ": checkCounter = " + pm.checkCounter.get();
		}
	}
	
	/**
	 * 检验pairManger1和pairManger2的运行频率 
	 * @author Administrator
	 *
	 */
	class PairChecker implements Runnable{
		
		private PairManager pm;
		
		public PairChecker(PairManager pm) {
			this.pm = pm;
		}

		@Override
		public void run() {
			while(true){
				pm.checkCounter.incrementAndGet();//原子类增量增长
				pm.getPair().checkState();
			}
		}
	}
	
	

	