package com.barefoot.crosstalk.utils;

import java.util.List;

public class Utils {
	
	public static boolean isNotNullAndEmpty(List<?> listOfObjects) {
		
		return (listOfObjects != null && listOfObjects.size() > 0);
	}
	
	public static String basicPluralize(String singularWord) {
		return singularWord == null ? null : (singularWord.trim() + "s");
	}
	
	public static String setterNameFor(String attributeName) {
		StringBuffer setterName = new StringBuffer("set");
		if (null != attributeName) {
			return setterName.append(snakeCaseToCamelCase(attributeName)).toString();
		}
		return null;
	}
	
	public static String getterNameFor(String attributeName) {
		StringBuffer setterName = new StringBuffer("get");
		if (null != attributeName) {
			return setterName.append(snakeCaseToCamelCase(attributeName)).toString();
		}
		return null;
	}
	
	public static String snakeCaseToCamelCase(String word) {
		if (word == null)
			return null;
		
		StringBuffer camelCaseWord = new StringBuffer("");
		String[] splittedName = word.split("_");
		for(String eachWord : splittedName) {
			camelCaseWord.append(capitalizeFirstLetter(eachWord));
		}
		
		return camelCaseWord.toString();
	}
	
	public static String camelCaseToSnakeCase(String word) {
		if (word == null)
			return null;
		
		StringBuffer snakeCaseWord = new StringBuffer("");
		String currentChar = null;
		for(int i=1; i <= word.length(); i++) {
			currentChar = word.substring(i-1, i);
			if(currentChar.equals(currentChar.toUpperCase())) {
				snakeCaseWord.append("_");
			}
			snakeCaseWord.append(currentChar.toLowerCase());
		}
		return snakeCaseWord.toString();
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
