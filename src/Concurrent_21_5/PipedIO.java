package Concurrent_21_5;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Sender implements Runnable{
	private Random rand = new Random(47);
	
	private PipedWriter out = new PipedWriter();
	
	public PipedWriter getPipedWriter(){
		return out;
	}
	
	@Override
	public void run() {
		try {
			while(true){
				for(char c = 'A'; c <= 'z'; c++){
					out.write(c);
					TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				}
			}
		} catch (IOException e) {
			System.out.println(e+"Sender write exception");
		} catch (InterruptedException e) {
			System.out.println(e+"Sender sleep interrupted");
		}
	}
}

class Reciver implements Runnable{
	private PipedReader in;

	public Reciver(Sender sender) throws IOException {
		in = new PipedReader(sender.getPipedWriter());
	}

	@Override
	public void run() {
		try {
			while(true){
				System.out.println("read:"+(char)in.read()+",");
			}
		} catch (Exception e) {
			System.out.println(e+"Reciver read exception");
		}
	}
}

public class PipedIO {
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
