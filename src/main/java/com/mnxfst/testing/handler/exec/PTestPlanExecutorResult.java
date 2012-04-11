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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Provides a container for the results collected by the {@link PTestPlanExecutor}
 * @author mnxfst
 * @since 12.04.2012
 */
public class PTestPlanExecutorResult implements Serializable {

	private static final long serialVersionUID = 4274768036961976465L;

	private String resultIdentifier = null;
	private String testplanName = null;
	private long totalRuntime = 0;
	private long minRuntime = 0;
	private long maxRuntime = 0;
	private double averageRuntime = 0;
	private double medianRuntime = 0;
	private long numOfRuns;
	private int workQueueSize;
	private int workerThreads;

	public PTestPlanExecutorResult() {
		
	}
	
	public PTestPlanExecutorResult(String resultIdentifier, String testplanName, long totalRuntime, long minRuntime, long maxRuntime, double averageRuntime, double medianRuntime, long numOfRuns, int workQueueSize, int workerThreads) {
		this.resultIdentifier = resultIdentifier;
		this.testplanName = testplanName;
		this.totalRuntime = totalRuntime;
		this.minRuntime = minRuntime;
		this.maxRuntime = maxRuntime;
		this.averageRuntime = averageRuntime;
		this.medianRuntime = medianRuntime;
		this.numOfRuns = numOfRuns;
		this.workerThreads = workerThreads;
		this.workQueueSize = workQueueSize;
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
	 * @return the testplanName
	 */
	public String getTestplanName() {
		return testplanName;
	}

	/**
	 * @param testplanName the testplanName to set
	 */
	public void setTestplanName(String testplanName) {
		this.testplanName = testplanName;
	}

	/**
	 * @return the totalRuntime
	 */
	public long getTotalRuntime() {
		return totalRuntime;
	}

	/**
	 * @param totalRuntime the totalRuntime to set
	 */
	public void setTotalRuntime(long totalRuntime) {
		this.totalRuntime = totalRuntime;
	}

	/**
	 * @return the minRuntime
	 */
	public long getMinRuntime() {
		return minRuntime;
	}

	/**
	 * @param minRuntime the minRuntime to set
	 */
	public void setMinRuntime(long minRuntime) {
		this.minRuntime = minRuntime;
	}

	/**
	 * @return the maxRuntime
	 */
	public long getMaxRuntime() {
		return maxRuntime;
	}

	/**
	 * @param maxRuntime the maxRuntime to set
	 */
	public void setMaxRuntime(long maxRuntime) {
		this.maxRuntime = maxRuntime;
	}

	/**
	 * @return the averageRuntime
	 */
	public double getAverageRuntime() {
		return averageRuntime;
	}

	/**
	 * @param averageRuntime the averageRuntime to set
	 */
	public void setAverageRuntime(double averageRuntime) {
		this.averageRuntime = averageRuntime;
	}

	/**
	 * @return the medianRuntime
	 */
	public double getMedianRuntime() {
		return medianRuntime;
	}

	/**
	 * @param medianRuntime the medianRuntime to set
	 */
	public void setMedianRuntime(double medianRuntime) {
		this.medianRuntime = medianRuntime;
	}
	
	/**
	 * @return the numOfRuns
	 */
	public long getNumOfRuns() {
		return numOfRuns;
	}

	/**
	 * @param numOfRuns the numOfRuns to set
	 */
	public void setNumOfRuns(long numOfRuns) {
		this.numOfRuns = numOfRuns;
	}

	/**
	 * @return the workQueueSize
	 */
	public int getWorkQueueSize() {
		return workQueueSize;
	}

	/**
	 * @param workQueueSize the workQueueSize to set
	 */
	public void setWorkQueueSize(int workQueueSize) {
		this.workQueueSize = workQueueSize;
	}

	/**
	 * @return the workerThreads
	 */
	public int getWorkerThreads() {
		return workerThreads;
	}

	/**
	 * @param workerThreads the workerThreads to set
	 */
	public void setWorkerThreads(int workerThreads) {
		this.workerThreads = workerThreads;
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("resultIdentifier", this.resultIdentifier)
			.append("testplanName", this.testplanName)
			.append("numOfRuns", this.numOfRuns)
			.append("workQueueSize", this.workQueueSize)
			.append("workerThreads", this.workerThreads)
			.append("totalRuntime", this.totalRuntime)
			.append("minRuntime", this.minRuntime)
			.append("maxRuntime", this.maxRuntime)
			.append("averageRuntime", this.averageRuntime)
			.append("medianRuntime", this.medianRuntime)
			.toString();
	}
}
