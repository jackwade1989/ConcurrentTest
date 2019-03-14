package Concurrent_21_7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Pool<T> {
	private int size;
	private List<T> items = new ArrayList<T>();
	private volatile boolean [] checkedOut;
	private Semaphore available;
	public Pool(Class<T> classObject,int size) {
		this.size = size;
		checkedOut = new boolean[size];
		available = new Semaphore(size,true);//指定许可的数量
		//Load pool with object that can be checked out
		//读取池中可以被签出的
		for(int i = 0;i < size ; ++i){
			try {
				//Assumes a default constructor
				items.add(classObject.newInstance());
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
	}
	public T checkOut() throws InterruptedException{
		available.acquire();//获取许可 并在许可的数量上减相应的数量
		return getItem();
	}
	
	public void checkIn (T x){
		if(releaseItem(x)){
			available.release();
		}
	}
	
	private synchronized T getItem(){
		for(int i=0;i<size;++i){
			if(!checkedOut[i]){
				checkedOut[i] = true;
				return items.get(i);
			}
		}
		return null;
	}
	
	
	private synchronized boolean releaseItem(T item){
		int index = items.indexOf(item);
		if(index == -1){
			return false;
		}
		if(checkedOut[index]){
			checkedOut[index] = false;
			return true;
		}
		return false;
	}
}


