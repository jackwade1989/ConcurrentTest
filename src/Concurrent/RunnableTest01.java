package Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableTest01 {
	public static void main(String[] args) {
		for(int i=0; i<5;i++){
			new Thread(new RunnableTest()).start();
		}
		testExecuter();
		testFixedThreadPool();
	}
	
	/**
	 * //线程管理对象 为每个任务创建一个线程(自动分配) ，不需要显示地去创建了
	 */
	public static void testExecuter(){
		ExecutorService exec = Executors.newCachedThreadPool();
		//开始执行
		for(int i=0;i<5;i++){
			exec.execute(new RunnableTest());
		}
		exec.shutdown();//shutdown() 防止新的任务被提交给Executer
	}
	
	/**
	 * newFixedThreadPool 限制线程的数量的线程管理器
	 */
	public static void testFixedThreadPool(){
		ExecutorService exec = Executors.newFixedThreadPool(5);
		//开始执行
		for(int i=0;i<5;i++){
			exec.execute(new RunnableTest());
		}
		exec.shutdown();
	}
	
	/**
	 * newSingleThreadExecutor 单一线程的管理器 用于定制一些长期存活的任务
	 * 如果向newSingleThreadExecutor提交多个任务 那么这些任务将排队。
	 */
	public static void singleThreadPool(){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		//开始执行
		for(int i=0;i<5;i++){
			exec.execute(new RunnableTest());
		}
		exec.shutdown();
	}
}
