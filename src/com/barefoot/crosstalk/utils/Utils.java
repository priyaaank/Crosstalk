package com.barefoot.crosstalk.utils;

import java.util.List;

public class Utils {
	
	public static boolean isNotNullAndEmpty(List listOfObjects) {
		return (listOfObjects != null & listOfObjects.size() > 0);
	}

}
