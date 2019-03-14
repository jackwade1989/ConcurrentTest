package Concurrent_21_3_3;

import java.util.concurrent.atomic.AtomicInteger;

import Concurrent_21_3.EvenChecker;
import Concurrent_21_3.IntGenerator;

public class AtomicEvenGenerator extends IntGenerator{
	private AtomicInteger currentEvenValue = new AtomicInteger(0);
	
	public int next(){
		return currentEvenValue.addAndGet(3);
	}
	
	public static void main(String[] args) {
		EvenChecker.test(new AtomicEvenGenerator());
	}
	

}
