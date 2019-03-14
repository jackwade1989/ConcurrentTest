package Concurren_21_4_2;

import java.util.concurrent.TimeUnit;

class NeedCleanUp{
	private final int id;
	public NeedCleanUp(int ident){
		id = ident;
		System.out.println("Needs cleanup:"+id);
	}
	
	public void cleanup(){
		System.out.println("clean up :"+id);
	}
	
}

class Blocked3 implements Runnable{
	private volatile double d = 0.0;
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){//��K�˄tһֱ�^�m
				System.out.println("while after");
				//point 1
				NeedCleanUp n1 = new NeedCleanUp(1);
				//Start try-finally immediately after definition of n1.to gurantee proper cleanup of n1
				try {
					System.out.println("sleeping");
					TimeUnit.SECONDS.sleep(1);
					//point 2
					NeedCleanUp n2 = new NeedCleanUp(2);
					//gurantee proper cleanup of n2
					try{
						System.out.println("calculating");
						//A time - consuming , non blocking operation.
						for (int i = 1; i < 2500000; i++) {
							d = d + (Math.PI + Math.E) / d;
						}
						System.out.println("Finshed time-consuming operation");
					}finally{
						n2.cleanup();
					}
				} finally {
					n1.cleanup();
				}
			}
			System.out.println("Exiting via while() test");
		} catch (InterruptedException e){
			System.out.println("Exiting via InterruptedException");
		}
		
	}
	
}

public class InteruptingIdiom {
	public static void main(String[] args) throws Exception {
		System.out.println(args.length);
//		if(args.length != 0){
//			System.out.println("usage :java InteruptingIdiom delay in mS");
//			System.exit(1);
//		}
		Thread t = new Thread(new Blocked3());
		t.start();
		TimeUnit.MILLISECONDS.sleep(new Integer(800));//�O�����t �������t���¼�Blocked3���ڲ�ͬ�ĵ��c�˳�Blocked3.run()
		t.interrupt();
	}
}
