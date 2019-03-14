package Concurrent_21_7;

import java.util.concurrent.Semaphore;

public class PlayGround {
	/**
	 * 跑道类
	 */
	static class Track{
		private int num;

		public Track(int num) {
			super();
			this.num = num;
		}

		@Override
		public String toString() {
			return "Track [num=" + num + "]";
		}
	}
	
	private Track[] tracks = {
			new Track(1),new Track(2),new Track(3),new Track(4),new Track(5)
	};
	
	private volatile boolean[] used = new boolean[5];
	
	//创建有效的许可证数量
	private Semaphore semaphore = new Semaphore(5,true);
	
	/**
	 * 获取一个跑道
	 */
	public Track getTrack() throws InterruptedException{
		semaphore.acquire();
		return getNextAvailableTrack();
	}
	
	/**
	 * 返回一个跑道
	 * 
	 */
	public void releaseTrack(Track track){
		if(makeAsUsed(track)){
			semaphore.release(1);//释放给定数目的许可，将其返回到信号量。
		}
	}
	
	/**
	 * 遍历，找到一个没人用的跑道
	 * 
	 */
	private Track getNextAvailableTrack(){
		for(int i = 0;i<used.length;i++){
			if(!used[i]){
				used[i] = true;
				return tracks[i];
			}
		}
		return null;
	}
	
	/**
	 * 返回一个跑道
	 */
	private boolean makeAsUsed(Track track){
		for(int i = 0;i<used.length;i++){
			if(tracks[i] == track){
				if(used[i]){
					used[i] = false;
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
}
