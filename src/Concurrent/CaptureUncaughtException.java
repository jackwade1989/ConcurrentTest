package Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 执行创建线程的类
 * @author Administrator
 *
 */
public class CaptureUncaughtException{
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool(
				new HandlerThreadFactory());//通过工厂方法来创建线程
		executorService.execute(new ExceptionThread2());//开始线程
	}
}
 
/**
 * 线程创建 抛出异常
 * @author Administrator
 *
 */
 class ExceptionThread2 implements Runnable{
	@Override
	public void run() {
		Thread t = Thread.currentThread();
		System.out.println("run() by \t" + t);
		System.out.println("eh = " + t.getUncaughtExceptionHandler());
		throw new RuntimeException();//抛出异常 用于捕获
	}
	
 }
	
	/**
	 * 处理未捕获的异常处理类
	 * @author Administrator
	 *
	 */
	class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			System.out.println("caught \t"+e);
		}
	}
	
	/**
	 * MyUncaughtExceptionHandler工厂类 
	 * @author Administrator
	 *
	 */
	class HandlerThreadFactory implements ThreadFactory{
		@Override
		public Thread newThread(Runnable r) {
			System.out.println(this + "creating  new Thread");
			Thread t = new Thread(r);//创建一个线程
			System.out.println("created" + t);
			t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());//设置捕获异常的类
			System.out.println("eh = "+ t.getUncaughtExceptionHandler());//输出捕获异常的类
			return t;
		}
	}
	

