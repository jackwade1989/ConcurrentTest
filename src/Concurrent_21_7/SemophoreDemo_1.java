package Concurrent_21_7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SemophoreDemo_1 {
	static class Student implements Runnable{
		private int num;
		private PlayGround playground;
		public Student(int num, PlayGround playground) {
			this.num = num;
			this.playground = playground;
		}
		@Override
		public void run() {
			try {
				//获取跑道
				PlayGround.Track track = playground.getTrack();
				if(track!=null){
					System.out.println("学生" + num + "在" + track.toString());
					TimeUnit.SECONDS.sleep(2);
					System.out.println("学生" + num + "释放" + track.toString());
					//释放跑道
					playground.releaseTrack(track);
				} 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		PlayGround playGround = new PlayGround();
		for(int i = 0; i < 100; i++){
			executor.execute(new Student(i+1, playGround));
		}
	}
}
