package Concurrent;

public class BasicThread {
	public static void main(String[] args) {
		Thread t = new Thread(new RunnableTest());
		t.start();//����RunnableTest�е�run���� ��������main���� ���߳�ͬʱ/ִ��
		System.out.println("Wating for RunnableTest!");
	}
}
