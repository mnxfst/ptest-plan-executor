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

package com.mnxfst.testing.handler.exec.exception;

import com.mnxfst.testing.handler.exec.PTestPlanProcessor;


/**
 * Thrown by the {@link PTestPlanProcessor} in case a specific state has already been visited during a
 * single run. This should prevent the caller from defining cycles / loops
 * @author mnxfst
 * @since 11.04.2012
 */
public class StateAlreadyVisitedException extends Exception {

	private static final long serialVersionUID = 8545772768554826449L;

	public StateAlreadyVisitedException() {
	}

	public StateAlreadyVisitedException(String msg) {
		super(msg);
	}

	public StateAlreadyVisitedException(Throwable cause) {
		super(cause);
	}

	public StateAlreadyVisitedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
