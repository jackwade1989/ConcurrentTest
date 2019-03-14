package Concurrent_21_3;

public class EvenGenerator extends IntGenerator{
	
	private int currentEvenValue = 0;
	
	@Override
	public int next() {
		++currentEvenValue;//danger point here
		System.out.println("call one value:"+currentEvenValue);
		Thread.yield();
		++currentEvenValue;
		System.out.println("call two value:"+currentEvenValue);
		return currentEvenValue;
	}
	
	public static void main(String[] args) {
		EvenChecker.test(new EvenGenerator()); 
	}

}
