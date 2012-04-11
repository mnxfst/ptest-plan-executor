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
 * @author mnxfst
 * @since 12.04.2012
 */
public class JMSActivity extends AbstractActivity {

	/**
	 * @see com.mnxfst.testing.handler.exec.activity.PTestPlanActivity#configure(java.util.Map, java.util.Map)
	 */
	public void configure(Map<String, String> configuration, Map<String, String> exportVariables) throws InvalidConfigurationException {
	}

	/**
	 * @see com.mnxfst.testing.handler.exec.activity.PTestPlanActivity#execute(int, com.mnxfst.testing.handler.exec.PTestPlanExecutionContext)
	 */
	public void execute(int elementId, PTestPlanExecutionContext contextElement) throws ActivityExecutionFailedException {

	}

}
