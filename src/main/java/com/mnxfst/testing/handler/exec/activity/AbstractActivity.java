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

package com.mnxfst.testing.handler.exec.activity;


/**
 * Provides an abstract parent implementation to all {@link PTestPlanActivity activities}
 * @author mnxfst
 * @since 11.04.2012
 */
public abstract class AbstractActivity implements PTestPlanActivity {
	
	private String activityType = null;
	private String activityId = null;
	private String description = null;
	private String nextActivityId = null;
	private boolean running = false;

	/**
	 * Default constructor
	 */
	public AbstractActivity() {		
	}

	/**
	 * Initializes the activity using the provided values
	 * @param activityId
	 * @param activityType
	 * @param nextActivityId
	 * @param description
	 */
	public AbstractActivity(String activityId, String activityType, String nextActivityId, String description) {
		this.activityId = activityId;
		this.activityType = activityType;
		this.nextActivityId = nextActivityId;
		this.description = description;
	}
    
    /**
     * Interrupts the action
     */
    public void interrupt() {
    	this.running = false;
    }

	/**
	 * @return the activityType
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * @param activityType the activityType to set
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * @return the activityId
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
    
}
