package Concurrent_21_3_6;

public class Test15 {
	
	private TestObject_15 object = new TestObject_15();
	private int i = 0;
	
	public void a(){
		synchronized (object) {
			object.setA("a");
			System.out.println("a:"+i++);
			System.out.println("a is :"+object.getA());
		}
	};
	
	public void b(){
		synchronized (object) {
			object.setA("b");
			System.out.println("b:"+i++);
			System.out.println("b is :"+object.getA());
		}	
	};
	
	public void c(){
		synchronized (object) {
			System.out.println("c:"+i++);
			object.setA("c");
			System.out.println("c is :"+object.getA());
		}
	};
	
	public static void main(String[] args) {
		final Test15 t = new Test15();
		new Thread(){
			public void run(){
				t.a();
				t.b();
				t.c();
			};
		}.start();
		t.a();
		t.b();
		t.c();
	}
}
