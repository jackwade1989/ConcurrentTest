package Concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * CallabelTest ʵ����Callable�ӿ� ������ȡ����ֵ
 * @author Administrator
 *
 */
public class CallableTest {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<String>> results = new ArrayList<Future<String>>();
		for(int i=0;i<10;i++){
			//submit()���������futune��������Callable���ؽ�����ض����ͽ����˲�����
			results.add(exec.submit(new CallabelTest(i)));
		}
		for(Future<String> fs:results){
			try {
				//isDone()��ѯ�����Ƿ����
				if(fs.isDone()){
					System.out.println(fs.get());
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				exec.shutdown();
			}
		}
	}
}
