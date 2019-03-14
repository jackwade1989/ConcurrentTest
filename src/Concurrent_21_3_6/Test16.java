package Concurrent_21_3_6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test16 {
	private TestObject_15 object = new TestObject_15();
	private int i = 0;
	private Lock lock = new ReentrantLock();
	
	public void a(){
		lock.lock();
		try {
			object.setA("a");
			System.out.println("a:"+i++);
			System.out.println("a is :"+object.getA());
		} finally {
			lock.unlock();
		}
			
	};
	
	public void b(){
		lock.lock();
		try {
			object.setA("b");
			System.out.println("b:"+i++);
			System.out.println("b is :"+object.getA());
		} finally {
			lock.unlock();
		}
	};
	
	public void c(){
		lock.lock();
		try {
			System.out.println("c:"+i++);
			object.setA("c");
			System.out.println("c is :"+object.getA());
		} finally {
			lock.unlock();
		}
	};
	
	public static void main(String[] args) {
		final Test15 t = new Test15();
		new Thread(){
			public void run(){
				t.a();
				t.b();
				t.c();
			};
		}.start();
		t.a();
		t.b();
		t.c();
	}
}
