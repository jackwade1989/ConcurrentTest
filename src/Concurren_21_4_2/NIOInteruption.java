package Concurren_21_4_2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class NIOBlocked implements Runnable{
	
	private final SocketChannel sc;
	
	public NIOBlocked(SocketChannel sc) {
		this.sc = sc;
	}



	@Override
	public void run() {
		try {
			System.out.println("wating for read() in " +this);
			sc.read(ByteBuffer.allocate(1));
		} catch (ClosedByInterruptException e) {
			System.out.println("ClosedByInterruptException!"+e.getCause());
		}catch (AsynchronousCloseException e) {
			System.out.println("AsynchronousCloseException!"+e.getCause());
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Exiting NIOBlocked.run()" + this);
	}
	
}

public class NIOInteruption {
	public static void main(String[] args) throws Exception{
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8080);
		InetSocketAddress isa = new InetSocketAddress("localhost",8080);
		SocketChannel sc1 = SocketChannel.open(isa);
		SocketChannel sc2 = SocketChannel.open(isa);
		Future<?> f = exec.submit(new NIOBlocked(sc1));
		exec.execute(new NIOBlocked(sc2));
		exec.shutdown();
		TimeUnit.SECONDS.sleep(1);
		//produce an interrupt via cancel;
		f.cancel(true);//中断某个单一的线程
		TimeUnit.SECONDS.sleep(1);
		//Release the block by closing the channel
		sc2.close();//關閉資源以釋放鎖 解除阻塞
	}
}
