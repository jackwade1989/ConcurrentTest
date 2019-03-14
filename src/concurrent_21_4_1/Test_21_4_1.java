package concurrent_21_4_1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TotalCounte {
	private int counter = 0;
	
	public synchronized int value() {
		return counter;
	}
	
	public synchronized int increment(){
		Random rand = new Random(47);
		int temp = counter;
		if(rand.nextBoolean()){
			Thread.yield();
		}
		return (counter = ++temp);
	}
}

/**
 * 单独的计数器
 * @author Administrator
 *
 */
class Counte implements Runnable{
	private int number;
	private final int id;
	private static volatile boolean canceled = false;
	private static TotalCounte tc = new TotalCounte();
	private static List<Counte> counteList = new ArrayList<Counte>();
	
	public Counte(int id) {
		this.id = id;
		counteList.add(this);
	}

	@Override
	public void run() {
		while(!canceled){
			synchronized (this) {
				++number;
			}
			System.out.println(this+" total count:"+tc.increment());
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (Exception e) {
				System.out.println("sleep interuped!");
			}
		}
		
		System.out.println("stopping:" + this);
	}
	
	public static void cancel(){
		canceled = true;
	}

	public synchronized int getNumber() {
		return number;
	}
	
	/**
	 * 返回总计数器
	 */
	
	public static int getTotalCount(){
		return tc.value();
	}
	
	/**
	 * 返回每个单独计数器的的汇总
	 */
	public static int getTotalNumbers(){
		int total = 0;
		for(Counte c:counteList){
			total += c.getNumber();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Counte [number=" + number + ", id=" + id + "]";
	}
	
	public static void customizationCounte(int countNumbs) throws InterruptedException{
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0;i<countNumbs;i++){
			exec.execute(new Counte(i));
		}
		TimeUnit.SECONDS.sleep(3);
		Counte.cancel();
		exec.shutdown();
		if(!exec.awaitTermination(300, TimeUnit.MICROSECONDS)){
			System.out.println("Some tasks were not terminate!");
		}
		System.out.println("总计数器："+Counte.getTotalCount());
		System.out.println("单独计数器汇总:"+Counte.getTotalNumbers());
	}
}



public class Test_21_4_1 {
	public static void main(String[] args) throws InterruptedException {
		Counte.customizationCounte(5);
	}
}
