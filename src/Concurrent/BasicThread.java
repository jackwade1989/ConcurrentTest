package Concurrent;

public class BasicThread {
	public static void main(String[] args) {
		Thread t = new Thread(new RunnableTest());
		t.start();//启动RunnableTest中的run方法 启动后与main方法 的线程同时/执行
		System.out.println("Wating for RunnableTest!");
	}
}
