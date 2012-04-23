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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.exception.ContextVariableEvaluationFailedException;

/**
 * Contains all required information for a single {@link PTestPlan} execution run. Since the context is being operated
 * only by one action at a time, there is no need to make this implementation thread-safe at the moment. 
 *
 * @author mnxfst
 * @since 11.04.2012
 */
public class PTestPlanExecutionContext implements Serializable {

	private static final long serialVersionUID = -8720239271745632501L;

	private static final int REPLACEMENT_PATTERN_PREFIX_LENGTH = 2; // ${ == 2 characters
	private static final String[] EMPTY_STRING_ARRAY = new String[0];

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
	 * Holds a mapping between a replacement pattern and its already evaluated pattern object to be used for faster access  
	 */
	private Map<String, PTestPlanExecutionContextReplacementPattern> replacementPatternMapping = new HashMap<String, PTestPlanExecutionContextReplacementPattern>();

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
		
	/**
	 * Cleans up the context
	 */
	public void cleanUp() {
		this.runId = 0;
		this.currentActivityId = null;
		this.finalTimestamp = 0;
		this.initTimestamp = 0;
		this.nextActivityId = null;
		this.contextVariables.clear();
	}
	
	/**
	 * The provided pattern names a context variable and an access path along getter methods that
	 * need to be evaluated to fetch the final value. Eg.: ${user.address.street} executes user#getAddress#getStreet
	 * @param replacementPattern
	 * @return
	 * @throws ContextVariableEvaluationFailedException
	 */
	public Object evaluate(String replacementPattern) throws ContextVariableEvaluationFailedException {
		
		// validate the replacement pattern
		if(replacementPattern == null || replacementPattern.trim().isEmpty())
			throw new ContextVariableEvaluationFailedException("No replacement pattern provided");

		// find replacement pattern in map of previously computed pattern and apply access methods
		PTestPlanExecutionContextReplacementPattern patternInstance = replacementPatternMapping.get(replacementPattern);
		if(patternInstance != null) {
			Serializable ctxVariable = getContextVariable(patternInstance.getName());
			return evaluateObject(ctxVariable, patternInstance.getAccessMethods());			
		}
		
		// ensure that the replacement pattern starts with '${' and ends with '}'
		if(replacementPattern.startsWith("${") && replacementPattern.endsWith("}")) {
			
			// extract context variable name
			String ctxVariableName = extractContextVariableName(replacementPattern);
			Serializable ctxVariable = getContextVariable(ctxVariableName);
			if(ctxVariable == null)
				return null;
			
			// if the variable has been found, try to extract the getter method names along the path described. if there 
			// are not getters to be called, just return the variable value
			String[] getterMethodNames = extractGetterMethodNames(replacementPattern);
			if(getterMethodNames == null || getterMethodNames.length < 1)
				return ctxVariable;
			
			// convert the getter names into method representations
			List<Method> getterMethods = new ArrayList<Method>();
			extractGetterMethods(ctxVariable.getClass(), getterMethodNames, getterMethods);
			
			// create entity for previously mentioned association map for pattern information and insert it
			PTestPlanExecutionContextReplacementPattern patternMapping = new PTestPlanExecutionContextReplacementPattern(replacementPattern, ctxVariableName);
			for(Method m : getterMethods)
				patternMapping.addAccessMethod(m);
			replacementPatternMapping.put(replacementPattern, patternMapping);
			
			// evaluate the getter methods against the variable value
			return evaluateObject(ctxVariable, getterMethods);
		} 
		
		throw new ContextVariableEvaluationFailedException("Invalid replacement pattern: " + replacementPattern + ". Expected prefix: ${variable}");
	}
	

	/**
	 * Evaluates the provided list of methods on the given object and the results of the getter recursively 
	 * @param input
	 * @param getterMethods
	 * @return
	 * @throws TSVariableEvaluationFailedException
	 */
	protected Object evaluateObject(Object input, List<Method> getterMethods) throws ContextVariableEvaluationFailedException {
		
		if(input == null)
			return null;

		if(getterMethods != null && !getterMethods.isEmpty()) {
			
			Method nextGetterMethod = getterMethods.get(0);
			Object result = null;
			
			try {
				result = nextGetterMethod.invoke(input);
			} catch (IllegalArgumentException e) {
				throw new ContextVariableEvaluationFailedException("Failed to evaluate method '"+nextGetterMethod.getName()+"' on entity of type " + input.getClass().getName() + ". Error: " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new ContextVariableEvaluationFailedException("Failed to evaluate method '"+nextGetterMethod.getName()+"' on entity of type " + input.getClass().getName() + ". Error: " + e.getMessage());
			} catch (InvocationTargetException e) {
				throw new ContextVariableEvaluationFailedException("Failed to evaluate method '"+nextGetterMethod.getName()+"' on entity of type " + input.getClass().getName() + ". Error: " + e.getMessage());
			}

			if(getterMethods.size() > 1 && result != null)
				return evaluateObject(result, getterMethods.subList(1, getterMethods.size()));
			
			return result;
		}
		
		return input;
		
	}
	
	/**
	 * Extracts the context variable name from the given replacement pattern. The provided prefix helps to speed up the
	 * stripping and must be contained in the replacement pattern. The replacement pattern is expected to have a prefix 
	 * holding the characters ${ and a postfix of char }
	 * @param replacementPattern 
	 * @return
	 */
	protected String extractContextVariableName(String replacementPattern) {
		
		String tmp = replacementPattern.substring(REPLACEMENT_PATTERN_PREFIX_LENGTH);
		if(tmp.indexOf(".") == -1) {
			return tmp.substring(0, tmp.length() - 1);
		}
		return tmp.substring(0, tmp.indexOf('.'));
	}
	
	/**
	 * Extracts the getter method names that needs to be executed along the expression path for evaluate
	 * an objects value. The provided prefix helps to speed-up stripping down the storage dependent prefix, eg. ${global.
	 * @param replacementPattern the provided input is assumed to be not null and must contain the provided prefix
	 * @param storageDependentPrefix the provided input is assumed to be not null and not empty
	 * @return
	 */
	protected String[] extractGetterMethodNames(String replacementPattern) {

		if(replacementPattern == null || replacementPattern.trim().isEmpty())
			return EMPTY_STRING_ARRAY;
		
		// strip out the named prefix and the closing brackts
		String[] splittedPath = replacementPattern.substring(REPLACEMENT_PATTERN_PREFIX_LENGTH, replacementPattern.length() - 1).split("\\.");
		
		if(splittedPath != null && splittedPath.length > 1) {			
			
			List<String> result = new ArrayList<String>();
			
			// iterate through path elements starting with the 2nd element since the first names the variable whereas the second references the first attribute
			// to be accessed via an assigned getter
			for(int i = 1; i < splittedPath.length; i++) {				
				String attrName = splittedPath[i];
				if(attrName != null && !attrName.isEmpty()) {
					result.add("get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1));					
				}
			}
			
			return (String[])result.toArray(EMPTY_STRING_ARRAY);
		}
		
		return EMPTY_STRING_ARRAY;		
	}
	

	/**
	 * Extracts the  {@link Method method representations} for the given path of getter methods 
	 * @param varType
	 * @param getterMethodNames
	 * @param result
	 * @throws ContextVariableEvaluationFailedException
	 */
	protected void extractGetterMethods(Class<?> varType, String[] getterMethodNames, List<Method> result) throws ContextVariableEvaluationFailedException {
		
		if(varType != null && getterMethodNames != null && getterMethodNames.length > 0) {

			String nextGetter = getterMethodNames[0];
			try {
				Method getterMethod = varType.getMethod(nextGetter);
				result.add(getterMethod);
				extractGetterMethods(getterMethod.getReturnType(), (String[])ArrayUtils.subarray(getterMethodNames, 1, getterMethodNames.length), result);
			} catch(NoSuchMethodException e) {
				throw new ContextVariableEvaluationFailedException("No such getter '"+nextGetter+"' for class " + varType.getName());
			}
			
		}

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
