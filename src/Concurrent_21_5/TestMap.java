package Concurrent_21_5;

import java.util.HashMap;
import java.util.Map;

public class TestMap {
	public static void main(String[] args) {
		boolean b = false;
		StringBuffer data = new StringBuffer();
		String ac [] ;
		Map<String, String>map = new HashMap<String, String>();
		map.put("belong", "5G");
		map.put("sta", "3F");
		for(Map.Entry<String, String> m:map.entrySet()){
			System.out.println(m.getKey()+":"+m.getValue()+",");
			data.append( m.getKey()+":"+m.getValue()+",");
			b = true;
		}
		System.out.println("b:"+b);
		ac = data.toString().split(",");
		for(int i=0;i<ac.length;i++){
			System.out.println("ac="+ac[i]);
		}
	}
}
