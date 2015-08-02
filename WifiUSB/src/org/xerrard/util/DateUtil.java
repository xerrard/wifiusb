package org.xerrard.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期工具
 * @author yinshengge
 *
 */
public class DateUtil {
	
	@SuppressWarnings("deprecation")
	public static Timestamp getMaxDate() {
		Timestamp now = getNow();
		now.setYear(now.getYear() + 100);
		return now;
	}
	
	public static Timestamp  getNow() {
		Timestamp ret = null;
		
		Calendar c  = Calendar.getInstance();
		ret = new Timestamp( c.getTime().getTime() );
		
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	public static Timestamp getZeroDateTime() {
		return new Timestamp(0, 0 , 1, 0, 0, 0, 0); 
	}
	@SuppressWarnings("deprecation")
	public static String toDefaultFmtString(Timestamp ts) {

		String ret = "";
		
		if (ts != null) {
			ret = String.format("%d-%d-%d %d:%d:%d"
					, ts.getYear() + 1900
					, ts.getMonth() + 1
					, ts.getDate()
					, ts.getHours()
					, ts.getMinutes()
					, ts.getSeconds());
			
		}
		
		return ret;
	}
	
	   public static String toCompleteFmtString(Timestamp ts) {


	        String ret = "";
	        
	        if (ts != null) {
	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	            ret = sdf.format(ts);
	        }
	        
	        return ret;
	    
	    }
}
