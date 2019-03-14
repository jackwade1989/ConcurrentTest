package Concurrent_21_3_3;

public class SerialNumberGenerator {
	private static volatile int serialNumber = 0;
	public static int nextSerialNumber(){
		return serialNumber++;
	}
}
