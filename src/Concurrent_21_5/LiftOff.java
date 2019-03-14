package Concurrent_21_5;

public class LiftOff implements Runnable{
protected int countDown = 10;
private static int taskCount = 0;
private final int id = taskCount++;

	public LiftOff() {}
	
	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	public String status (){
		return "#" + id + "(" + (countDown >0 ? countDown : "LiftOff!") + ") ,";
	}


	@Override
	public void run() {
		while(countDown--> 0){
			System.out.print(status());//输出任务信息
			Thread.yield();//让步
		}
	}

}
