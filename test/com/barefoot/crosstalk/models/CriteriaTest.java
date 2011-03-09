package com.barefoot.crosstalk.models;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.barefoot.crosstalk.models.Criteria;
import com.barefoot.crosstalk.models.Question;

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
		
		Map<String, String[]> selectionQueryWithParams = testCriteria.selectionQueryWithParams();
		Assert.assertEquals(params[0], selectionQueryWithParams.get(SELECTION_QUERY.toString())[0]);
		Assert.assertEquals(params[1], selectionQueryWithParams.get(SELECTION_QUERY.toString())[1]);
		Assert.assertEquals(params[2], selectionQueryWithParams.get(SELECTION_QUERY.toString())[2]);
		Assert.assertEquals(params[3], selectionQueryWithParams.get(SELECTION_QUERY.toString())[3]);		
		Assert.assertEquals(params[4], selectionQueryWithParams.get(SELECTION_QUERY.toString())[4]);
	}

}
