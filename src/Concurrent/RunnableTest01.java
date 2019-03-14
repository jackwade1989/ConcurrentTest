package Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableTest01 {
	public static void main(String[] args) {
		for(int i=0; i<5;i++){
			new Thread(new RunnableTest()).start();
		}
		testExecuter();
		testFixedThreadPool();
	}
	
	/**
	 * //�̹߳������ Ϊÿ�����񴴽�һ���߳�(�Զ�����) ������Ҫ��ʾ��ȥ������
	 */
	public static void testExecuter(){
		ExecutorService exec = Executors.newCachedThreadPool();
		//��ʼִ��
		for(int i=0;i<5;i++){
			exec.execute(new RunnableTest());
		}
		exec.shutdown();//shutdown() ��ֹ�µ������ύ��Executer
	}
	
	/**
	 * newFixedThreadPool �����̵߳��������̹߳�����
	 */
	public static void testFixedThreadPool(){
		ExecutorService exec = Executors.newFixedThreadPool(5);
		//��ʼִ��
		for(int i=0;i<5;i++){
			exec.execute(new RunnableTest());
		}
		exec.shutdown();
	}
	
	/**
	 * newSingleThreadExecutor ��һ�̵߳Ĺ����� ���ڶ���һЩ���ڴ�������
	 * �����newSingleThreadExecutor�ύ������� ��ô��Щ�����Ŷӡ�
	 */
	public static void singleThreadPool(){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		//��ʼִ��
		for(int i=0;i<5;i++){
			exec.execute(new RunnableTest());
		}
		exec.shutdown();
	}
}
