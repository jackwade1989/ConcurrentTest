package Concurren_21_4_2;

import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
	
	 public static boolean belongCalendar(Date time, Date from, Date to) {
	        Calendar date = Calendar.getInstance();
	        date.setTime(time);

	        Calendar after = Calendar.getInstance();
	        after.setTime(from);

	        Calendar before = Calendar.getInstance();
	        before.setTime(to);

	        if (date.after(after) && date.before(before)) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	 
	  public static Date stringToDate(String str) throws ParseException{
      	SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
      	Date date = ft.parse(str);
      	return date;
      }
	 
	 public static void main(String[] args) throws ParseException {
		//打印测试
	        Date time1  = stringToDate("11/03/2017");
	        Date time2 = stringToDate("15/03/2017");
	        Date time3 = stringToDate("17/03/2017");
	        Date from = stringToDate("12/03/2017");
	        Date to= stringToDate("16/03/2017");
	        System.out.println(belongCalendar(time1,from,to));
	        System.out.println(belongCalendar(time2,from,to));
	        System.out.println(belongCalendar(time3,from,to)); 
	        
	        System.out.println("covertDate:"+stringToDate("03/10/2017"));
	        
	}
	        
}
