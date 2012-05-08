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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kahadb.util.ByteArrayInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanBuilder;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;
import com.mnxfst.testing.handler.exec.response.PTestPlanExecutionStartedResponse;
import com.mnxfst.testing.handler.exec.response.PTestPlanExecutorResponseBuilder;
import com.mnxfst.testing.server.PTestServerContextRequestHandler;
import com.mnxfst.testing.server.cfg.PTestServerConfiguration;
import com.mnxfst.testing.server.exception.ContextInitializationFailedException;
import com.mnxfst.testing.server.exception.RequestProcessingFailedException;

/**
 * Provides a context handler implementation for processing incoming requests regarding the execution of test plans
 * and the collection of results 
 * @author mnxfst
 * @since 29.03.2012
 */
public class PTestPlanExecutionContextHandler implements PTestServerContextRequestHandler {
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	// action parameter name and available values
	public static final String CONTEXT_HANDLER_ACTION_PARAM = "action";
	public static final String CONTEXT_HANDLER_EXECUTE_ACTION_PARAM_VALUE = "execute";
	public static final String CONTEXT_HANDLER_COLLECT_ACTION_PARAM_VALUE = "collect";
	public static final String CONTEXT_HANDLER_INTERRUPT_ACTION_PARAM_VALUE = "interrupt";

	// parameters required to execute a testplan
	public static final String CONTEXT_HANDLER_NUM_OF_THREADS_PARAM = "threads";
	public static final String CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM = "recurrences";
	public static final String CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM = "recurrenceType";
	public static final String CONTEXT_HANDLER_TESTPLAN_PARAM = "testplan";
	
	// parameters required to collect results
	public static final String CONTEXT_HANDLER_RESULT_IDENTIFIER_PARAM = "resultIdentifier";	

	// general response codes
	public static final int RESPONSE_CODE_EXECUTION_STARTED = 1;
	public static final int RESPONSE_CODE_EXECUTION_RESULTS_CONTAINED = 2;
	public static final int RESPONSE_CODE_EXECUTION_RESULTS_PENDING = 3;
	public static final int RESPONSE_CODE_ERROR = 4;
	
	// error codes
	public static final String ERROR_CODE_INVALID_OPTION_CODE = "invalid_option_code";
	public static final String ERROR_CODE_THREADS_MISSING_OR_INVALID = "threads_missing_or_invalid";  
	public static final String ERROR_CODE_RECURRENCES_MISSING_OR_INVALID = "recurrences_missing_or_invalid"; 
	public static final String ERROR_CODE_RECURRENCE_TYPE_MISSING_OR_INVALID = "recurrence_type_missing_or_invalid"; 
	public static final String ERROR_CODE_TESTPLAN_MISSING = "testplan_missing"; 
	public static final String ERROR_CODE_TESTPLAN_PROCESSING_ERROR = "testplan_processing_error";
	public static final String ERROR_CODE_TESTPLAN_PARSING_FAILED = "testplan_parsing_failed";
	public static final String ERROR_CODE_RESULT_ID_MISSING = "result_identifier_missing";
	public static final String ERROR_CODE_RESULT_ID_INVALID = "result_identifier_invalid";
	public static final String ERROR_CODE_ACTION_TYPE_MISSING_OR_INVALID = "action_type_missing_or_invalid";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
 	private static ConcurrentMap<String, PTestPlanExecutorResult> testPlanExecutionResultCache = new ConcurrentHashMap<String, PTestPlanExecutorResult>();
 	private static ExecutorService testPlanExecutorService = Executors.newCachedThreadPool();
	
	private String hostname = null;
	private int port = 0;
	
	/**
	 * @see com.mnxfst.testing.server.PTestServerContextRequestHandler#initialize(com.mnxfst.testing.server.cfg.PTestServerConfiguration)
	 */
	public void initialize(PTestServerConfiguration properties) throws ContextInitializationFailedException {

		// validate properties as such
		if(properties == null) 
			throw new ContextInitializationFailedException("Missing required configuration properties");
		
		// validate hostname
		if(properties.getHostname() == null || properties.getHostname().isEmpty())
			throw new ContextInitializationFailedException("Missing required hostname");
		
		// validate port
		if(properties.getPort() <= 0)
			throw new ContextInitializationFailedException("Missing required port");
		
		// fetch hostname and port from properties
		this.hostname = properties.getHostname();
		this.port = properties.getPort();
		
	}

	/**
	 * @see com.mnxfst.testing.server.PTestServerContextRequestHandler#processRequest(org.jboss.netty.handler.codec.http.HttpRequest, java.util.Map, boolean, org.jboss.netty.channel.MessageEvent)
	 */
	public void processRequest(HttpRequest httpRequest, Map<String, List<String>> requestParameters, boolean keepAlive, MessageEvent event) throws RequestProcessingFailedException {
		
		// validate request
		if(httpRequest == null)
			throw new RequestProcessingFailedException("Missing required http request");
		
		// validate request parameters
		if(requestParameters == null)
			throw new RequestProcessingFailedException("Missing required request parameter map");
		
		// validate event
		if(event == null)
			throw new RequestProcessingFailedException("Missing required message event");
		
		int errors = 0;		
		
		boolean actionTypeValid = true;
		String action = parseSingleStringValue(requestParameters.get(CONTEXT_HANDLER_ACTION_PARAM));
		if(action == null || action.trim().isEmpty()) {
			errors = errors + 1;
			actionTypeValid = false;
		}
		
		if(action.equalsIgnoreCase(CONTEXT_HANDLER_EXECUTE_ACTION_PARAM_VALUE)) {
			// parse out and validate number of threads
			boolean threadsValid = true;
			Integer numOfThreads = parseSingleIntValue(requestParameters.get(CONTEXT_HANDLER_NUM_OF_THREADS_PARAM));
			if(numOfThreads == null || numOfThreads.intValue() < 1) {
				errors = errors + 1;
				threadsValid = false;
			}		
			
			// parse out and validate number of recurrences
			boolean recurrencesValid = true;
			Integer numOfRecurrences = parseSingleIntValue(requestParameters.get(CONTEXT_HANDLER_NUM_OF_RECURRENCES_PARAM));
			if(numOfRecurrences == null || numOfRecurrences.intValue() < 1) {
				errors = errors + 1;
				recurrencesValid = false;
			}		
			
			// parse out and validate the recurrence type
			boolean recurrencesTypeValid = true;
			PTestPlanRecurrenceType recurrenceType = parseSingleRecurrenceType(requestParameters.get(CONTEXT_HANDLER_RECURRENCE_TYPE_PARAM));
			if(recurrenceType == null || recurrenceType == PTestPlanRecurrenceType.UNKNOWN) {
				errors = errors + 1;
				recurrencesTypeValid = false;
			}		
			
			// parse out and validate the testplan to execute
			boolean testPlanValid = true;
			List<String> values = requestParameters.get(CONTEXT_HANDLER_TESTPLAN_PARAM);
			String testPlan = (values != null && values.size() > 0 ? values.get(0): null);
			if(testPlan == null || testPlan.trim().isEmpty()) {
				errors = errors + 1;
				testPlanValid = false;
			}
			
			// write all request parameters having one value into a key/value map which is going to be forwarded to each test plan instance as global variables
			Map<String, Serializable> accessableTestplanVariables = new HashMap<String, Serializable>();
			for(String paramName : requestParameters.keySet()) {
				List<String> additionalValues = requestParameters.get(paramName);
				if(additionalValues != null && !additionalValues.isEmpty()) {
					accessableTestplanVariables.put(paramName, additionalValues.get(0));
				}
			}
		
			// handle errors first
			if(errors > 0) {
	
				// build a map containing the error keys as well as short messages
				PTestPlanExecutionStartedResponse response = new PTestPlanExecutionStartedResponse(hostname, port, PTestPlanExecutionStartedResponse.RESPONSE_ERROR);
	
				if(!threadsValid) {
					response.addError(ERROR_CODE_THREADS_MISSING_OR_INVALID, "Missing or invalid number of threads");
				}
				if(!recurrencesValid) {
					response.addError(ERROR_CODE_RECURRENCES_MISSING_OR_INVALID, "Missing or invalid number of recurrences");
				}
				if(!recurrencesTypeValid) {
					response.addError(ERROR_CODE_RECURRENCE_TYPE_MISSING_OR_INVALID, "Missing or invalid recurrence type");
				}
				if(!testPlanValid) {
					response.addError(ERROR_CODE_TESTPLAN_MISSING, "Missing required testplan");
				}
				
				try {
					sendResponse(PTestPlanExecutorResponseBuilder.export(response), keepAlive, event);
				} catch (InvalidConfigurationException e) {
					throw new RequestProcessingFailedException("Failed to create response for incoming request");
				}
			} else {
	
				PTestPlanExecutionStartedResponse response = new PTestPlanExecutionStartedResponse(hostname, port, PTestPlanExecutionStartedResponse.RESPONSE_OK);
				UUID resultIdentifier = UUID.fromString(new com.eaio.uuid.UUID().toString());
				
				try {
					PTestPlan plan = PTestPlanBuilder.build(new ByteArrayInputStream(testPlan.getBytes("UTF-8")));
					testPlanExecutorService.execute(new PTestPlanExecutor(plan, resultIdentifier.toString(), numOfRecurrences.intValue(), numOfRecurrences.intValue(), numOfThreads));
					response.setResultIdentifier(resultIdentifier.toString());
				} catch (UnsupportedEncodingException e) {
					response.setResponseCode(PTestPlanExecutionStartedResponse.RESPONSE_ERROR);
					response.addError(ERROR_CODE_TESTPLAN_PARSING_FAILED, "Failed to parse provided test plan due to an unsupported encoding. Error: " + e.getMessage());				
				} catch (InvalidConfigurationException e) {
					response.setResponseCode(PTestPlanExecutionStartedResponse.RESPONSE_ERROR);
					response.addError(ERROR_CODE_TESTPLAN_PARSING_FAILED, "Failed to parse provided test plan due to an invalid configuration. Error: " + e.getMessage());
				}
	
				try {
					sendResponse(PTestPlanExecutorResponseBuilder.export(response), keepAlive, event);
				} catch (InvalidConfigurationException e) {
					throw new RequestProcessingFailedException("Failed to create response for incoming request");
				}				
			}
		} else if(action.equalsIgnoreCase(CONTEXT_HANDLER_COLLECT_ACTION_PARAM_VALUE)) {
			
			PTestPlanExecutionStartedResponse response = new PTestPlanExecutionStartedResponse(hostname, port, PTestPlanExecutionStartedResponse.RESPONSE_OK);

			String resultIdentifier = parseSingleStringValue(requestParameters.get(CONTEXT_HANDLER_RESULT_IDENTIFIER_PARAM));
			if(resultIdentifier == null || resultIdentifier.trim().isEmpty()) {
				response.setResponseCode(PTestPlanExecutionStartedResponse.RESPONSE_ERROR);
				response.addError(ERROR_CODE_RESULT_ID_MISSING, "Missing required result identifier");
			} else {
				PTestPlanExecutorResult result = testPlanExecutionResultCache.get(resultIdentifier);
				if(result == null) {
					response.setResponseCode(PTestPlanExecutionStartedResponse.RESPONSE_ERROR);
					response.addError(ERROR_CODE_RESULT_ID_INVALID, "Invalid result identifier '"+resultIdentifier+"'");
				} else {					
					response.setAverageRuntime(result.getAverageRuntime());
					response.setMaxRuntime(result.getMaxRuntime());
					response.setMedianRuntime(result.getMedianRuntime());
					response.setMinRuntime(result.getMinRuntime());
					response.setNumOfRuns(result.getNumOfRuns());
					response.setResultIdentifier(resultIdentifier);
					response.setTestplan(result.getTestplanName());
					response.setTotalRuntime(result.getTotalRuntime());
					response.setWorkerThreads(Long.valueOf(result.getWorkerThreads()));
					response.setWorkQueueSize(Long.valueOf(result.getWorkQueueSize()));					
				}
			}
			try {
				sendResponse(PTestPlanExecutorResponseBuilder.export(response), keepAlive, event);
			} catch (InvalidConfigurationException e) {
				throw new RequestProcessingFailedException("Failed to create response for incoming request");
			}				

		} else {
			PTestPlanExecutionStartedResponse response = new PTestPlanExecutionStartedResponse(hostname, port, PTestPlanExecutionStartedResponse.RESPONSE_ERROR);
			response.addError(ERROR_CODE_ACTION_TYPE_MISSING_OR_INVALID, "Missing or invalid action type '"+action+"'");
			try {
				sendResponse(PTestPlanExecutorResponseBuilder.export(response), keepAlive, event);
			} catch (InvalidConfigurationException e) {
				throw new RequestProcessingFailedException("Failed to create response for incoming request");
			}				
		}
		
	}
	
	////////////////////////////////////////// REFACTOR TO PARENT CLASS IF NEEDED //////////////////////////////////////////
	
	/**
	 * Parses out a single recurrences type from the provided list of values. If the result is null, the list did not contain any value
	 * or the value could not be parsed into a integer object   
	 * @param values
	 * @return
	 */
	protected PTestPlanRecurrenceType parseSingleRecurrenceType(List<String> values) {
		if(values == null)
			return null;
		
		String tmp = values.get(0);
		if(tmp == null || tmp.isEmpty())
			return null;
		
		if(tmp.equalsIgnoreCase("TIMES"))
			return PTestPlanRecurrenceType.TIMES;
		else if(tmp.equalsIgnoreCase("MILLIS"))
			return PTestPlanRecurrenceType.MILLIS;
		else if(tmp.equalsIgnoreCase("SECONDS"))
			return PTestPlanRecurrenceType.SECONDS;
		else if(tmp.equalsIgnoreCase("MINUTES"))
			return PTestPlanRecurrenceType.MINUTES;
		else if(tmp.equalsIgnoreCase("HOURS"))
			return PTestPlanRecurrenceType.HOURS;
		else if(tmp.equalsIgnoreCase("DAYS"))
			return PTestPlanRecurrenceType.DAYS;
		
		return null;
	}
	
	/**
	 * Parses out a single int value from the provided list of values. If the result is null, the list did not contain any value
	 * or the value could not be parsed into a integer object   
	 * @param values
	 * @return
	 */
	protected Integer parseSingleIntValue(List<String> values)  {
		
		if(values == null)
			return null;
		
		String tmp = values.get(0);
		if(tmp == null || tmp.isEmpty())
			return null;

		try {
			return Integer.valueOf(values.get(0));
		} catch(NumberFormatException e) {
			
		}
		
		return null;
	}

	/** 
	 * Parses out a single string value from the provided list of values.
	 * @param values
	 * @return
	 */
	protected String parseSingleStringValue(List<String> values) {
		
		if(values == null)
			return null;
		
		return values.get(0);
	}
	
	/**
	 * Sends a response containing the given message to the calling client
	 * @param responseMessage
	 * @param keepAlive
	 * @param event
	 */
	private void sendResponse(String responseMessage, boolean keepAlive, MessageEvent event) {
		HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		
		httpResponse.setContent(ChannelBuffers.copiedBuffer(responseMessage, CharsetUtil.UTF_8));
		httpResponse.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
		
		if(keepAlive)
			httpResponse.setHeader(HttpHeaders.Names.CONTENT_LENGTH, httpResponse.getContent().readableBytes());
		
		ChannelFuture future = event.getChannel().write(httpResponse);
		if(!keepAlive)
			future.addListener(ChannelFutureListener.CLOSE);
	}
	
	protected static void addResponse(String identifier, PTestPlanExecutorResult result) {
		testPlanExecutionResultCache.put(identifier, result);
	}
	
	protected PTestPlanExecutorResult getResponse(String identifier) {
		return testPlanExecutionResultCache.get(identifier);
	}
	
	protected Set<String> getResponseIdentifiers() {
		return testPlanExecutionResultCache.keySet();
	}
}
