package com.barefoot.crosstalk.models;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.barefoot.crosstalk.components.persistence.Criteria;

public class CriteriaTest {
	
	private Criteria<Question> testCriteria;
	private Question testQuestionObject;
	
	@Before
	public void setup() {
		testQuestionObject = new Question(null);
		testCriteria = new Criteria<Question>(testQuestionObject);
	}
	
	@Test
	public void testGenerationOfSimpleSelectQuery() {
		final StringBuffer SELECTION_QUERY = new StringBuffer();
		SELECTION_QUERY.append("select id, questiontext, questiontitle, askeddate from question t0 where ")
					   .append("id = '1' and ")
					   .append("askeddate in ('01/01/2010', '02/02/2010', '03/03/2010') and ")
					   .append("questiontitle = 'random question title' ")
					   .append("order by questiontext, questiontitle asc, askeddate desc ");
		
		testCriteria.select("id","questiontext","questiontitle","askeddate")
					.where("id", "1")
					.and("questiontitle", "random question title")
					.and("askeddate", "01/01/2010", "02/02/2010", "03/03/2010")
					.orderByAscending("questiontext","questiontitle")
					.orderByDescending("askeddate");
		
		Assert.assertEquals(SELECTION_QUERY.toString().toLowerCase(), testCriteria.selectionQuery().toLowerCase());
	}
	
	@Test
	public void testGenerationOfBasicSelectQuery() {
		final StringBuffer SELECTION_QUERY = new StringBuffer();
		SELECTION_QUERY.append("select *  from question t0 where ")
					   .append("id = '1'  ");
		
		testCriteria.where("id", "1");
		
		Assert.assertEquals(SELECTION_QUERY.toString().toLowerCase(), testCriteria.selectionQuery().toLowerCase());
	}
	
	@Test
	public void testGenerationOfSelectQueryWithParamsArray() {
		final StringBuffer SELECTION_QUERY = new StringBuffer();
		SELECTION_QUERY.append("where ")
					   .append("id = ? and ")
					   .append("askeddate in (?,?,?) and ")
					   .append("questiontitle = ?");
		String[] params = new String[] {"1","01/01/2010","02/02/2010","03/03/2010",
										"random question title"};
		
		testCriteria.select("id","questiontext","questiontitle","askeddate")
					.where("id", "1")
					.and("questiontitle", "random question title")
					.and("askeddate", "01/01/2010", "02/02/2010", "03/03/2010")
					.orderByAscending("questiontext","questiontitle")
					.orderByDescending("askeddate");
		
		Assert.assertEquals(SELECTION_QUERY.toString(), testCriteria.escapedSelectionQuery());
		
		Assert.assertEquals(params[0], testCriteria.selectionQueryParamsArray()[0]);
		Assert.assertEquals(params[1], testCriteria.selectionQueryParamsArray()[1]);
		Assert.assertEquals(params[2], testCriteria.selectionQueryParamsArray()[2]);
		Assert.assertEquals(params[3], testCriteria.selectionQueryParamsArray()[3]);		
		Assert.assertEquals(params[4], testCriteria.selectionQueryParamsArray()[4]);
	}

}
