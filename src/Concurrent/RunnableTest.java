package Concurrent;
/**
 * ʵ��runnable��ȡ�߳�
 * @author Administrator
 *
 */
public class RunnableTest implements Runnable{
	
	protected static int taskCount;
	private final int id = taskCount++;
	 
	public RunnableTest() {
		System.out.println("Runnalble start id = "+ id);
	}
	
	public void run() {
		System.out.println("task 1 id="+id);
		Thread.yield();//yeild()֪ͨ�����߳̿�ʼִ��
		System.out.println("task 2 id="+id);
		Thread.yield();
		System.out.println("task 3 id="+id);
		Thread.yield();
		System.out.println("print end , id = "+id);
	}
	
}




