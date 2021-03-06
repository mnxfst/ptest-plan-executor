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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holds a mapping from a replacement pattern towards a context variable
 * @author mnxfst
 * @since 23.04.2012
 */
public class PTestPlanExecutionContextReplacementPattern implements Serializable {

	private static final long serialVersionUID = 7315419676794540981L;

	/** replacement pattern */
	private String replacementPattern = null;
	
	/** name of variable the pattern must be applied to */
	private String name = null;
	
	/** list of getter methods along the pattern path */
	private List<Method> accessMethods = new ArrayList<Method>();

	/**
	 * Default constructor
	 */
	public PTestPlanExecutionContextReplacementPattern() {		
	}
	
	/**
	 * Initializes the instance using the provided values
	 * @param replacementPattern
	 * @param variableName
	 */
	public PTestPlanExecutionContextReplacementPattern(String replacementPattern, String variableName) {
		this.replacementPattern = replacementPattern;
		this.name = variableName;
	}

	public void addAccessMethod(Method method) {
		this.accessMethods.add(method);
	}

	/**
	 * @return the replacementPattern
	 */
	public String getReplacementPattern() {
		return replacementPattern;
	}

	/**
	 * @param replacementPattern the replacementPattern to set
	 */
	public void setReplacementPattern(String replacementPattern) {
		this.replacementPattern = replacementPattern;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the accessMethods
	 */
	public List<Method> getAccessMethods() {
		return accessMethods;
	}

	/**
	 * @param accessMethods the accessMethods to set
	 */
	public void setAccessMethods(List<Method> accessMethods) {
		this.accessMethods = accessMethods;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("replacementPattern", this.replacementPattern)
				.append("name", this.name)
				.append("accessMethods", this.accessMethods).toString();
	}
	
}
