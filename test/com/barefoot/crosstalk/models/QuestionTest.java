package com.barefoot.crosstalk.models;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;

import com.barefoot.crosstalk.components.persistence.PersistableObject.PersistableObjectCursor;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowSQLiteCursor;

@RunWith(RobolectricTestRunner.class)
public class QuestionTest {

	private Question testQuestion;
	private Application testApplication;
	private Context applicationContext;
	
	@Before
	public void setup() {
		testApplication = Robolectric.application;
		applicationContext = testApplication.getApplicationContext();
		testQuestion = new Question(applicationContext);
	}
	
	@Test
	public void testQuestionGettersAndSetters() {
		testQuestion.setAskedDate("some random date");
		testQuestion.setId(12);
		testQuestion.setQuestionText("This is a sample question that I want to ask!!");
		testQuestion.setQuestionTitle("This is a test title for the question");
		assertEquals("some random date", testQuestion.getAskedDate());
		assertEquals(12, testQuestion.getId());
		assertEquals("This is a sample question that I want to ask!!", testQuestion.getQuestionText());
		assertEquals("This is a test title for the question", testQuestion.getQuestionTitle());
		
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testThatQuestionCursorFactoryIsNotNull() {
		assertNotNull(testQuestion.getCursorFactory());
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testThatQuestionCursorIsNotNull() {
		testQuestion.setAskedDate("some random date");
		testQuestion.setId(12);
		testQuestion.setQuestionText("This is a sample question that I want to ask!!");
		testQuestion.setQuestionTitle("This is a test title for the question");
		
		SQLiteCursor newCursor = (SQLiteCursor) testQuestion.getCursorFactory().newCursor(null, null, null, null);
		ShadowSQLiteCursor shadowCursor = (ShadowSQLiteCursor) Robolectric.shadowOf(newCursor);
		ResultSet mockResultSet = mock(ResultSet.class);
		ResultSetMetaData mockMetaResultSet = mock(ResultSetMetaData.class);
		try {
			when(mockMetaResultSet.getColumnCount()).thenReturn(4);
			when(mockMetaResultSet.getColumnName(0)).thenReturn("id");
			when(mockMetaResultSet.getColumnName(1)).thenReturn("question_text");
			when(mockMetaResultSet.getColumnName(2)).thenReturn("question_title");
			when(mockMetaResultSet.getColumnName(3)).thenReturn("asked_date");
			when(mockResultSet.getMetaData()).thenReturn(mockMetaResultSet);
		} catch (SQLException e) {
			fail();
		}

		shadowCursor.setResultSet(mockResultSet);
		Question modelObject = (Question)((PersistableObjectCursor)newCursor).getModelObject(testQuestion, null, null);
		
		assertNotNull(modelObject);
	}
	
}
