package Concurrent_21_3;

public class Controller_Practice extends Practice_1{
	
	public void init(String str1,String str2,int id){
		 
		System.out.println("Im'init method");
		System.out.println("who coming here: " + id);
		setPractice_1(str1,str2,id);
		
	};
	
	private void setPractice_1(String str1,String str2,int id){
		setPractice_2(str1,str2,id);
	}
	
	private void setPractice_2(String str1,String str2,int id){
		setTitle(str1);
		setAnswer(str2);
		System.out.println("id is "+id+" :method_3 now i will modify this data!");
		System.out.println("now id is"+id+" :answers data is :"+ getAnswer());
		System.out.println("now id is"+id+" :title data is :"+ getTitle());
		System.out.println("im done~! id = "+id);
	}
	
	public static void main(String[] args) {
		ThreadPractice.test(new Controller_Practice());
	}

}
