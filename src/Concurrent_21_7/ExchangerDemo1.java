package Concurrent_21_7;  
import java.util.concurrent.Exchanger;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.*;  
public class ExchangerDemo1	 {  
      
    public static void main(String[] args) {  
        ExecutorService executor = Executors.newCachedThreadPool();  
          
        final Exchanger exchanger = new Exchanger();  
        executor.execute(new Runnable() {  
            String data1 = "克拉克森，小拉里南斯";  
              
  
            @Override  
            public void run() {  
                nbaTrade(data1, exchanger);  
            }  
        });  
          
  
        executor.execute(new Runnable() {  
            String data1 = "格里芬";  
  
            @Override  
            public void run() {  
                nbaTrade(data1, exchanger);  
            }  
        });  
          
        executor.execute(new Runnable() {  
            String data1 = "哈里斯";  
  
            @Override  
            public void run() {  
                nbaTrade(data1, exchanger);  
            }  
        });  
          
        executor.execute(new Runnable() {  
            String data1 = "以赛亚托马斯，弗莱";  
  
            @Override  
            public void run() {  
                nbaTrade(data1, exchanger);  
            }  
        });  
          
        executor.shutdown();  
    }  
  
    private static void nbaTrade(String data1, Exchanger exchanger) {  
        try {  
            System.out.println(Thread.currentThread().getName() + "在交易截止之前把 " + data1 + " 交易出去");  
            Thread.sleep((long) (Math.random() * 1000));  
  
            String data2 = (String) exchanger.exchange(data1);  
            System.out.println(Thread.currentThread().getName() + "交易得到" + data2);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
} 