package Concurrent_21_5;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Sender2 implements Runnable{
	private Random rand = new Random(47);
	
	private CharQueue out = new CharQueue();
	
	public CharQueue getCharQueueOut(){
		return out;
	}
	
	@Override
	public void run() {
		try {
			while(true){
				for(char c = 'A'; c <= 'z'; c++){
					out.put(c);
					TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				}
			}
		} catch (InterruptedException e) {
			System.out.println(e+"Sender interrupted");
		}
	}
}

class CharQueue extends LinkedBlockingQueue<Character>{};

class Reciver2 implements Runnable{
	private CharQueue in;

	public Reciver2(Sender2 sender) {
		in = sender.getCharQueueOut();
	}

	@Override
	public void run() {
		try {
			while(true){
				System.out.println("read:"+in.take()+",");
			}
		} catch (Exception e) {
			System.out.println(e+"Reciver read exception");
		}
	}
}

public class PipedIO_Pratice_30 {
	public static void main(String[] args) throws IOException, InterruptedException {
		Sender sender = new Sender();
		Reciver receiver = new Reciver(sender);
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(sender);
		exec.execute(receiver);
		TimeUnit.SECONDS.sleep(4);
		exec.shutdownNow();
	}
}
