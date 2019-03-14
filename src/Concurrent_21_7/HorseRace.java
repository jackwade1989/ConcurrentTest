package Concurrent_21_7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Horse implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private int strides = 0;
	private static Random rand = new Random(47);
	private static CyclicBarrier barrier;
	public synchronized int getStrides(){
		return strides;
	}
	public Horse(CyclicBarrier b) {
		this.barrier = b;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){ //为马匹产生随机数步伐  一次只有一个人任务进来
				synchronized (this) {
					strides += rand.nextInt(3);
				}
				barrier.await();//产生后阻塞当前线程 让下一个线程进来产生步伐
			}
		} catch (InterruptedException e) {
			System.out.println("InterruptedException"+e);
		} catch (BrokenBarrierException e) {
			System.out.println("BrokenBarrierException");
			throw new RuntimeException(e);
		}
	}
	@Override
	public String toString() {
		return "Horse [id=" + id + "]";
	}
	
	/**
	 * 打印出每匹马的运动踪迹
	 * @return
	 */
	public String tracks(){
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < getStrides();i++){
			s.append("*");
		}
		s.append(id);
		return s.toString();
	}
	
}

/**
 * 赛马类
 * @author Administrator
 *
 */
public class HorseRace{
	//终点线
	static final int FINISH_LINE = 75;
	
	private List<Horse> horses = new ArrayList<Horse>();
	
	private ExecutorService exec = Executors.newCachedThreadPool();
	private CyclicBarrier barrier;
	
	public HorseRace (int nHorse,final int pause){
		barrier = new CyclicBarrier(nHorse,new Runnable() {//nHorse 指定启动barrier前需调用await的数量。(new Runnable())在启动 barrier 时执行的命令；如果不执行任何操作，则该参数为 null 
			@Override
			public void run() {
				StringBuilder s = new StringBuilder();
				for(int i = 0; i <FINISH_LINE;i++){
					s.append("=");//输出程序的分割点
				}
				System.out.println(s.toString());
				for(Horse horse : horses){
					System.out.println(horse.tracks());//输出马匹的运动踪迹
				}
				for(Horse horse : horses){
					if(horse.getStrides() >= FINISH_LINE){//输出最先到达终点的马匹 产生冠军后结束任务
						System.out.print(horse + "won");
						exec.shutdownNow();
						return;
						
					}
					try {
						TimeUnit.MILLISECONDS.sleep(pause);
					} catch (InterruptedException e) {
						System.out.println("barrier-action sleep interrupted");
					}
				}
			}
		});
		for(int i = 0;i<nHorse;i++){
			Horse horse = new Horse(barrier);
			horses.add(horse);//添加马匹
			exec.execute(horse);//执行踪迹 产生步伐线程
			
		}
	}
	
	public static void main(String[] args) {
		int nHorses = 7;
		int pause = 200;
		
		if(args.length > 0){
			int n = new Integer(args[0]);
			nHorses = n >  0 ? n :nHorses;
		}
		if(args.length > 1){
			int p = new Integer(args[1]);
			pause = p > -1 ? p : pause;		
		}
		new HorseRace(nHorses, pause);
	}
	
}

