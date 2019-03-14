package Concurrent_21_3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明 此程序用来演示数据的原子性 在一个线程使用的值结束前，可能会被其他线程调用赋值操作，改变值，从而不能保证值的原子性
 * @author Administrator
 *
 */
public class EvenChecker implements Runnable{
	
	private IntGenerator generator;
	
	private final int id;
	
	public EvenChecker(IntGenerator g, int ident) {
		generator = g;
		id = ident;
	}

	@Override
	public void run() {
		while(!generator.isCanceled()){
			int val = generator.next();
			
			if(val % 2 != 0){
				System.out.println("id call:"+id);
				System.out.println("ident:"+id+"\t"+val + ":not even!");
				generator.cancel(); //Cancel all EvenCheckers
			}
		}
	}
	
	public static void test(IntGenerator gp , int count){
		System.out.println("Press ctrl-c to exit");
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0;i<count;i++){
			exec.execute(new EvenChecker(gp, i));
		}
		exec.shutdown();
	}
	
	public static void test(IntGenerator gp){
		test(gp,10);
	}
	

}
