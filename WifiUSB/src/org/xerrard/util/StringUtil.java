package org.xerrard.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 字符串工具
 * @author yinshengge
 *
 */
public class StringUtil {
	

	private static final String NUMBER_REG = "\\d+";
	
	public static boolean isNumber(String s) {
		boolean ret = false;
		
		if (!isNullOrEmpty(s)) {
			
			ret = s.matches(NUMBER_REG);
		}
		
		return ret; 
	}
	
	public static boolean isUserId(String s) {
		return isNumber(s) 
			   && s.length() == 13;
	}
	
	public static boolean isNotNullAndEmpy(String s) {
		return !isNullOrEmpty(s);
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	
	private static boolean  isNumeric(Object o) {
		return o instanceof Long 
		|| o instanceof Integer 
		|| o instanceof BigInteger
		|| o instanceof Short
		|| o instanceof Double
		|| o instanceof BigDecimal
		|| o instanceof Float;
	}
	
	public static String toSQLString(Object o) {
		
		if (o == null) {
			return "null";
		}
		if (o instanceof String ) {
			return "'" + o.toString() + "'"; 			
		} else if (isNumeric(o)) {
			return o.toString();
		} else if (o instanceof Boolean) {
			return Boolean.TRUE.equals(o)? "1" : "0";
		} else if (o instanceof Timestamp){
			return "'" + DateUtil.toDefaultFmtString((Timestamp)o) + "'"; 			
		} else {
			return "'" +o.toString()+ "'";
		}
	}
	
	public static String StringArraySerialASSingleString(String[] arr, String splitter) {
		StringBuffer ret = new StringBuffer("");
		
		if (arr != null && splitter != null) {
			for (String item : arr) {
				if (item != null) {
					
					if (ret.length() != 0) {
						ret.append(splitter);
					}
					ret.append(item);
				}
			}
		}
		
		return ret.toString();
	}
	
	public static String StringArraySerialASSingleString(Collection<String> l, String splitter) {
		StringBuffer ret = new StringBuffer("");
		
		if (l != null && splitter != null) {
			for (String item : l) {
				if (item != null) {
					
					if (ret.length() != 0) {
						ret.append(splitter);
					}
					ret.append(item);
				}
			}
		}
		
		return ret.toString();
	}
	
	public static String valueToDBExpression(Class<?> clazz, String value)  {
		
		String ret = null;
		String quot = "";
		
		if (String.class.equals(clazz) 
				|| 	Date.class.equals(clazz)) {
			quot = "'";
		}
		ret =  quot + value + quot;
		
		return ret;
	}
	public static String valueToDBExpression(Class<?> clazz, Object value)  {
		String ret = null;
		if (value != null) {
			ret = valueToDBExpression(clazz, value.toString());			
		}
		return ret;
	}
	
	public static String[] stringListToArrary(List<String> l) {
		
		String[] ret = null;
		
		int colLength = l == null? 0 : l.size() ;
		if (colLength > 0) {
			ret = new String[colLength];
			
			for (int i = 0; i < colLength; i++) {
				ret[i] = l.get(i);
			}
		}
		
		return ret;
	}
}
