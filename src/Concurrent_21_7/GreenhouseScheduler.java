package Concurrent_21_7;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GreenhouseScheduler {
	private volatile boolean light = false;
	private volatile boolean water = false;
	private String thermostat = "Day";

	public synchronized String getThermostat() {
		return thermostat;
	}

	public synchronized void setThermostat(String value) {
		thermostat = value;
	}

	ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(10);

	/**
	 * 一个运行一次任务的方法
	 * 
	 * @param event 要执行的任务
	 * @param delay 延后执行的时间
	 */
	public void schedule(Runnable event, long delay) {
		schedule.schedule(event, delay, TimeUnit.MILLISECONDS);// 运行一次任务
	}

	/**
	 * 可以在指定的时间重复运行的任务
	 * 
	 * @param event 要执行的任务
	 * @param initialDelay初始延迟的时间
	 * @param period 连续执行之间的周期
	 */
	public void repeat(Runnable event, long initialDelay, long period) {
		schedule.scheduleAtFixedRate(event, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	class LightOn implements Runnable {

		@Override
		public void run() {
			// 开灯
			System.out.println("Turning on lights");
			light = true;
		}
	}

	class LightOff implements Runnable {

		@Override
		public void run() {
			// 关灯
			System.out.println("Turning off lights");
			light = false;
		}
	}

	class WaterOn implements Runnable {
		@Override
		public void run() {
			System.out.println("Turning greenhouse water on");
			water = true;// 开水

		}
	}

	class WaterOff implements Runnable {
		@Override
		public void run() {
			System.out.println("Turning greenhouse water off");
			water = false;// 关水

		}
	}

	// 恒温的夜间设定
	class ThermostaNight implements Runnable {

		@Override
		public void run() {
			System.out.println("Thermosta to night setting");
			System.out.println("Night");
		}
	}

	// 恒温的白天设定
	class ThermostaDay implements Runnable {

		@Override
		public void run() {
			System.out.println("Thermosta to Day setting");
			System.out.println("Day");
		}
	}

	class Bell implements Runnable {

		@Override
		public void run() {
			System.out.println("Bing");
		}
	}
	
	/**
	 * 终止 输出数据
	 * @author Administrator
	 *
	 */
	class Terminate implements Runnable{

		@Override
		public void run() {
			System.out.println("Terminating");
			schedule.shutdownNow();
			//当scheduler已经关闭的时候 必须启动一个线程来做这个工作
			new Thread(){
				public void run(){
					for(DataPoint d:data){
						System.out.println(d);
					}
				}
			}.start();
		}
	}
	
	/**
	 * 数据收集
	 * @author Administrator
	 *
	 */
	static class DataPoint{
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
			return time.getTime() + String.format("temprature : %1$.1f humidity : %2$.2f", temperature,humidity);
		}
	}
	
	private Calendar lastTime = Calendar.getInstance();
	{//调整至半个小时
		lastTime.set(Calendar.MINUTE, 30);
		lastTime.set(Calendar.SECOND, 00);
	}
	
	private float lastTemp = 65.0f;
	private int tempDirection = +1;
	private float lastHumdity = 50.0f;
	private int humidityDirection = +1;
	private Random rand = new Random();
	List<DataPoint>data = Collections.synchronizedList(new ArrayList<DataPoint>());
	
	/**创造仿真数据
	 * 
	 * @author Administrator
	 *
	 */
	class CollectData implements Runnable{

		@Override
		public void run() {
			System.out.println("Collecting data");
			synchronized (GreenhouseScheduler.this) {
				//pretend the interval is longer than it is 假装间隔时间比它长
				lastTime.set(Calendar.MINUTE, lastTime.get(Calendar.MINUTE)+30);
				//one in 5 chances of reversing the direction  //有5分之1的几率会改变方向
				if(rand.nextInt(5)==4){
					tempDirection = -tempDirection;
				}
				//store previous value 储存前一个值
				lastTemp = lastTemp + tempDirection * (1.0f + rand.nextFloat());
				if(rand.nextInt(5)==4){
					humidityDirection = -humidityDirection;
				}
				lastHumdity = lastHumdity + humidityDirection * rand.nextFloat();
				data.add(new DataPoint((Calendar) lastTime.clone(), lastTemp, lastHumdity));
			}
		}
	}
	
	public static void main(String[] args) {
		GreenhouseScheduler gh = new GreenhouseScheduler();
		gh.schedule(gh.new Terminate(),5000);
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


	


