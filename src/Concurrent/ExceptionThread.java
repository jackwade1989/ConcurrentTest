package Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

	public class ExceptionThread implements Runnable{
	
		@Override
		public void run() {
			throw new RuntimeException();//抛出异常
		}
		
		public static void main(String[] args) {//但是在main方法中并没有捕获
			try {
				ExecutorService exec = Executors.newCachedThreadPool();
				exec.execute(new ExceptionThread());
			} catch (Exception e) {
				e.getMessage();
			}
		}
		
	}
