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

import java.util.Map;

import com.mnxfst.testing.handler.exec.PTestPlanExecutionContext;
import com.mnxfst.testing.handler.exec.exception.ActivityExecutionFailedException;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Provides a common interface to all activities operating on a {@link PTestPlanExecutionContext}
 * @author mnxfst
 * @since 11.04.2012
 */
public interface PTestPlanActivity {

	/**
	 * Sets the type of the activity
	 * @param type
	 */
	public void setActivityType(String type);
	
	/**
	 * Returns the activity type
	 * @return
	 */
	public String getActivityType();
	
	/**
	 * Sets the key which uniquely identifies this activity
	 * @param actionId
	 */
	public void setActivityId(String activityId);
	
	/**
	 * Returns the key which uniquely identifies this activity
	 * @return
	 */
	public String getActivityId();
	
	/**
	 * Sets the activity description
	 * @param description
	 */
	public void setDescription(String description);
	
	/**
	 * Returns the activity description
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Sets the next activity id
	 */
	public void setNextActivityId(String nextActivityId);
	
	/**
	 * Returns the id of the next activity
	 * @return
	 */
	public String getNextActivityId();
	
	/**
	 * Configures the activity instance using the provided map of key/value pairs
	 * @param configuration
	 * @throws InvalidConfigurationException
	 */
	public void configure(Map<String, String> configuration, Map<String, String> exportVariables) throws InvalidConfigurationException;
	
	/**
	 * Executes the implemented behavior on the provided {@link PTestPlanExecutionContext context} element and reports back
	 * @param elementId
	 * @param contextElement
	 * @param executionController
	 * @throws SCActionExecutionFailedException
	 */
	public void execute(int elementId, PTestPlanExecutionContext contextElement) throws ActivityExecutionFailedException;
	
	/**
	 * Interrupts the action
	 */
	public void interrupt();

	
}
