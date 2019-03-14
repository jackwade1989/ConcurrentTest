package Concurrent_21_5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

class LiftOffRunner2 implements Runnable{
	
	private BlockingQueue<LiftOff> rockets;
	
	/**
	 * 队列构造器 接收队列
	 * @param queue
	 */
	public LiftOffRunner2(BlockingQueue<LiftOff> queue) {
		this.rockets = queue;
	}
	
	public void add(LiftOff lo){
		try {
			rockets.put(lo);//将任务放进队列
		} catch (Exception e) {
			System.out.println("Interrupted during put()");
		}
	}
	

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				LiftOff rocket = rockets.take();//从队列中获取任务
				rocket.run();//运行任务
			}
		} catch (Exception e) {
			System.out.println("Waking from take()");
		}
		System.out.println("Exiting LiftOffRunner");
	}
	
}

/**
 * listOff 生产者
 * @author Administrator
 *
 */
class LiftOffProducer implements Runnable {
	private LiftOffRunner2 runner;

	public LiftOffProducer(LiftOffRunner2 runner2) {
		this.runner = runner2;
	}

	public void run() {
		for (int i = 0; i < 5; i++)
			runner.add(new LiftOff(5));
		System.out.println("Exiting LiftOffProducer");
	}
}

public class TestBlockingQueues_pratice_28 {
	static void getkey(){//获取控制台enter事件
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();//读取控制台信息
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * message 输出队列信息
	 * @param message
	 */
	static void getkey(String message){
		System.out.println(message);
		getkey();
	}
	
	static void test (String msg,BlockingQueue<LiftOff> queue){
		System.out.println(msg);
		ExecutorService exec = Executors.newFixedThreadPool(2);
		LiftOffRunner2 runner = new LiftOffRunner2(queue);
		LiftOffProducer producer = new LiftOffProducer(runner);
		exec.execute(runner);
		exec.execute(producer);
		getkey("Press 'Enter' (" + msg + ")");
		exec.shutdownNow();
		System.out.println("Finshed" + msg + "test");//输出完成信息 显示各种队列的排序取值相关
	}
	
	public static void main(String[] args) {
		test("LinkedBlockingQueue", new LinkedBlockingQueue<LiftOff>());//无固定大小队列
		test("ArrayBlockingQueue", new ArrayBlockingQueue<LiftOff>(3));//固定大小队列
		test("SynchronousQueue", new SynchronousQueue<LiftOff>());//无缓冲队列

	}
	
}
