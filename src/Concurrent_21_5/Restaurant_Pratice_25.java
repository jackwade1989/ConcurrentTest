package Concurrent_21_5;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 服务员与厨师同属于饭店的动作 肉属于饭店的属性
 * 
 * @author Administrator
 *
 */

public class Restaurant_Pratice_25 {
	Meal_1 meal;
	WaitPerson_1 waitPerson = new WaitPerson_1(this);
	Chef_1 chef = new Chef_1(this);
	BusBoy boy = new BusBoy(this);
	ExecutorService exec = Executors.newCachedThreadPool();
	boolean isEated = false;

	public Restaurant_Pratice_25() throws InterruptedException, ExecutionException {
		exec.execute(waitPerson);
		exec.execute(boy);
		Future<?> cf = exec.submit(chef);
		// TimeUnit.SECONDS.sleep(10);
		System.out.println("cf:" + cf.get());
		// exec.shutdown();
	}

	public static void main(String[] args) {
		System.out.println("開始!");
		try {
			new Restaurant_Pratice_25();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Meal_1 {
	private int orderNum;

	public Meal_1(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "Meal_1 [orderNum=" + orderNum + "]";
	}

}

class WaitPerson_1 implements Runnable {

	private Restaurant_Pratice_25 resaurant;

	public WaitPerson_1(Restaurant_Pratice_25 resaurant) {
		this.resaurant = resaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					while (resaurant.meal == null) {// 没有肉 服务员等待
						System.out.println("服务员等待!");
						wait();
					}
					
					System.out.println("服务员拿了：" + resaurant.meal);
					TimeUnit.MILLISECONDS.sleep(100 * new Random().nextInt(100));
					synchronized (resaurant.boy) {
						System.out.println("用餐完毕!");
						resaurant.isEated = true;
						System.out.println("开始通知服务员搞卫生!");
						resaurant.boy.notifyAll();
					}
					while(resaurant.isEated){
						System.out.println("服务员等待打扫!");
						wait();
					}

					synchronized (resaurant.chef) {
						System.out.println("开始唤醒厨师！");
						resaurant.meal = null;
						resaurant.chef.notifyAll();
						
					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("服务员终止!");
		}
	}

}

class Chef_1 implements Callable<Integer> {

	private Restaurant_Pratice_25 restrurant;
	private int count = 0;

	public Chef_1(Restaurant_Pratice_25 restrurant) {
		this.restrurant = restrurant;
	}

	@Override
	public Integer call() {
		try {
			synchronized (this) {
				System.out.println("厨师开始！");
				while (!Thread.interrupted()) {
					while (restrurant.meal != null) {// 如果有肉则等待
						System.out.println("厨师等待！");
						wait();
					}
					if (++count > 10) {
						System.out.println("卖完了！");
						restrurant.exec.shutdownNow();
					}
					synchronized (restrurant.waitPerson) {
						System.out.println("开始做肉！");
						restrurant.meal = new Meal_1(count); // 有肉了
						restrurant.waitPerson.notifyAll(); // 唤醒服务员
						System.out.println("做肉完毕！");
					}
				}

			}
		} catch (InterruptedException e) {
			System.out.println("厨师唤醒失败");
		}
		return count;
	}

}

class BusBoy implements Runnable {
	private Restaurant_Pratice_25 restrurant;

	public BusBoy(Restaurant_Pratice_25 restrurant) {
		this.restrurant = restrurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					while (!restrurant.isEated) {
						wait();
					}
					synchronized (restrurant.waitPerson) {
						while (restrurant.isEated) {
							System.out.println("开始打扫!");
							System.out.println("清扫完毕!");
							restrurant.isEated = false;
							restrurant.waitPerson.notifyAll();
						}
					}
				}
			}

		} catch (InterruptedException e) {
			System.out.println("营业结束，清洁员回去休息!");
		}
	}

}