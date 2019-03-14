package Concurren_21_4_2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestMap {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", 1l);
		map.put("b", "1");
		map.put("c", 2);
		map.put("e", 3);
		
		//第一种：普遍使用，二次取值
		for(String key:map.keySet()){
			System.out.println("第一种遍历方式"+key+":"+map.get(key));
		}
		System.out.println("\t");
		
		 //第二种
	      System.out.println("通过Map.entrySet使用iterator遍历key和value：");
		Iterator<Map.Entry<String, Object>> entry = map.entrySet().iterator();
		while(entry.hasNext()){
			Map.Entry<String, Object> obj = entry.next();
			System.out.println("第二种遍历方式"+obj.getKey()+":"+obj.getValue());
		}
		System.out.println("\t");
		
		 //第三种：推荐，尤其是容量大时
	      System.out.println("通过Map.entrySet遍历key和value");
		for(Map.Entry<String, Object> obj :map.entrySet()){
			System.out.println("第三种遍历方式"+obj.getKey()+":"+obj.getValue());
		}
		
		//第四种
	      System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
		System.out.println("\t");
		for(Object val:map.values()){
			System.out.println("第四中校验方式:"+val);
		}
		
	}
}
