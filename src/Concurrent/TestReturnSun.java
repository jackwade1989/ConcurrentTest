package Concurrent;

public class TestReturnSun extends TestReturnFather{
	String name = "sun";
	

	public TestReturnSun() {
		System.out.println("sun");
	}
	
	public String appengString(String a,String b){
		StringBuffer c = new StringBuffer();
		return c.append(a).append(b).toString();
	}
	
	public int muilple(int a , int b){
		int c = 0;
		try {
			c = a/b;
			return c;
		}  catch (ArithmeticException e1) {
			System.out.println("ArithmeticException");
		}catch (Exception e) {
			System.out.println("exception");
		}
		finally {
			System.out.println("finally");
		}
		return 0;
	}
	
	public static void main(String[] args) {
		TestReturnSun t = new TestReturnSun();
		System.out.println(t.muilple(3, 0));
	}
	
}
