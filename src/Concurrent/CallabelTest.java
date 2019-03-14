package Concurrent;

import java.util.concurrent.Callable;

public class CallabelTest implements Callable<String>{
	
	private int id;
	
	public CallabelTest(int id) {
		this.id = id;
	}

	//从call方法中获取参数返回
	@Override
	public String call() throws Exception {
		return "The Callable return :" +id;
	}
	
}
 
