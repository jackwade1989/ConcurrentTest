package Concurrent;

import java.util.concurrent.Callable;

public class CallabelTest implements Callable<String>{
	
	private int id;
	
	public CallabelTest(int id) {
		this.id = id;
	}

	//��call�����л�ȡ��������
	@Override
	public String call() throws Exception {
		return "The Callable return :" +id;
	}
	
}
 
