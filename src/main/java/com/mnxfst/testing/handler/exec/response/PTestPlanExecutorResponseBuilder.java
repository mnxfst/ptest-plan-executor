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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanActivitySettings;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanConfigurationOption;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Executor response builder
 * @author mnxfst
 * @since 08.05.2012
 */
public class PTestPlanExecutorResponseBuilder {

	private static XStream xstream = null;
	
	static {
		xstream = new XStream(new StaxDriver());
		xstream.autodetectAnnotations(true);
		xstream.alias("planExecutor", PTestPlanExecutionStartedResponse.class);
		xstream.alias("ptestplan", PTestPlan.class);
		xstream.alias("activity", PTestPlanActivitySettings.class);
		xstream.alias("configuration", PTestPlanConfigurationOption.class);
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws InvalidConfigurationException
	 */
	public static PTestPlanExecutionStartedResponse buildTestplanExecutionStartedResponse(InputStream inputStream) throws InvalidConfigurationException {
		
		if(inputStream == null)
			throw new InvalidConfigurationException("Missing required input stream containing a test plan");
		
		try {

			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			int c = 0;
			while((c = inputStream.read()) != -1)
				bOut.write(c);

			String cnt = new String(bOut.toByteArray(), "UTF-8");
			System.out.println(cnt);
			
			
			PTestPlanExecutionStartedResponse response = (PTestPlanExecutionStartedResponse)xstream.fromXML(new StringReader(cnt));
			return response;
		} catch(Exception e) {
			e.printStackTrace();
			throw new InvalidConfigurationException("Failed to parse response from provided input stream. Error: " + e.getMessage());
		}
	}
	
	/**
	 * Converts the provided response into its string representation 
	 * @param ptestPlan
	 * @return
	 * @throws IOException
	 * @throws InvalidConfigurationException
	 */
	public static String export(PTestPlanExecutionStartedResponse response) throws InvalidConfigurationException {
		
		if(response == null)
			throw new InvalidConfigurationException("Missing required response");
		
		return xstream.toXML(response);
	}
		

}
