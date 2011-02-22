package com.barefoot.crosstalk.models;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class CriteriaTest {
	
	private Criteria<Question> testCriteria;
	private Question testQuestionObject;
	
	@Before
	public void setup() {
		testQuestionObject = new Question();
		testCriteria = new Criteria<Question>(testQuestionObject);
	}
	
	@Test
	public void testGenerationOfSimpleSelectQuery() {
		final String SELECTION_QUERY = "select id, questiontext, questiontitle, askeddate from question where " +
									   "id = 1 and questiontitle = 'random question title' and "+
									   "askeddate in ('01/01/2010','02/02/2010','03/03/2010') "+
									   "order by questiontext, questiontitle asc askeddate desc";
		testCriteria.select("id","questiontext","questiontitle","askeddate")
					.where("id", "1")
					.and("questiontitle", "random question title")
					.and("askeddate", "01/01/2010", "02/02/2010", "03/03/2010")
					.orderByAscending("questiontext","questiontitle")
					.orderByDescending("askeddate");
		Assert.assertEquals(SELECTION_QUERY, testCriteria.selectionQuery());
	}

}
