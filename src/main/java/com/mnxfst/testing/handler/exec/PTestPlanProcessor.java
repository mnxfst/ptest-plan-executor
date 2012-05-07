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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.mnxfst.testing.handler.exec.activity.PTestPlanActivity;
import com.mnxfst.testing.handler.exec.exception.ActivityExecutionFailedException;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;
import com.mnxfst.testing.handler.exec.exception.StateAlreadyVisitedException;

/**
 * The processor is in charge of executing a single run. It receives a {@link PTestPlanExecutionContext context element}
 * to work on, an unique identifier and a set of available actions that carry out the required steps on the context element.
 * @author mnxfst
 * @since 11.04.2012
 */
public class PTestPlanProcessor implements Callable<PTestPlanExecutionContext> {

	private static final Logger logger = Logger.getLogger(PTestPlanProcessor.class);
	
	/** initial state identifier */
	public static final String INIT_STATE = "INIT";
	/** final state identifier */
	public static final String FINAL_STATE= "FINAL";
	
	/** unique process identifier */
	private final String workerId;
	/** context element to work on */
	private final PTestPlanExecutionContext element;
	/** map of action names towards the action implementations which will be used to work on the context element */
	private final Map<String, PTestPlanActivity> activities;
	
	
	/**
	 * Initializes the processor instance
	 * @param workerId
	 * @param element
	 * @param actions
	 */
	public PTestPlanProcessor(final String workerId, final PTestPlanExecutionContext element, final Map<String, PTestPlanActivity> activities) throws InvalidConfigurationException {
		
		// validate worker identifier
		if(workerId == null || workerId.trim().isEmpty())
			throw new InvalidConfigurationException("Required worker identifier missing or empty");
		
		// validate context element
		if(element == null)
			throw new InvalidConfigurationException("Required context element mising");
		if(element.getNextActivityId() == null || element.getNextActivityId().isEmpty())
			throw new InvalidConfigurationException("Required 'next state' attribute of context element does not carry any values");
		
		// validate actions
		if(activities == null || activities.isEmpty())
			throw new InvalidConfigurationException("Required set of available actions missing");
		
		// assign variables
		this.element = element;
		this.activities = activities;
		this.workerId = workerId;
	}


	/**
	 * @see java.util.concurrent.Callable#call()
	 */	
	public PTestPlanExecutionContext call() throws Exception {

		// count number of activities being called during run 
		int activityCalls = 0;
		// set the start time 
		element.setInitTimestamp(System.currentTimeMillis());
		// keeps track of the states already visited
		Set<String> statesAlreadyVisited = new HashSet<String>();

		// keep on going until the next state attribute references the final state
		while(element.getNextActivityId() != null) {

			// fetch the next action and report an error and quit if the action does not exist
			PTestPlanActivity nextActivity = activities.get(element.getNextActivityId());
			if(nextActivity == null) {
				logger.error("[worker="+workerId+"] Referenced next state '"+element.getNextActivityId()+"' not found. Exiting...");				
				// TODO add error state holder to element
				break;
			}
			
			// execute the action and report an error and quit if the action finished with an error -- logical errors or anything else must be handled through error cases in the test plan, this represents just technical errors!
			try {
				nextActivity.execute(1, element);
				element.setCurrentActivityId(nextActivity.getActivityId());
				activityCalls = activityCalls + 1;
			} catch (ActivityExecutionFailedException e) {
				logger.error("[worker="+workerId+"] Failed to execute action '"+nextActivity.getActivityId()+"'. Reporting cell back as being available for further processing. Reason: " + e.getMessage(), e);
				// TODO add error state holder to element
			}
			
			// if the current state name equals the 'FINAL' identifier, finish the execution
			if(element.getCurrentActivityId() == null || element.getCurrentActivityId().trim().isEmpty() || element.getCurrentActivityId().equalsIgnoreCase(FINAL_STATE))
				break;
		
			if(statesAlreadyVisited.contains(element.getCurrentActivityId()))
				throw new StateAlreadyVisitedException("The state '"+element.getCurrentActivityId()+"' has already been visited. Assuming cycle in test plan definition. Please check!");
			else
				statesAlreadyVisited.add(element.getCurrentActivityId()); 
		}

		element.setActionCalls(activityCalls);
		element.setFinalTimestamp(System.currentTimeMillis());
		return element;
	}
	
}
