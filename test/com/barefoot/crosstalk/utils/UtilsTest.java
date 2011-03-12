package com.barefoot.crosstalk.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class UtilsTest {
	
	@Test
	public void testEmptinessOfAList() {
		List<String> testList = null;
		Assert.assertFalse(Utils.isNotNullAndEmpty(testList));
		testList = new ArrayList<String>();
		Assert.assertFalse(Utils.isNotNullAndEmpty(testList));
		testList.add("Something");
		Assert.assertTrue(Utils.isNotNullAndEmpty(testList));
	}
	
	@Test
	public void testSnakeCaseToCamelCaseConversion() {
		Assert.assertEquals("SomeWierdName", Utils.snakeCaseToCamelCase("some_wierd_name"));
		Assert.assertEquals("Wierd", Utils.snakeCaseToCamelCase("wierd"));
		Assert.assertNull(Utils.snakeCaseToCamelCase(null));
	}
	
	@Test
	public void testCamelCaseToSnakeCaseConversion() {
		Assert.assertEquals("some_wierd_name", Utils.camelCaseToSnakeCase("someWierdName"));
		Assert.assertEquals("wierd", Utils.camelCaseToSnakeCase("wierd"));
		Assert.assertNull(Utils.camelCaseToSnakeCase(null));
	}
	
	@Test
	public void testSetterGenerationForSnakeCaseField() {
		Assert.assertEquals("setSomeWierdName", Utils.setterNameFor("some_wierd_name"));
		Assert.assertEquals("setWierd", Utils.setterNameFor("wierd"));
		Assert.assertNull(Utils.setterNameFor(null));
	}

	@Test
	public void testGetterGenerationForSnakeCaseField() {
		Assert.assertEquals("getSomeWierdName", Utils.getterNameFor("some_wierd_name"));
		Assert.assertEquals("getWierd", Utils.getterNameFor("wierd"));
		Assert.assertNull(Utils.setterNameFor(null));
	}
	
	@Test
	public void testBasicPluralization() {
		Assert.assertEquals("wierds", Utils.basicPluralize("wierd"));
		Assert.assertNull(Utils.basicPluralize(null));
	}
	
	@Test
	public void testConversionOfListToCommaSeparatedString() {
		List<String> listOfNames = new ArrayList<String>();
		listOfNames.add("Jhon");
		listOfNames.add("Mark dwarbey");
		listOfNames.add("something excellent");
		listOfNames.add("random crap");
		listOfNames.add("whatever");
		String expectedNameStringWithQuotes = "'Jhon', 'Mark dwarbey', 'something excellent', 'random crap', 'whatever'";
		String expectedNameStringWithoutQuotes = "Jhon, Mark dwarbey, something excellent, random crap, whatever";
		Assert.assertEquals(expectedNameStringWithQuotes, Utils.getCommaSeparatedStringForAllElements(listOfNames , true));
		Assert.assertEquals(expectedNameStringWithoutQuotes, Utils.getCommaSeparatedStringForAllElements(listOfNames , false));
	}
}
