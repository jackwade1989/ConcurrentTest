package Concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * CallabelTest 实现了Callable接口 用来获取返回值
 * @author Administrator
 *
 */
public class CallableTest {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<String>> results = new ArrayList<Future<String>>();
		for(int i=0;i<10;i++){
			//submit()方法会产生futune对象，它用Callable返回结果的特定类型进行了参数化
			results.add(exec.submit(new CallabelTest(i)));
		}
		for(Future<String> fs:results){
			try {
				//isDone()查询任务是否完成
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
