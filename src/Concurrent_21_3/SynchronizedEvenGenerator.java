package Concurrent_21_3;

public class SynchronizedEvenGenerator extends IntGenerator{
	
	private int currentEvenValue = 0;

	@Override
	public synchronized int next() {
		++currentEvenValue;
		System.out.println("call one value:"+currentEvenValue);
		Thread.yield();
		++currentEvenValue;
		System.out.println("call two value:"+currentEvenValue);
		// TODO Auto-generated method stub
		return currentEvenValue;
	}
	
	public static void main(String[] args) {
		EvenChecker.test(new SynchronizedEvenGenerator());
	}

}
