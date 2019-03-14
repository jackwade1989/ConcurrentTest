package Concurrent_21_8;

import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 顾客类
 * @author Administrator
 *	
 */
class Customer {
	//客户获得的服务时间 
	private final int serviceTime;
	
	public int getServiceTime(){
		return serviceTime;
	}
	
	public Customer(int tm){
		serviceTime = tm;
	}

	@Override
	public String toString() {
		return "Customer [serviceTime=" + serviceTime + "]";
	}
	
}

/**
 * 指定ArrayBlocking大小及输出客户排队
 * @author Administrator
 *
 */
class CustomerLine extends ArrayBlockingQueue<Customer>{
	public CustomerLine(int maxLineSize){
		super(maxLineSize);
	}
	
	public String toString(){
		if(this.size() == 0){
			return "[Empty]";
		}
		StringBuilder result = new StringBuilder();
		for(Customer customer : this){
			result.append(customer);
		}
		return result.toString();
	}
}

/**
 * 随机的添加数组到客户队列
 * @author Administrator
 *
 */
class CustomerGenerator implements Runnable{
	private CustomerLine customers;
	private static Random rand = new Random(47);
	
	public CustomerGenerator(CustomerLine customers) {
		this.customers = customers;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(rand.nextInt(300));
				customers.put(new Customer(rand.nextInt(1000)));//添加客户 并初始化客户办理业务的时间
			}
		} catch (Exception e) {
			System.out.println("CustomerGenerator interrupted");
		}
		System.out.println("CustomerGenerator terminating");
	}
}

class Teller implements Runnable,Comparable<Teller>{
	
	private static int counter = 0;
	private final int id = counter ++;
	
	//customers served during this shift
	private int customersServed = 0; //服务的客户
	private CustomerLine customers;
	private boolean servingCustomerLine = true; //正在服务的客户队列
	
	public Teller(CustomerLine customers) {
		this.customers = customers;
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				Customer customer = customers.take();//柜员开始服务客户
				TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());//休眠时间取决于客户办理的业务()
				synchronized(this){
					customersServed++;
					while(!servingCustomerLine){//如果上一个客户没有服务完 在其他的线程等待
						wait();
					}
				}
			}
		} catch (Exception e) {
			System.out.println(this + " interrupted");
		}
		System.out.println(this + " terminating");
	}
	
	/**
	 * 柜员暂停中
	 */
	public synchronized void doSomethingElse(){
		customersServed = 0;
		servingCustomerLine = false;
	}
	
	/**
	 * 断言 显示某个正在服务的客户 
	 * 更改柜员的服务状态为 服务中
	 */
	public synchronized void serveCustomerLine(){
		assert !servingCustomerLine : "already serving :" + this;
		servingCustomerLine = true;
		notifyAll();
	}
	
	public String toString(){
		return "Teller" + id + " ";
	}
	
	public String shortString(){
		return "T(出纳员:)" + id;
	}
	
	
	//used by priority queue 此处使用优先级队列
	//比较出纳员服务过的数量，是的优先级队列可以把工作量最小的出纳员推向顾客
	@Override
	public synchronized int compareTo(Teller other) {
		 return customersServed < other.customersServed ? -1 : (customersServed == other.customersServed ? 0 : 1);
	}

}

/**
 * 柜员管理 柜员的工厂类 此类创建一个柜员 并让一个柜员去开始执行服务任务
 * @author Administrator
 *
 */
class TellerManager implements Runnable {
	private ExecutorService exec;
	private CustomerLine customers;
	private PriorityQueue<Teller> workingTellers = new PriorityQueue<Teller>();//优先级队列 工作中的柜员
	private Queue<Teller> tellerDoingOTherThings = new LinkedList<Teller>();//柜员暂停服务列表
	private int adjustmentPeriod; //调整期间
	private static Random rand = new Random(47);
	public TellerManager(ExecutorService e,CustomerLine customers,int adjustmengPeriod){
		exec = e;
		this.customers = customers;
		this.adjustmentPeriod = adjustmengPeriod;
		//start with a single teller;
		Teller teller = new Teller(customers);
		exec.execute(teller);//开始一个柜员 让柜员开始服务
		workingTellers.add(teller);//添加到优先级队列
	}
	/**
	 * 柜员的数量控制 ，如果顾客数量少，相应的减少柜员的数量 ，如果顾客数量多，则添加相应的柜员
	 */
	
	public void adjustTellerNumber(){
		if(customers.size() / workingTellers.size() > 2){
			//If tellers are on break or doing another job,bring one back
			//如果有柜员在休息,则从暂停队列移除一个柜员。
			if(tellerDoingOTherThings.size() > 0){
				Teller teller = tellerDoingOTherThings.remove();
				teller.serveCustomerLine();//去干活
				workingTellers.offer(teller);//将制定的 元素插入此优先级队列
				return;
			}
			//Else create (hire) a new teller;
			//否则创建一个(雇佣)新的柜员 并加入到柜员数组中
			Teller teller = new Teller(customers);
			exec.execute(teller);
			workingTellers.add(teller);
			return;
		}
		//If line is short enough,remove a teller;
		//如果顾客不多，则移除一个柜员，放置在闲置的队列中
		if(workingTellers.size() > 1 && customers.size() / workingTellers.size() < 2){
			while(workingTellers.size() > 1){
				System.out.print("客户当前数量 :" + customers.size() + " ");
				System.out.print("柜员当前数量 :" + workingTellers.size() + " ");
				ressignOneTeller();
			}
		}
	}
	
	//Give a teller a different job or a break;
	//移除woringTellers的柜员，并修改柜员的服务状态
	public void ressignOneTeller(){
		Teller teller = workingTellers.poll();//获取并移除此队列的头，如果此队列为空，则返回 null。
		teller.doSomethingElse();//修改柜员服务状态
		tellerDoingOTherThings.offer(teller);//将指定元素添加到此列表的末尾（最后一个元素）。
		}
	
	/**
	 * 调整柜员数量的执行任务
	 */
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
				adjustTellerNumber();
				System.out.println(customers + " {");
				for(Teller teller : workingTellers){
					System.out.print(teller.shortString() + " ");
				}
				System.out.println(" } ");
			}
		} catch (InterruptedException e) {
			System.out.println(this + "interrupted");
		}
		System.out.println(this + "terminating");
	}
	@Override
	public String toString() {
		return "TellerManager ";
	}
	
}

public class BankTellerSimulation {
	static final int MAX_LINE_SIZE = 50;
	//指定调整期间
	static final int ADJUSTMENT_PERIOD = 1000;
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException, IOException {
		ExecutorService exec = Executors.newCachedThreadPool();
		//If line too long,customer will leave
		//如果排队的队伍太长，客户会离开
		CustomerLine customers = new CustomerLine(MAX_LINE_SIZE);//指定客户数量
		exec.execute(new CustomerGenerator(customers));//执行生成客户的任务
		//Manager will add and remove tellers as necsssary;
		//管理类任务会根据客户数量添加柜员或移除柜员
		exec.execute(new TellerManager(exec, customers, ADJUSTMENT_PERIOD));
		if(args.length > 0){
			TimeUnit.SECONDS.sleep(new Integer(args[0]));
		}else{
			System.out.println("Press 'Enter' to quit");
			System.in.read();
		}
		exec.shutdownNow();
	}
}

