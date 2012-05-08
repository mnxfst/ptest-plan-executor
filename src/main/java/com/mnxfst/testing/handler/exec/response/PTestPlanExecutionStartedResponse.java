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

package com.mnxfst.testing.handler.exec.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;

/**
 * Test plan execution started response type
 * @author mnxfst
 * @since 08.05.2012
 */
@XStreamAlias ( "planExecutor" )
public class PTestPlanExecutionStartedResponse implements Serializable {

	private static final long serialVersionUID = 5497823392857471187L;
	
	public static final int RESPONSE_OK = 1;
	public static final int RESPONSE_ERROR = 2;

	@XStreamAlias ( "hostname" )
	private String hostname = null;
	@XStreamAlias ( "port" )
	private Integer port = null;
	@XStreamAlias ( "responseCode" )
	private Integer responseCode = null;
	@XStreamAlias ( "resultIdentifier" )
	private String resultIdentifier = null;
	@XStreamConverter (MapConverter.class)
	private Map<String, String> errors = new HashMap<String, String>();
	
	@XStreamAlias ("testplan")
	private String testplan = null;
	@XStreamAlias ("totalRuntime")
	private Long totalRuntime = null;
	@XStreamAlias ("minRuntime")
	private Long minRuntime = null;
	@XStreamAlias ("maxRuntime")
	private Long maxRuntime = null;
	@XStreamAlias ("averageRuntime")
	private Double averageRuntime = null;
	@XStreamAlias ("medianRuntime")
	private Double medianRuntime = null;
	@XStreamAlias ("numOfRuns")
	private Long numOfRuns = null;
	@XStreamAlias ("workQueueSize")
	private Long workQueueSize = null;
	@XStreamAlias ("workerThreads")
	private Long workerThreads = null;
	
	public PTestPlanExecutionStartedResponse() {		
	}
	
	public PTestPlanExecutionStartedResponse(String hostname, int port, int responseCode) {
		this.hostname = hostname;
		this.port = port;
		this.responseCode = responseCode;
	}
	
	public PTestPlanExecutionStartedResponse(String hostname, int port, int responseCode, String resultIdentifier) {
		this.hostname = hostname;
		this.port = port;
		this.responseCode = responseCode;
		this.resultIdentifier = resultIdentifier;
	}

	public void addError(String errorKey, String errorMessage) {
		this.errors.put(errorKey, errorMessage);
	}
	
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the responseCode
	 */
	public Integer getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the resultIdentifier
	 */
	public String getResultIdentifier() {
		return resultIdentifier;
	}

	/**
	 * @param resultIdentifier the resultIdentifier to set
	 */
	public void setResultIdentifier(String resultIdentifier) {
		this.resultIdentifier = resultIdentifier;
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the testplan
	 */
	public String getTestplan() {
		return testplan;
	}

	/**
	 * @param testplan the testplan to set
	 */
	public void setTestplan(String testplan) {
		this.testplan = testplan;
	}

	/**
	 * @return the totalRuntime
	 */
	public Long getTotalRuntime() {
		return totalRuntime;
	}

	/**
	 * @param totalRuntime the totalRuntime to set
	 */
	public void setTotalRuntime(Long totalRuntime) {
		this.totalRuntime = totalRuntime;
	}

	/**
	 * @return the minRuntime
	 */
	public Long getMinRuntime() {
		return minRuntime;
	}

	/**
	 * @param minRuntime the minRuntime to set
	 */
	public void setMinRuntime(Long minRuntime) {
		this.minRuntime = minRuntime;
	}

	/**
	 * @return the maxRuntime
	 */
	public Long getMaxRuntime() {
		return maxRuntime;
	}

	/**
	 * @param maxRuntime the maxRuntime to set
	 */
	public void setMaxRuntime(Long maxRuntime) {
		this.maxRuntime = maxRuntime;
	}

	/**
	 * @return the averageRuntime
	 */
	public Double getAverageRuntime() {
		return averageRuntime;
	}

	/**
	 * @param averageRuntime the averageRuntime to set
	 */
	public void setAverageRuntime(Double averageRuntime) {
		this.averageRuntime = averageRuntime;
	}

	/**
	 * @return the medianRuntime
	 */
	public Double getMedianRuntime() {
		return medianRuntime;
	}

	/**
	 * @param medianRuntime the medianRuntime to set
	 */
	public void setMedianRuntime(Double medianRuntime) {
		this.medianRuntime = medianRuntime;
	}

	/**
	 * @return the numOfRuns
	 */
	public Long getNumOfRuns() {
		return numOfRuns;
	}

	/**
	 * @param numOfRuns the numOfRuns to set
	 */
	public void setNumOfRuns(Long numOfRuns) {
		this.numOfRuns = numOfRuns;
	}

	/**
	 * @return the workQueueSize
	 */
	public Long getWorkQueueSize() {
		return workQueueSize;
	}

	/**
	 * @param workQueueSize the workQueueSize to set
	 */
	public void setWorkQueueSize(Long workQueueSize) {
		this.workQueueSize = workQueueSize;
	}

	/**
	 * @return the workerThreads
	 */
	public Long getWorkerThreads() {
		return workerThreads;
	}

	/**
	 * @param workerThreads the workerThreads to set
	 */
	public void setWorkerThreads(Long workerThreads) {
		this.workerThreads = workerThreads;
	}
	
	
	
	
	
}
