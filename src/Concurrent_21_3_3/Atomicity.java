package Concurrent_21_3_3;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Atomicity implements Runnable {
	int i;

	void f1() {
		i++;
		System.out.println("f1:"+i);
	}

	void f2() {
		i += 3;
		System.out.println("f2:"+i);
	}

	@Override
	public void run() {
		f1();
		f2();
	}

	public static void main(String[] args) {
		boolean f = false;
		int k = 0;
		Executor exec = Executors.newCachedThreadPool();
		while(!f){
			exec.execute(new Atomicity());
			k++;
			if(k==50){
				f = false;
				break;
			}
			
		}
		
	}

}
