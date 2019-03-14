package Concurrent_21_5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务员与厨师同属于饭店的动作 肉属于饭店的属性
 * 
 * @author Administrator
 *
 */

public class Restarurant_Test {
	Meal_1 meal;
	WaitPerson_2 waitPerson = new WaitPerson_2(this);
	Chef_2 chef = new Chef_2(this);
	ExecutorService exec = Executors.newCachedThreadPool();

	public Restarurant_Test() throws InterruptedException, ExecutionException {
		exec.execute(waitPerson);
		exec.execute(chef);
		// TimeUnit.SECONDS.sleep(10);
		// exec.shutdown();
	}

	public static void main(String[] args) {
		System.out.println("開始!");
			try {
				new Restarurant_Test();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				System.out.println("log:============"+e.getMessage());
			}
	}

}

class Meal_2 {
	private int orderNum;

	public Meal_2(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "Meal_2 [orderNum=" + orderNum + "]";
	}

}

class WaitPerson_2 implements Runnable {

	private Restarurant_Test resaurant;

	public WaitPerson_2(Restarurant_Test resaurant) {
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

class Chef_2 implements Runnable {

	private Restarurant_Test restrurant;
	private int count = 0;

	public Chef_2(Restarurant_Test restrurant) {
		this.restrurant = restrurant;
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				System.out.println("厨师开始！");
				while (!Thread.interrupted()) {
					while (restrurant.meal != null) {// 如果有肉则等待
						System.out.println("厨师等待！");
						wait();
					}
					if (++count == 10) {
						System.out.println("卖完了！");
						restrurant.exec.shutdown();
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
	}
}
