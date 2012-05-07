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

package com.mnxfst.testing.handler.exec.activity.log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import com.mnxfst.testing.handler.exec.PTestPlanExecutionContext;
import com.mnxfst.testing.handler.exec.activity.AbstractActivity;
import com.mnxfst.testing.handler.exec.exception.ActivityExecutionFailedException;
import com.mnxfst.testing.handler.exec.exception.ContextVariableEvaluationFailedException;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Implements a simple log activity using log4j as destination
 * @author mnxfst
 * @since 07.05.2012
 */
public class Log4jActivity extends AbstractActivity {

	private static final Logger logger = Logger.getLogger(Log4jActivity.class);
	
	public static final String CFG_LOG_MESSAGE_TEMPLATE = "logTemplate";
	
	/** log message template */
	private String logMessageTemplate = null;
	/** holds the identified template variables */
	private Map<String, String> logTemplateVariables = new HashMap<String, String>();

	
	/**
	 * @see com.mnxfst.testing.handler.exec.activity.PTestPlanActivity#configure(java.util.Map, java.util.Map)
	 */	
	public void configure(Map<String, String> configuration, Map<String, String> exportVariables) throws InvalidConfigurationException {
		if(configuration == null || configuration.isEmpty())
			throw new InvalidConfigurationException("Required configuration set is either null or empty");
		
		this.logMessageTemplate = configuration.get(CFG_LOG_MESSAGE_TEMPLATE);
		if(this.logMessageTemplate == null || this.logMessageTemplate.trim().isEmpty())
			throw new InvalidConfigurationException("Required configuration option '"+CFG_LOG_MESSAGE_TEMPLATE+"' missing for activity '"+getActivityId()+"'");
		
		this.logTemplateVariables = getContextVariablesFromString(this.logMessageTemplate);
		
		if(logger.isDebugEnabled())
			logger.debug("activity[id="+getActivityId()+", type="+getActivityType()+", logTemplate="+logMessageTemplate+"] sucessfully initialized");
	}

	/**
	 * @see com.mnxfst.testing.handler.exec.activity.PTestPlanActivity#execute(int, com.mnxfst.testing.handler.exec.PTestPlanExecutionContext)
	 */
	@Override
	public void execute(int elementId, PTestPlanExecutionContext contextElement) throws ActivityExecutionFailedException {

		// replace log with values fetched from context
		String payload = null;
		try {
			payload = new String(this.logMessageTemplate.getBytes(), "UTF-8");
		} catch(UnsupportedEncodingException e) {
			logger.error("Failed to convert log template into UTF-8 string. Error: " + e.getMessage());
			throw new ActivityExecutionFailedException("Failed to convert log template into UTF-8 string. Error: " + e.getMessage());
		}

		// iterate through log template variables
		for(String logPattern : logTemplateVariables.keySet()) {
			
			// fetch the associated log pattern / replacement pattern
			String replacementPattern = logTemplateVariables.get(logPattern);
			
			// evaluate the pattern using the provided context information
			Object ctxValue = null;
			try {
				ctxValue = contextElement.evaluate(logPattern);
			} catch(ContextVariableEvaluationFailedException e) {
				throw new ActivityExecutionFailedException("Failed to evaluate pattern '" + logPattern + "'. Error: " + e.getMessage());
			}
			
			// if the value is not null, replace all pattern
			if(ctxValue != null)
				payload = payload.replaceAll(replacementPattern, Matcher.quoteReplacement(ctxValue.toString())); 			
		}
		
//		logger.warn(payload);
		
		contextElement.setNextActivityId(getNextActivityId());
	}

}
