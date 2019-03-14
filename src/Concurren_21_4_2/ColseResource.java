package Concurren_21_4_2;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 在阻塞时关闭底层资源
 * @author Administrator
 *
 */
public class ColseResource {
	public static void main(String[] args) throws Exception{
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8080);
		InputStream socketInput = new Socket("localhost",8080).getInputStream();
		exec.execute(new IOBlocked(socketInput));
		exec.equals(new IOBlocked(System.in));
		TimeUnit.MICROSECONDS.sleep(100);
		System.out.println("Shunting dwon all threads");
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing "+socketInput.getClass().getName());
		socketInput.close();//关闭底层资源
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing "+System.in.getClass().getName());
		System.in.close();//关闭底层资源
		
	}
}
