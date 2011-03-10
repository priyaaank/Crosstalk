package com.barefoot.crosstalk.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class UtilsTest {
	
	@Test
	public void testSnakeCaseToCamelCaseConversion() {
		Assert.assertEquals("setSomeWierdName", Utils.setterNameFor("some_wierd_name"));
		Assert.assertEquals("setWierd", Utils.setterNameFor("wierd"));
		Assert.assertNull(Utils.setterNameFor(null));
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
