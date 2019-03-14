package Concurrent_21_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.plaf.synth.SynthSpinnerUI;

class Shop implements Runnable{
	wareHouse goods;
	SongHuo songHuo;
	private int [] shopNum = new int[3];
	
	
	
	public int[] getShopNum() {
		return shopNum;
	}



	public void setShopNum(int[] shopNum) {
		this.shopNum = shopNum;
	}



	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				synchronized (this) {
					while(goods.getNum()>0){
						if(shopNum==null){
							for(int i=0;i<shopNum.length;i++){
								goods.setGoodsDecut();
								shopNum[i]=goods.getNum();
							}
						}else{
							synchronized (songHuo) {
								songHuo.notifyAll();
							}
							this.wait();
						}
						
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Shop interrupted！");
		}
		
	}
	
}

class wareHouse{
	private final String xbox = "xBoxOne";
	private int num;//orderNum
	
	
	public wareHouse() {
		this.num = 10;
	}
	
	public String getXbox() {
		return xbox;
	}
	public int getNum() {
		return num;
	}
	public void setGoodsDecut() {
		synchronized (this) {
			while(num>0){
				num --;
			}
			
		}
	}
	
	
}

class SongHuo implements Runnable{
	Shop shop;

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()){
				while(shop.getShopNum()==null){
					this.wait();
					synchronized (shop) {
						shop.notifyAll();
					}
				}
				for(int i=0; i<shop.getShopNum().length;i++){
					ReciverShop.getHuowu(i);
				}
			}
		} catch (Exception e) {
		}
		
	}
}

class ReciverShop{
	public static void getHuowu(int i){
		System.out.println("拿到货物！");
	}
}

public class Pratice_24 {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0; i<3;i++){
			exec.execute(new SongHuo());
		}
		exec.execute(new Shop());
	}
}
