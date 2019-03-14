package Concurrent_21_3_5;

import javax.xml.bind.helpers.ParseConversionEventImpl;

public class ExplicitCriticalSection {
	public static void main(String[] args) {
		PairManager 
			pman1 = new ExplicPairManager1(),
			pman2 = new ExplicPairManager2();
		CriticalSection.testApproaches(pman1, pman2);
	}
}
