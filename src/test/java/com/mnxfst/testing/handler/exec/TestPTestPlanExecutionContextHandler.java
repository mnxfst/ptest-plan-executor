/*
 *  The ptest framework provides you with a performance test utility
 *  Copyright (C) 2012  Christian Kreutzfeldt <mnxfst@googlemail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.mnxfst.testing.handler.exec;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.junit.Assert;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanActivitySettings;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanBuilder;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanConfigurationOption;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;
import com.mnxfst.testing.server.PTestServerResponseBuilder;
import com.mnxfst.testing.server.cfg.PTestServerConfiguration;
import com.mnxfst.testing.server.exception.ContextInitializationFailedException;
import com.mnxfst.testing.server.exception.RequestProcessingFailedException;

/**
 * Test case for {@link PTestPlanExecutionContextHandler}
 * @author mnxfst
 * @since 10.04.2012
 */
public class TestPTestPlanExecutionContextHandler {

	@Test
	public void testInitializeWithNullInput() throws ContextInitializationFailedException {
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.initialize(null);
			Assert.fail("Provided configuration must not be null");
		} catch(ContextInitializationFailedException e) {
			//
		}
	}
	
	@Test
	public void testInitializeWithNullHostname() throws ContextInitializationFailedException {
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.initialize(new PTestServerConfiguration(null, 123, 123));
			Assert.fail("Provided configuration must contain a valid hostname");
		} catch(ContextInitializationFailedException e) {
			//
		}
	}
	
	@Test
	public void testInitializeWithEmptyHostname() throws ContextInitializationFailedException {
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.initialize(new PTestServerConfiguration("", 123, 123));
			Assert.fail("Provided configuration must contain a valid hostname");
		} catch(ContextInitializationFailedException e) {
			//
		}
	}
	
	@Test
	public void testInitializeWithInvalidPort() throws ContextInitializationFailedException {
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.initialize(new PTestServerConfiguration("localhost", 0, 123));
			Assert.fail("Provided configuration must contain a valid port");
		} catch(ContextInitializationFailedException e) {
			//
		}
	}
	
	@Test
	public void testInitializeWithValidHostnameAndPort() throws ContextInitializationFailedException {
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 123));
	}
	
	@Test
	public void testProcessRequestWithInvalidRequest() throws RequestProcessingFailedException {
		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.processRequest(null, new HashMap<String, List<String>>(), false, testEvent);
			Assert.fail("The provided input must contain a valid request");
		} catch(RequestProcessingFailedException e) {
			//
		}
	}

	@Test
	public void testProcessRequestWithNullRequestParameters() throws RequestProcessingFailedException {
		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), null, false, testEvent);
			Assert.fail("The provided input must contain a valid request parameter map");
		} catch(RequestProcessingFailedException e) {
			//
		}		
	}

	@Test
	public void testProcessRequestWithNullMessageEvent() throws RequestProcessingFailedException {

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		try {
			handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), new HashMap<String, List<String>>(), false, null);
			Assert.fail("The provided input must contain a valid request parameter map");
		} catch(RequestProcessingFailedException e) {
			//
		}		
	}

	@Test
	public void testProcessRequestWithMissingNumOfThreads() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.DAYS.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_THREADS_MISSING_OR_INVALID, "Missing or invalid number of threads");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithInvalidNumOfThreads() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "0", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.DAYS.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_THREADS_MISSING_OR_INVALID, "Missing or invalid number of threads");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithMissingNumOfRecurrences() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.DAYS.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_RECURRENCES_MISSING_OR_INVALID, "Missing or invalid number of recurrences");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithInvalidNumOfRecurrences() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "0", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.DAYS.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_RECURRENCES_MISSING_OR_INVALID, "Missing or invalid number of recurrences");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithMissingTypeOfRecurrences() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_RECURRENCE_TYPE_MISSING_OR_INVALID, "Missing or invalid recurrence type");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithInvalidTypeOfRecurrences() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, "unknown-type", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_RECURRENCE_TYPE_MISSING_OR_INVALID, "Missing or invalid recurrence type");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithMissingTestplan() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_MISSING, "Missing required testplan");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithEmptyTestplan() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_MISSING, "Missing required testplan");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithTestplanContainingSpaces() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "            \t\t\t", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		
		Map<String, String> errorMessages = new HashMap<String, String>();
		errorMessages.put(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_MISSING, "Missing required testplan");
		String expectedResponse = PTestServerResponseBuilder.buildErrorResponse("localhost", 8080, errorMessages);
		Assert.assertEquals("The response must be equal to the expected string", expectedResponse.trim(), contents.trim());
	}

	@Test
	public void testProcessRequestWithInvalidTestplan() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "no such testplan", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		Assert.assertTrue("The response must contain the error key " + PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED, contents.indexOf(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED) != -1);
	}

	@Test
	public void testProcessRequestWithTestplanHavingEmptyXML() throws RequestProcessingFailedException, ContextInitializationFailedException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, "<?xml version=\"1.0\" ?>", params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		Assert.assertTrue("The response must contain the error key " + PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED, contents.indexOf(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED) != -1);
	}

	@Test
	public void testProcessRequestWithTestplanHavingEmptyTestplanXMLNode() throws RequestProcessingFailedException, ContextInitializationFailedException, InvalidConfigurationException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		String xml = "<?xml version=\"1.0\" ?><ptestplan/>";
		
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, xml, params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		Assert.assertTrue("The response must contain the error key " + PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED, contents.indexOf(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED) != -1);
	}

	@Test
	public void testProcessRequestWithTestplanHavingInvalidSettings() throws RequestProcessingFailedException, ContextInitializationFailedException, InvalidConfigurationException {

		TestMessageEventChannelFuture testMessageEventChannelFuture = new TestMessageEventChannelFuture();
		TestMessageEventChannel testMessageChannel = new TestMessageEventChannel(testMessageEventChannelFuture);
		TestMessageEvent testEvent = new TestMessageEvent("dummy", testMessageChannel);

		PTestPlan plan = new PTestPlan();
		plan.setCreatedBy("mnxfst");
		plan.setCreationDate(new Date());
		plan.setDescription("test description");		
		plan.setName("my-test-plan");
		
		PTestPlanConfigurationOption options = new PTestPlanConfigurationOption();
		options.setId("id-1");
		options.addOption("test-1", "value-1");
		options.addOption("test-2", "value-2");		
		plan.addGlobalCfgOption(options);
		
		options = new PTestPlanConfigurationOption();
		options.setId("id-2");
		options.addOption("test-3", "value-3");
		options.addOption("test-4", "value-4");		
		plan.addGlobalCfgOption(options);
		
		options = new PTestPlanConfigurationOption();
		options.setId("activity-1");
		options.addOption("set-1", "val-1");
		PTestPlanActivitySettings activityOpt = new PTestPlanActivitySettings();
		activityOpt.addConfigOption("set-1", "val-1");
		activityOpt.setClazz("test-class");
		activityOpt.setDescription("test class description");
		plan.addActivityConfigOption(activityOpt);

		String xml = PTestPlanBuilder.export(plan);
		
		PTestPlanExecutionContextHandler handler = new PTestPlanExecutionContextHandler();
		handler.initialize(new PTestServerConfiguration("localhost", 8080, 1));
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_THREADS_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM, "1", params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM, PTestPlanRecurrenceType.TIMES.toString(), params);
		insertSingleValue(PTestPlanExecutionContextHandler.CONTEXT_HANDLER_TESTPLAN_PARAM, xml, params);
		handler.processRequest(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"), params, false, testEvent);
		
		Assert.assertNotNull("The message contained within the channel future must not be null", testMessageEventChannelFuture.getMessage());
		DefaultHttpResponse response = (DefaultHttpResponse)testMessageEventChannelFuture.getMessage();		
		Assert.assertEquals("The status must be 200", HttpStatus.SC_OK, response.getStatus().getCode());
		
		ChannelBuffer buf = response.getContent();
		Assert.assertNotNull("The buffer must not be null", buf);
		String contents = new String(buf.array());
		Assert.assertNotNull("The contents must not be null", contents);
		Assert.assertFalse("The contents must not be empty", contents.isEmpty());
		Assert.assertTrue("The response must contain the error key " + PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED, contents.indexOf(PTestPlanExecutionContextHandler.ERROR_CODE_TESTPLAN_PARSING_FAILED) != -1);
	}

	/**
	 * Inserts the key/value pair into the provided map 
	 * @param key
	 * @param value
	 * @param params
	 */
	private void insertSingleValue(String key, String value, Map<String, List<String>> params) {
		List<String> values = new ArrayList<String>();
		values.add(value);
		params.put(key, values);
	}
}
