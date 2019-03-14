import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSplit {
	public static void main(String[] args) {
		String a = "a,b,,c,,";
		String [] b = a.split(",");
		System.out.println("b.length:"+b.length);
		for(String c:b){
			System.out.println(c);
		}
		
		List<String> list = new ArrayList<String>();
		list.add("2");
		list.add("1");
		list.add("3");
		for(int i=0;i<list.size()-1;i++){
			if("3".equals(list.get(i))){
				list.remove(i);
			}
			System.out.println("list:"+list.get(i));
		}
	}
}
