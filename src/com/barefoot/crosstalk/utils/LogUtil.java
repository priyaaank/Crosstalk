package com.barefoot.crosstalk.utils;

public class LogUtil {
	
	public static String logTagForMe() {
		if(Thread.currentThread().getStackTrace().length > 2){
			return Thread.currentThread().getStackTrace()[2].getClassName();
		}
		return "CROSSTALK";
	}
}
