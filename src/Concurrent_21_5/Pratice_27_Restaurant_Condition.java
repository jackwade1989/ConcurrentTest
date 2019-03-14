package Concurrent_21_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * resturant使用Condition对象
 * 
 * @author Administrator
 *
 */
public class Pratice_27_Restaurant_Condition {
	private Chef2 chef = new Chef2(this);
	private WaitPerson2 waitPerson = new WaitPerson2(this);
	private Meal2 meal;
	ExecutorService exec = Executors.newCachedThreadPool();

	public Chef2 getChef() {
		return chef;
	}

	public void setChef(Chef2 chef) {
		this.chef = chef;
	}

	public WaitPerson2 getWaitPerson() {
		return waitPerson;
	}

	public void setWaitPerson(WaitPerson2 waitPerson) {
		this.waitPerson = waitPerson;
	}

	public Meal2 getMeal() {
		return meal;
	}

	public void setMeal(Meal2 meal) {
		this.meal = meal;
	}

	public Pratice_27_Restaurant_Condition() {
		exec.execute(waitPerson);
		exec.execute(chef);
	}

	public static void main(String[] args) {
		new Pratice_27_Restaurant_Condition();
	}

}

class Meal2 {
	private final int orderNum;

	public Meal2(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "Meal2 [orderNum=" + orderNum + "]";
	}
}

class WaitPerson2 implements Runnable {

	ExecutorService exec = Executors.newCachedThreadPool();

	Lock lock = new ReentrantLock();

	Condition condition = lock.newCondition();

	private Pratice_27_Restaurant_Condition resturant;

	public WaitPerson2(Pratice_27_Restaurant_Condition resturant) {
		this.resturant = resturant;
	}

	@Override
	public void run() {
		System.out.println("服务员开始工作!");
		while (!Thread.interrupted()) {
			lock.lock();
			try {
				while (resturant.getMeal() == null) {
					System.out.println("没有食物，服务员等待厨师做菜!");
					condition.await();
				}
			} catch (InterruptedException e) {
				System.out.println("服务员异常");
			} finally {
				lock.unlock();
			}

			System.out.println("服务员上了订单:" + resturant.getMeal());
			resturant.getChef().lock.lock();
			try {
				System.out.println("通知厨师做菜");
				resturant.setMeal(null);
				resturant.getChef().condition.signalAll();
			} finally {
				resturant.getChef().lock.unlock();
			}
		}
	}
}

class Chef2 implements Runnable {

	private Pratice_27_Restaurant_Condition resturant;

	private int count;

	ExecutorService exec = Executors.newCachedThreadPool();

	Lock lock = new ReentrantLock();

	Condition condition = lock.newCondition();

	public Chef2(Pratice_27_Restaurant_Condition resturant) {
		this.resturant = resturant;
	}

	@Override
	public void run() {
		
			while (!Thread.interrupted()) {
				lock.lock();
				try {
				while (resturant.getMeal() != null) {
					System.out.println("订单已有， 厨师等待！");
					condition.await();
				}
				if (++count == 10) {
					System.out.println("卖完了!");
					resturant.exec.shutdownNow();
				}
				
				} catch (Exception e) {
					System.out.println("厨师异常!");
				} finally {
					lock.unlock();
				}
				try {
					resturant.getWaitPerson().lock.lock();
					resturant.setMeal(new Meal2(count));
					System.out.println("厨师烧肉完毕!");
					resturant.getWaitPerson().condition.signalAll();
				} finally {
					resturant.getWaitPerson().lock.unlock();
				}
			}

	}

}
