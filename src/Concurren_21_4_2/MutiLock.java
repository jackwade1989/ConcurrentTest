package Concurren_21_4_2;

public class MutiLock {
	public synchronized void f1(int count){
		if(count-- > 0){
			System.out.println("f1() calling f2() with count "+ count);
			f2(count);
		}
	}
	
	public synchronized void f2(int count){
		if(count-- > 0){
			System.out.println("f2() calling f1() with count "+ count);
			f1(count);
		}
	}
	
	public static void main(String[] args) {
		final MutiLock mLock = new MutiLock();
		new Thread(){
			public void run(){
				mLock.f1(10);
			}
		}.start();
		
	}
	
}

