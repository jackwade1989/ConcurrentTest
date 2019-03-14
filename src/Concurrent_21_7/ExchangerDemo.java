package Concurrent_21_7;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ExchangerProducer<T> implements Runnable{
	private Generator<T> generator;
	private Exchanger<List<T>> exchanger;
	private List<T> holder;
	
	ExchangerProducer(Exchanger<List<T>>exc,Generator<T> gen ,List<T>holder){
		exchanger = exc;
		generator = gen;
		this.holder = holder;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				for(int i = 0;i < ExchangerDemo.size ; i++){
					holder.add(generator.next());
					holder = exchanger.exchange(holder);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class ExchangerConsumer<T> implements Runnable{
	private Exchanger<List<T>>exchanger;
	private List<T> holder;
	private volatile T value;
	
	public ExchangerConsumer(Exchanger<List<T>> exchanger, List<T> holder) {
		this.exchanger = exchanger;
		this.holder = holder;
	}


	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				holder = exchanger.exchange(holder);
				for(T x : holder){
					value = x;
					holder.remove(x);
				}
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
		System.out.println("Final value" + value);
	}
}

public class ExchangerDemo {
	static int size = 10;
	static int delay = 5;
	public static void main(String[] args) {
		if(args.length > 0){
			size = new Integer(args[0]);
		}
		if(args.length > 1){
			delay = new Integer(args[1]);
		}
		ExecutorService exec = Executors.newCachedThreadPool();
		Exchanger<List<Fat>> xc = new Exchanger<List<Fat>>();
		List<Fat>producerList = new CopyOnWriteArrayList<Fat>(),
				 consumerList = new CopyOnWriteArrayList<Fat>();
		 exec.execute(new ExchangerProducer<Fat>(xc,
			      BasicGenerator.create(Fat.class), producerList));
		 exec.execute(
			      new ExchangerConsumer<Fat>(xc,consumerList));
			    TimeUnit.SECONDS.sleep(delay);
			    exec.shutdownNow();
		
	}
}
