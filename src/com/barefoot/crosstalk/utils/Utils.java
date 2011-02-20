package com.barefoot.crosstalk.utils;

import java.util.List;

public class Utils {
	
	public static boolean isNotNullAndEmpty(List listOfObjects) {
		return (listOfObjects != null & listOfObjects.size() > 0);
	}
	
	public static String setterNameFor(String attributeName) {
		if (null != attributeName) {
			return "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1, attributeName.length());
		}
		return null;
	}

}
