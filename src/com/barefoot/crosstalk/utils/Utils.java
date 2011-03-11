package com.barefoot.crosstalk.utils;

import java.util.List;

public class Utils {
	
	public static boolean isNotNullAndEmpty(List<?> listOfObjects) {
		return (listOfObjects != null & listOfObjects.size() > 0);
	}
	
	public static String basicPluralize(String singularWord) {
		return singularWord == null ? null : (singularWord.trim() + "s");
	}
	
	public static String setterNameFor(String attributeName) {
		String[] splittedName = null;
		StringBuffer setterName = new StringBuffer("set");
		if (null != attributeName) {
			splittedName = attributeName.split("_");
			for(String eachWord : splittedName) {
				setterName.append(capitalizeFirstLetter(eachWord));
			}
			
 			return setterName.toString();
		}
		return null;
	}

	public static String getCommaSeparatedStringForAllElements(List<String> list, boolean includeQuotes) {
		String commaSymbol =  includeQuotes ? "'" : "";
		if(list != null && list.size() > 0) {
			StringBuffer commaSeparatedList = new StringBuffer();
			for(String eachElement : list) {
				commaSeparatedList.append(commaSymbol)
								  .append(eachElement)
								  .append(commaSymbol)
								  .append(", ");
			}
			return commaSeparatedList.substring(0,commaSeparatedList.length() - 2);
		}
		
		return "";
	}
	
	private static String capitalizeFirstLetter(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1, word.length());
	}
}
