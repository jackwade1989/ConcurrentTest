package Concurrent;

public class DeamonSpawn implements Runnable{
	@Override
	public void run() {
		while(true){
			Thread.yield();
		}
	}
}