package Concurrent_21_7;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ToTravel2 {
	public static void main(String[] args) {
		DelayQueue<DelayedTask1> delayQueue = new DelayQueue<DelayedTask1>();
		delayQueue.add(new DelayedTask1("chenchao", 1l, TimeUnit.MILLISECONDS));
		delayQueue.add(new DelayedTask1("chencWen", 2l, TimeUnit.MILLISECONDS));
		delayQueue.add(new DelayedTask1("chenYong", 3l, TimeUnit.MILLISECONDS));
		
		System.out.println("queue put done");
		
		while(!delayQueue.isEmpty()){
			try {
				DelayedTask1 task = delayQueue.take();
				System.out.println(task.name+":"+System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

class DelayedTask1 implements Delayed{
	public String name;
	
	public Long DelayTime;
	
	public TimeUnit timeUnit;
	
	public Long executeTime;
	
	public DelayedTask1(String name, Long delayTime, TimeUnit timeUnit) {
		this.name = name;
		DelayTime = delayTime;
		this.timeUnit = timeUnit;
		this.executeTime = System.currentTimeMillis() + timeUnit.toMillis(delayTime);
	}

	//compareTo方法的作用即是判断队列中元素的顺序谁前谁后。当前元素比队列元素后执行时，返回一个正数，比它先执行时返回一个负数，否则返回0.
	@Override
	public int compareTo(Delayed o) {
		if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)){
			return 1;
		}else if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)){
			return -1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	
}
