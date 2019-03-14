package concurrent_21_4_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 程序的总计数器
 * @author Administrator
 *
 */
class Count{
	private int count = 0;
	private Random rand = new Random(47);
	
	public synchronized int increment(){
		int temp = count;
		if(rand.nextBoolean()){//线程的切换
			Thread.yield();
		}
		return (count = ++temp);//所有的任务将会在这里面统计
	}
	
	/**
	 * 
	 * @return 返回总计数器的值
	 */
	public synchronized int value(){
		return count;
	}
	
}

/**
 * 
 * @author Administrator
 *
 */
class Entrance implements Runnable{
	private static Count count = new Count();
	private static List<Entrance> entrances = new ArrayList<Entrance>();
	private int number = 0;
	
	private final int id;
	private static volatile boolean canceled = false;
	
	public static void cancel(){
		canceled = true;
	}
	
	public Entrance(int id) {
		this.id = id;
		entrances.add(this);
	}

	@Override
	public void run() {
		while(!canceled){//如果不是false就不会结束任务 当调用者的循环结束后 任务结束
			synchronized (this) {//这里是同步的关键 
				++number;//单独线程独享的数字 花园的各个入口的统计
			}
			System.out.println(this+" Total:"+count.increment());//调用总计数器并增长 不在代码同步块中 多个任务共享的调用
			try {
				TimeUnit.MICROSECONDS.sleep(100);//100毫秒调用一次
			} catch (Exception e) {
				System.out.println("sleep interrupted");
			}
		}
		System.out.println("Stopping"+this);
	}
	
	/**
	 * 返回此线程的人数
	 * @return
	 */
	public synchronized int getValue(){
		return number;
	}

	@Override
	public String toString() {
		return "Entrance [number=" + number + ", id=" + id + "]";
	}
	
	/**
	 * 返回总计数器的人数
	 * @return
	 */
	public static int getTotalCount(){
		return count.value();
	}
	
	/**
	 * 将不同线程统计统计的人数汇总
	 * @return
	 */
	public static int sumEntrances(){
		int sum = 0;
		for(Entrance entrance:entrances){
			sum += entrance.getValue();
		}
		return sum;
	}
	
}

public class OrnamentalGarden {
	public static void main(String[] args) throws InterruptedException {
//		Random r = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		//线程切换时这里的任务会同时运行 知道循环结束
		for(int i=0;i<5; i++){
			exec.execute(new Entrance(i));
			
		}
		//在3秒后结束任务
		TimeUnit.SECONDS.sleep(3);
		Entrance.cancel();
		exec.shutdown();
		
		//如果有未结束的等待250微秒后输出
		if(!exec.awaitTermination(250, TimeUnit.MICROSECONDS)){
			System.out.println("Some tasks were not terminated!");
		}
		//分别输出Total总计数器计数的和每个线程汇总的数量
		System.out.println("Total:" + Entrance.getTotalCount());
		System.out.println("Sum of Entrance:" + Entrance.sumEntrances());
		
	}
}
