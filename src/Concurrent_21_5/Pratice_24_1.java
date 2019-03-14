package Concurrent_21_5;

class Product{
	private String productName;
	private Integer [] num = new Integer [3];

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer[] getNum() {
		return num;
	}

	public void setNum(Integer[] num) {
		this.num = num;
	}

	
}

class Producer implements Runnable{
	private Pratice_24_1 t;
	
	public Producer(Pratice_24_1 pa) {
		this.t = pa;
	}

	@Override
	public void run() {
		int successNum = 0;
		int failNum = 0;
		boolean flag = false;
		try {
			while(!Thread.interrupted()){
				synchronized (this) {
					while(!flag){
						for(int i=0;i<t.product.getNum().length;i++){
							if(null==t.product.getNum()[i]){
								++failNum ;
							}else{
								++successNum ;
								t.product.getNum()[i] = ++successNum;
							}
						}
						if(successNum == 3){
							flag = true;
							wait();
						}else{
							flag = false;
						}
					}
				}
				
				synchronized (t.consumer) {
					if(successNum == 3){
						notifyAll();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Producer interrupted!");
		}
	}
	
}

class Consumer implements Runnable{
	Pratice_24_1 t;
	
	public Consumer(Pratice_24_1 t) {
		this.t = t;
	}

	@Override
	public void run() {
		
	}
}

public class Pratice_24_1 {
	Product product = new Product();
	Producer producer = new Producer(this);
	Consumer consumer = new Consumer(this);
	public static void main(String[] args) {
		Integer [] nums = new Integer [3];
		for(int i=0;i<nums.length;i++){
			if(null!=nums[i]){
				System.out.println("ok");
				System.out.println(nums[i]);
			}
			System.out.println();
		}
	}
}



