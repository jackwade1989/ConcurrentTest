package Concurrent_21_5;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast {
	public enum Status {DRY,BUTTED,JAMMED};
	
	private Status status = Status.DRY;
	
	private final int id;
	
	public Toast(int idn){
		id = idn;
	}
	
	public void butter(){
		status = Status.BUTTED;
	}
	
	public void jam(){
		status = Status.JAMMED;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public int getId(){
		return id;
	}

	@Override
	public String toString() {
		return "Toast [status=" + status + ", id=" + id + "]";
	}
}

class ToastQueue extends LinkedBlockingQueue<Toast>{};

/**
 * 吐司制作队列
 * Toaster maker 
 * @author Administrator
 *
 */
class Toaster implements Runnable {
	
	private ToastQueue toastQueue;
	
	private int count = 0;
	
	private Random rand = new Random(47);
	
	public Toaster(ToastQueue tq){
		toastQueue = tq;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
				//make toast
				Toast t = new Toast(count++);
				System.out.println(t);
				// insert into queue
				toastQueue.put(t);
			}
		} catch (Exception e) {
			System.out.println("Toaster interrupted");
		}
		System.out.println("Toaster off");
	}
}

/**
 * 吐司涂抹黄油队列
 * @author Administrator
 *
 */
class Butterer implements Runnable {
	private ToastQueue dryQueue , butteredQueue;
	
	public Butterer(ToastQueue dryQueue, ToastQueue butteredQueue) {
		this.dryQueue = dryQueue;
		this.butteredQueue = butteredQueue;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				Toast t = dryQueue.take();//取出干的吐司
				t.butter();//抹酱操作
				System.out.println(t);
				butteredQueue.put(t);//放入涂过黄油的队列中
			}
		} catch (Exception e) {
			System.out.println("Butterer interrupted");
		}
	}
}

/**
 * 涂抹酱
 * @author Administrator
 *
 */
class Jammer implements Runnable{
	private ToastQueue butteredQueue,finshedQueue;

	public Jammer(ToastQueue butteredQueue, ToastQueue finshedQueue) {
		this.butteredQueue = butteredQueue;
		this.finshedQueue = finshedQueue;
	}

	@Override
	public void run() {
		while(!Thread.interrupted()){
			try {
				Toast t = butteredQueue.take();//取出抹过黄油的吐司
				t.jam();//涂抹酱
				System.out.println(t);
				finshedQueue.put(t);
			} catch (Exception e) {
				System.out.println("Jammer interrupted");
			}
			System.out.println("Jammer off");
		}
	}
}

class Eater implements Runnable {
	private ToastQueue finishedQueue;
	
	private int counter = 0;
	
	public Eater(ToastQueue finishedQueue) {
		this.finishedQueue = finishedQueue;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				Toast t = finishedQueue.take();//从完成的队列中取出吐司
				if(t.getId()!=counter++||t.getStatus()!=Toast.Status.JAMMED){//确认队列顺序或是否为已涂抹过的吐司
					System.out.println(">>>>> Error :" + t);
					System.exit(1);
				}else{
					System.out.println("Chomp!" + t);//吃吧 asshole
				}
			}
		} catch (Exception e) {
			System.out.println("Eater interrupted");
		}
		System.out.println("Easter off");
	}
}

public class ToastOmatic {
	public static void main(String[] args) throws InterruptedException {
		ToastQueue dryQueue = new ToastQueue(),
				   butteredQueue = new ToastQueue(),
				   finishedQueue = new ToastQueue();
		
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Toaster(dryQueue));
		exec.execute(new Butterer(dryQueue,butteredQueue));
		exec.execute(new Jammer(butteredQueue,finishedQueue));
		exec.execute(new Eater(finishedQueue));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
