package Concurrent_21_5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast2 {
	private Status status = Status.DRY;

	private final int id;

	public Toast2(int id) {
		System.out.println("status:" + status);
		this.id = id;
	}

	public enum Status {
		DRY, BUTTERED, JAMMED, READY {
			public String toString() {
				return BUTTERED.toString() + "&" + JAMMED.toString();
			}
		}
	};

	public void butter() {
		status = (status == Status.DRY) ? Status.BUTTERED : Status.READY;
	}

	public void jam() {
		status = (status == Status.DRY) ? Status.JAMMED : Status.READY;
	}

	public Status getStatus() {
		return status;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Toast2 [status=" + status + ", id=" + id + "]";
	}
}

class ToastQueue2 extends LinkedBlockingQueue<Toast2> {};

/**
 * 面包制作
 * 
 * @author Administrator
 *
 */
class Toaster2 implements Runnable {
	private ToastQueue2 toastQueue;

	private int count = 0;

	private Random random = new Random(47);

	public Toaster2(ToastQueue2 toastQueue) {
		this.toastQueue = toastQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(500));
				Toast2 t = new Toast2(count++);
				System.out.println(t);
				toastQueue.put(t); // 放入面包队列
			}
		} catch (Exception e) {
			System.out.println("Toaster interrupted");
		}
		System.out.println("Toaster off");
	}
}

class Butterer2 implements Runnable {
	private ToastQueue2 inQueue, butteredQueue;

	public Butterer2(ToastQueue2 inQueue, ToastQueue2 butteredQueue) {
		this.inQueue = inQueue;
		this.butteredQueue = butteredQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast2 t = inQueue.take();
				t.butter();
				System.out.println(t);
				butteredQueue.put(t);
			}
		} catch (Exception e) {
			System.out.println("Butterer2 interruped");
		}
		System.out.println("buttered off");
	}
}

class Jammer2 implements Runnable {
	private ToastQueue2 inQueue, jammedQueue;

	public Jammer2(ToastQueue2 inQueue, ToastQueue2 jammedQueue) {
		this.inQueue = inQueue;
		this.jammedQueue = jammedQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast2 t = inQueue.take();
				t.jam();
				System.out.println(t);
				jammedQueue.put(t);
			}
		} catch (Exception e) {
			System.out.println("jammer2 interruped");
		}
		System.out.println("jammer2 off");
	}
}

class Eater2 implements Runnable {
	private ToastQueue2 finishedQueue;

	public Eater2(ToastQueue2 finishedQueue) {
		this.finishedQueue = finishedQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast2 t = finishedQueue.take();
				
				System.out.println(t);
				if(t.getStatus() != Toast2.Status.READY){
					System.out.println("ERROR :"+ t);
				}else{
					System.out.println("chomp................."+ t);
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Eater2 interruped");
		}
		System.out.println("Eater2 off");
	}
}

/**
 * 将两个任务类组装在一起
 * @author Administrator
 *
 */
class Alternator implements Runnable{
	private ToastQueue2 inQueue,out1Queue,out2Queue;
	private boolean outTo2;//control alternation
	
	public Alternator(ToastQueue2 inQueue, ToastQueue2 out1Queue, ToastQueue2 out2Queue) {
		System.out.println("outTo2:"+ outTo2);
		this.inQueue = inQueue;
		this.out1Queue = out1Queue;
		this.out2Queue = out2Queue;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				Toast2 t = inQueue.take();
				if(!outTo2){
					out1Queue.put(t);
				}
				else
					out2Queue.put(t);//change state for next time
				outTo2 = !outTo2;
				System.out.println("in run as outTo2:"+outTo2);
			}
		} catch (InterruptedException e) {
			System.out.println("in run as outTo2:"+outTo2);
			System.out.println("Alternator interruped");
		}
		System.out.println("Alternator off");
	}
}

/**
 * Merge 此处用来合并任务
 * @author Administrator
 *
 */
class Merge implements Runnable{
	private ToastQueue2 in1Queue,in2Queue,toBeButteredQueue,toBeJammedQueue,finishedQueue;

	public Merge(ToastQueue2 in1, ToastQueue2 in2, ToastQueue2 toBeButtered, ToastQueue2 toBeJammed,
			ToastQueue2 finished) {
		this.in1Queue = in1;
		this.in2Queue = in2;
		this.toBeButteredQueue = toBeButtered;
		this.toBeJammedQueue = toBeJammed;
		this.finishedQueue = finished;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				Toast2 t = null;
				while(t == null){
					t = in1Queue.poll(50,TimeUnit.MILLISECONDS);
					if(t!=null){
						break;
					}
					t = in2Queue.poll(50,TimeUnit.MILLISECONDS);
				}
				//Relay toast onto the proper queue
				switch (t.getStatus()) {
				case BUTTERED:
					toBeJammedQueue.put(t);
					break;
				case JAMMED:
					toBeButteredQueue.put(t);
				default:
					finishedQueue.put(t);
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Merge interrupted");
		}
		System.out.println("Merge off");
	}
}

public class ToastOmatic_Pratice_29 {
	public static void main(String[] args) throws InterruptedException {
		ToastQueue2 dryQueue = new ToastQueue2(),
				butteredQueue = new ToastQueue2(),
				toBebutteredQueue = new ToastQueue2(),
				jammedQueue = new ToastQueue2(),
				toBeJammedQueue = new ToastQueue2(),
				finishedQueue = new ToastQueue2();
		
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Toaster2(dryQueue));
		exec.execute(new Alternator(dryQueue, toBebutteredQueue, toBeJammedQueue));
		exec.execute(new Butterer2(toBebutteredQueue, butteredQueue));
		exec.execute(new Jammer2(toBeJammedQueue, jammedQueue));
		exec.execute(new Merge(butteredQueue, jammedQueue, toBebutteredQueue, toBeJammedQueue, finishedQueue));
		exec.execute(new Eater2(finishedQueue));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
