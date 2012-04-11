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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;

/**
 * Contains all required information for a single {@link PTestPlan} execution run. Since the context is being operated
 * only by one action at a time, there is no need to make this implementation thread-safe at the moment. 
 *
 * @author mnxfst
 * @since 11.04.2012
 */
public class PTestPlanExecutionContext implements Serializable {

	private static final long serialVersionUID = -8720239271745632501L;

	/** 
	 * Numerical identifier being assigned to each run by the producer to uniquely identify each entity
	 */
	private long runId = 0;

	/** 
	 * Provides a transient store for exchanging pairs of keys and values between actions operating 
	 * within a single execution run. Either the last consumer or the entity producer must ensure that
	 * the map is completely cleaned up and all contained objects disconnected before reusing the entity.
	 */
	private Map<String, Serializable> contextVariables = new HashMap<String, Serializable>();

	/**
	 * Names the current state or step within a test plan
	 */
	private String currentActivityId = null;
	
	/**
	 * Names the next state or step to execute within a test plan. 
	 */
	private String nextActivityId = null;

	/**
	 * Timestamp of entity initialization
	 */
	private long initTimestamp = 0;
	
	/**
	 * Timestamp of final processing step
	 */
	private long finalTimestamp = 0;

	/**
	 * Counts the number of actions executed
	 */
	private int actionCalls = 0;
	
	// TODO HISTOGRAM
	
	/**
	 * Default constructor
	 */
	public PTestPlanExecutionContext() {		
	}

	/**
	 * Adds a new context variable
	 * @param key
	 * @param value
	 */
	public void addContextVariable(String key, Serializable value) {
		this.contextVariables.put(key, value);
	}
	
	/**
	 * Returns the associated value from the context
	 * @param key
	 * @return
	 */
	public Serializable getContextVariable(String key) {
		return this.contextVariables.get(key);
	}
		
	public void cleanUp() {
		this.runId = 0;
		this.currentActivityId = null;
		this.finalTimestamp = 0;
		this.initTimestamp = 0;
		this.nextActivityId = null;
		this.contextVariables.clear();
	}
	
	/**
	 * @return the runId
	 */
	public long getRunId() {
		return runId;
	}

	/**
	 * @param runId the runId to set
	 */
	public void setRunId(long runId) {
		this.runId = runId;
	}

	/**
	 * @return the contextVariables
	 */
	public Map<String, Serializable> getContextVariables() {
		return contextVariables;
	}

	/**
	 * @param contextVariables the contextVariables to set
	 */
	public void setContextVariables(Map<String, Serializable> contextVariables) {
		this.contextVariables = contextVariables;
	}
	
	/**
	 * @return the currentActivityId
	 */
	public String getCurrentActivityId() {
		return currentActivityId;
	}

	/**
	 * @param currentActivityId the currentActivityId to set
	 */
	public void setCurrentActivityId(String currentActivityId) {
		this.currentActivityId = currentActivityId;
	}

	/**
	 * @return the nextActivityId
	 */
	public String getNextActivityId() {
		return nextActivityId;
	}

	/**
	 * @param nextActivityId the nextActivityId to set
	 */
	public void setNextActivityId(String nextActivityId) {
		this.nextActivityId = nextActivityId;
	}

	/**
	 * @return the initTimestamp
	 */
	public long getInitTimestamp() {
		return initTimestamp;
	}

	/**
	 * @param initTimestamp the initTimestamp to set
	 */
	public void setInitTimestamp(long initTimestamp) {
		this.initTimestamp = initTimestamp;
	}

	/**
	 * @return the finalTimestamp
	 */
	public long getFinalTimestamp() {
		return finalTimestamp;
	}

	/**
	 * @param finalTimestamp the finalTimestamp to set
	 */
	public void setFinalTimestamp(long finalTimestamp) {
		this.finalTimestamp = finalTimestamp;
	}

	/**
	 * @return the actionCalls
	 */
	public int getActionCalls() {
		return actionCalls;
	}

	/**
	 * @param actionCalls the actionCalls to set
	 */
	public void setActionCalls(int actionCalls) {
		this.actionCalls = actionCalls;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("runId", this.runId)
			.append("initTimestamp", this.initTimestamp)
			.append("finalTimestamp", this.finalTimestamp)
			.append("currentActivityId", this.currentActivityId)
			.append("nextActivityId", this.nextActivityId)
			.append("actionCalls", this.actionCalls)
			.append("contextVariables", this.contextVariables).toString();
	}
	
	
}
