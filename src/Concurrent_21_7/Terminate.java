package Concurrent_21_7;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

abstract class Event1 implements Runnable, Delayed {
	protected final long delayTime;
	private long trigger;

	public Event(long delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * 当前元素比队列元素后执行时，返回一个正数，比它先执行时返回一个负数，否则返回0.
	 */
	@Override
	public int compareTo(Delayed o) {
		Event that = (Event) o;
		if (this.trigger < that.trigger) {
			return -1;
		}
		if (this.trigger > that.trigger) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
	}

	public void start() {
		trigger = System.nanoTime() + NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS);
	}

	public abstract void run();
}

/**
 * 从Delayqueue取出到期的任务并运行
 * 
 * @author Administrator
 *
 */
class Controller implements Runnable {
	private DelayQueue<Event> q;

	public Controller(DelayQueue<Event> q) {
		this.q = q;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Event event = q.take();
				System.out.println(event);
				event.run();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Finished Controller");
	}

	public void addEvent(Event e) {
		e.start();// ?
		q.put(e);
	}
}

class GreenhouseController extends Controller {
	public GreenhouseController(DelayQueue<Event> q) {
		super(q);
		// TODO Auto-generated constructor stub
	}

	private boolean light = false;

	public class LightOn extends Event {

		public LightOn(long delayTime) {
			super(delayTime);
		}

		@Override
		public void run() {
			light = true;
		}

		@Override
		public String toString() {
			return "Light is on";
		}

		public class LightOff extends Event {

			public LightOff(long delayTime) {
				super(delayTime);
			}

			@Override
			public void run() {
				light = false;
			}

			@Override
			public String toString() {
				return "Light is off";
			}
		}

		private boolean water = false;

		public class WaterOn extends Event {

			public WaterOn(long delayTime) {
				super(delayTime);
			}

			@Override
			public void run() {
				water = true;
			}

			@Override
			public String toString() {
				return "water is on";
			}
		}

		public class WaterOff extends Event {

			public WaterOff(long delayTime) {
				super(delayTime);
			}

			@Override
			public void run() {
				water = false;
			}

			@Override
			public String toString() {
				return "water is off";
			}
		}

		private String thermostat = "Day";

		public class thermostatNight extends Event {

			public thermostatNight(long delayTime) {
				super(delayTime);
			}

			@Override
			public void run() {
				thermostat = "Night";
			}

			@Override
			public String toString() {
				return "thermostat on night setting";
			}
		}

		public class thermostatDay extends Event {

			public thermostatDay(long delayTime) {
				super(delayTime);
			}

			@Override
			public void run() {
				thermostat = "Day";
			}

			@Override
			public String toString() {
				return "thermostat on day setting";
			}
		}

			public class Restart extends Event {
				public Restart(long delayTime, Event[] evenList) {
					super(delayTime);
					this.evenList = evenList;
					for (Event e : evenList) {
						addEvent(e);
					}
				}

				private Event[] evenList;

				@Override
				public void run() {
					for (Event e : evenList) {
						addEvent(e);
					}
					addEvent(this);
				}
				public String toString() {
					return "Restarting system";
				}
			}
			/**
			 * 终止 输出数据
			 * 
			 * @author Administrator
			 *
			 */
			public class Terminate extends Event {
				public Terminate(long delayTime,ExecutorService e) {
					super(delayTime);	
					exec = e;
				}

				private ExecutorService exec;
				
				@Override
				public void run() {
					exec.shutdownNow();
				}

				@Override
				public String toString() {
					return "Terminating";
				}
			}
		}
	}

public class Terminate{
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
	}
}
	

	/**
	 * 数据收集
	 * 
	 * @author Administrator
	 *
	 */
	static class DataPoint {
		final Calendar time;
		final float temperature;
		final float humidity;

		public DataPoint(Calendar d, float temp, float hum) {
			this.time = d;
			this.temperature = temp;
			this.humidity = hum;
		}

		@Override
		public String toString() {
			return time.getTime() + String.format("temprature : %1$.1f humidity : %2$.2f", temperature, humidity);
		}
	}

	private Calendar lastTime = Calendar.getInstance();
	{// 调整至半个小时
		lastTime.set(Calendar.MINUTE, 30);
		lastTime.set(Calendar.SECOND, 00);
	}

	private float lastTemp = 65.0f;
	private int tempDirection = +1;
	private float lastHumdity = 50.0f;
	private int humidityDirection = +1;
	private Random rand = new Random();
	List<DataPoint> data = Collections.synchronizedList(new ArrayList<DataPoint>());

	/**
	 * 创造仿真数据
	 * 
	 * @author Administrator
	 *
	 */
	class CollectData implements Runnable {

		@Override
		public void run() {
			System.out.println("Collecting data");
			synchronized (GreenhouseSchedulerPratice_33.this) {
				// pretend the interval is longer than it is 假装间隔时间比它长
				lastTime.set(Calendar.MINUTE, lastTime.get(Calendar.MINUTE) + 30);
				// one in 5 chances of reversing the direction //有5分之1的几率会改变方向
				if (rand.nextInt(5) == 4) {
					tempDirection = -tempDirection;
				}
				// store previous value 储存前一个值
				lastTemp = lastTemp + tempDirection * (1.0f + rand.nextFloat());
				if (rand.nextInt(5) == 4) {
					humidityDirection = -humidityDirection;
				}
				lastHumdity = lastHumdity + humidityDirection * rand.nextFloat();
				data.add(new DataPoint((Calendar) lastTime.clone(), lastTemp, lastHumdity));
			}
		}
	}

	public static void main(String[] args) {
		GreenhouseSchedulerPratice_33 gh = new GreenhouseSchedulerPratice_33();
		gh.schedule(gh.new Terminate(), 5000);
		gh.repeat(gh.new Bell(), 0, 1000);
		gh.repeat(gh.new ThermostaNight(), 0, 2000);
		gh.repeat(gh.new LightOn(), 0, 200);
		gh.repeat(gh.new LightOff(), 0, 400);
		gh.repeat(gh.new WaterOn(), 0, 600);
		gh.repeat(gh.new WaterOff(), 0, 800);
		gh.repeat(gh.new ThermostaDay(), 0, 1400);
		gh.repeat(gh.new CollectData(), 500, 500);

	}
}
