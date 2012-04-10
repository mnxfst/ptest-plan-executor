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

package com.mnxfst.testing.server.handler.planexec.exception;

/**
 * Thrown in case a supposed test plan does not contain the expected information set 
 * @author mnxfst
 * @since 10.04.2012
 */
public class InvalidTestPlanConfigurationException extends Exception {

	private static final long serialVersionUID = 2850947133773091598L;

	public InvalidTestPlanConfigurationException() {
	}

	public InvalidTestPlanConfigurationException(String msg) {
		super(msg);
	}

	public InvalidTestPlanConfigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidTestPlanConfigurationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
