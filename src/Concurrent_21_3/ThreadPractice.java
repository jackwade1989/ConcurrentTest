package Concurrent_21_3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPractice implements Runnable{
	private static int id;
	private Practice_1 p;
	
	@Override
	public void run() {
		++id;
		String str [][] = {{"java","good"},{"c++","good"},{"jquery","good"}};
		switch (id) {
		case 1:
			p.init(str[0][0],str[0][1],id);
			break;
		case 2:
			p.init(str[1][0],str[1][1],id);
			break;
		case 3:
			p.init(str[2][0],str[2][1],id);
		default:
			p.init(str[0][0],str[0][1],id);
			break;
		}
	}
	
	
	
	public ThreadPractice(int id, Practice_1 p) {
		this.id = id;
		this.p = p;
	}



	public static void test(Practice_1 con,int count){
		System.out.println("task start!");
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int j=0;j<count;j++){
			executorService.execute(new ThreadPractice(id,con));
		}
		executorService.shutdown();
	}
	
	public static void test(Practice_1 con){
		test(con,10);
	}
	
}
