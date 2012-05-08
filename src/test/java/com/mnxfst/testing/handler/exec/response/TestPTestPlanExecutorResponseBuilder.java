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

package com.mnxfst.testing.handler.exec.response;

import org.apache.kahadb.util.ByteArrayInputStream;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * @author mnxfst
 * @since 08.05.2012
 */
public class TestPTestPlanExecutorResponseBuilder {

	@Test
	public void testExport() throws InvalidConfigurationException {
		String responseString = PTestPlanExecutorResponseBuilder.export(new PTestPlanExecutionStartedResponse("localhost", 10, 20));
		PTestPlanExecutionStartedResponse responseObject = PTestPlanExecutorResponseBuilder.buildTestplanExecutionStartedResponse(new ByteArrayInputStream(responseString.getBytes()));
		
		System.out.println(responseString);
		System.out.println(PTestPlanExecutorResponseBuilder.export(responseObject));
		
		String tst = "<?xml version=\"1.0\" ?><planExecutor><hostname>localhost</hostname><port>9090</port><responseCode>2</responseCode><errors><entry><string>testplan_parsing_failed</string><string>Failed to parse provided test plan. Error: Failed to parse testplan from provided input stream. Error: ptestplan</string></entry></errors></planExecutor>";
		PTestPlanExecutorResponseBuilder.buildTestplanExecutionStartedResponse(new ByteArrayInputStream(tst.getBytes()));
	}
	
}
