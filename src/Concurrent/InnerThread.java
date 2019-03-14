package Concurrent;

import java.util.concurrent.TimeUnit;

class InnerThread {
	private int countDown =5;
	private Inner inner;
	private class Inner extends Thread{
		public Inner(String name) {
			super(name);
			start();
		}
		
		public void run(){
			try {
				while(true){
					System.out.println(this);
					if(--countDown==0){
						return;
					}
					sleep(10);
				}
				
			} catch (InterruptedException e) {
				System.out.println("interupted");
			}
		}

		@Override
		public String toString() {
			return getName()+":"+countDown;
		}
	}
	
   public InnerThread(String name){
		inner = new Inner(name);
	}
}
 
 class InnerThread2{
	   private int countDown =5;
	   private Thread t;
	   public InnerThread2(String name){
		   t = new Thread(name){
			 public void run(){
				 try {
					 while(true){
						 System.out.println(this);
						 if(--countDown==0){
							 return;
						 }
						 sleep(10);
					 }
				} catch (InterruptedException e) {
					System.out.println("sleep() interrupted!");
				}
			 }  
			 	public String toString(){
					return getName()+":"+countDown;
				} 
		   };
		   t.start();
	   }
 }
 
 class InnerRunnable1{
	 private int countDown =5;
	 private Inner inner;
	 
	 private class Inner implements Runnable{
		 Thread t;
		 Inner() {
			t = new Thread();
			t.start();
			
		}
		@Override
		public void run() {
			try {
				while(true){
					System.out.println(this);
					 if(--countDown==0){
						 return;
					 }
					 TimeUnit.MILLISECONDS.sleep(10);
				}
				
			} catch (InterruptedException e) {
				System.out.println("sleep（） Interruped");
			}
		}
		public String toString(){
			return t.getName()+":"+countDown;
		}
	 }
	 
	 public InnerRunnable1(String name){
		 inner = new Inner();
	 }
 }
 
 class InnerRunnable2{
	 private int countDown =5;
	 private Thread t;
	 
	 InnerRunnable2(String name){
		 t= new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(true){
						System.out.println(this);
						 if(--countDown==0){
							 return;
						 }
						 TimeUnit.MILLISECONDS.sleep(10);
					}
					
				} catch (InterruptedException e) {
					System.out.println("sleep（） Interruped");
				}
				
				}
			public String toString(){
				return Thread.currentThread().getName()+":"+countDown;
			}
			
		 	},name);
		 t.start();{
		 };
	 }
 }
 
 class ThreadMethod{
	 private int countDown =5;
	 private Thread t;
	 private String name;
	 
	 public ThreadMethod(String name){
		 this.name = name;
	 }
	 public void runTask(){
		 if(t==null){
			 t=new Thread(name){
				 public void run(){
					 try {
						
						while(true){
							System.out.println(this);
							 if(--countDown==0){
								 return;
							 }
							 sleep(10);
						}
					} catch (InterruptedException e) {
						System.out.println("sleep（） Interruped");
					}
				 }
				 public String toString(){
						return getName()+":"+countDown;
					}
			 };
			 t.start();
		 }
	 }
 }
 
 
