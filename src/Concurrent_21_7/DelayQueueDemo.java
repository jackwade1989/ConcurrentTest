package Concurrent_21_7;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DelayedTask implements Runnable, Delayed {
	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayedTask> sequence = new ArrayList<DelayedTask>();

	public DelayedTask(int delayInmilliseconds) {
		delta = delayInmilliseconds;
		trigger = System.nanoTime() + NANOSECONDS.convert(delta, MILLISECONDS);
		sequence.add(this);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
	}

	@Override
	public int compareTo(Delayed o) {
		DelayedTask that = (DelayedTask) o;
		if (trigger < that.trigger) {
			return -1;
		}
		if (trigger > that.trigger) {
			return 1;
		}
		return 0;
	}

	@Override
	public void run() {
		System.out.println(this + "");
	}

	@Override
	public String toString() {
		return String.format("[%1$-4d]", delta) + "Task" + id+";";
	}

	public String summary() {
		return "(" + id + ":" + delta + ")";
	}

	public static class EndSentTinel extends DelayedTask {
		private ExecutorService exec;

		public EndSentTinel(int delay, ExecutorService e) {
			super(delay);
			exec = e;
		}

		public void run() {
			for (DelayedTask pt : sequence) {
				System.out.print(pt.summary() + "");
			}
			System.out.println("");
			System.out.println(this + " Calling shundownNow()");
			exec.shutdownNow();
		}

	}
}

class DelayedTaskConsumer implements Runnable {
	private DelayQueue<DelayedTask> q;

	public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
		this.q = q;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println("now is:"+q);
				q.take().run();
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
		System.out.print("Finished DelayedTaskConsumer");
	}

}

public class DelayQueueDemo {
	public static void main(String[] args) {
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
		for (int i = 0; i < 20; i++) {
			queue.put(new DelayedTask(rand.nextInt(5000)));
			queue.add(new DelayedTask.EndSentTinel(5000, exec));
			exec.execute(new DelayedTaskConsumer(queue));//由DelayQueue来管理启动DelayedTask 当DelayQueue发现有到期的任务则拿出来执行它
		}
	}
}
