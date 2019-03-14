package Concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * 
 * 创建显示地后台线程 工厂模式
 * @author Administrator
 *
 */
public class DaemonThreadFactory implements ThreadFactory{
	public Thread newThread(Runnable r){
		Thread thread = new Thread(r);
		thread.setDaemon(true);
		return thread;
	}

}
