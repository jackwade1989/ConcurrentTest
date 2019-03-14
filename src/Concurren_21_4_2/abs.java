package Concurren_21_4_2;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class abs extends TestAbstract{

	@Override
	public void test1() {
		System.out.println("jackwade");
	}

	@Override
	public void test2() {
		System.out.println("test2");
		test();
	}
	
	public static void main(String[] args) {
		abs a = new abs();
		a.test2();
		Hashtable<String, String> tabMap = new Hashtable<String,String>();
		tabMap.put(null, null);
		HashMap<String, String> map = new HashMap<String,String>();
		map.put(null, null);
		for(Map.Entry<String,String> m:map.entrySet()){
			System.out.println(m.getKey()+":"+m.getValue());
			System.out.println(m);
		}
		
		Iterator<Entry<String, String>>it = tabMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,String> tbmap = it.next();
			System.out.println(tbmap);
		}
		
	}


}
