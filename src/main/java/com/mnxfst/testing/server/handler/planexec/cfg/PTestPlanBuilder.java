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

package com.mnxfst.testing.server.handler.planexec.cfg;

import java.io.IOException;
import java.io.InputStream;

import com.mnxfst.testing.server.handler.planexec.exception.InvalidTestPlanConfigurationException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Test plan parser
 * @author mnxfst
 * @since 02.04.2012
 */
public class PTestPlanBuilder {

	private static XStream xstream = null;
	
	static {
		xstream = new XStream(new StaxDriver());
		xstream.autodetectAnnotations(true);
	}
	
	/**
	 * Builds a test plan from the contents of the provided input stream 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws InvalidTestPlanConfigurationException
	 */
	public static PTestPlan build(InputStream inputStream) throws InvalidTestPlanConfigurationException {
		
		if(inputStream == null)
			throw new InvalidTestPlanConfigurationException("Missing required input stream containing a test plan");
		
		try {
			PTestPlan plan = (PTestPlan)xstream.fromXML(inputStream);
			return plan;
		} catch(Exception e) {
			throw new InvalidTestPlanConfigurationException("Failed to parse testplan from provided input stream. Error: " + e.getMessage());
		}
	}
	
	/**
	 * Converts the provided test plan into its xml representation 
	 * @param ptestPlan
	 * @return
	 * @throws IOException
	 * @throws InvalidTestPlanConfigurationException
	 */
	public static String export(PTestPlan ptestPlan) throws InvalidTestPlanConfigurationException {
		
		if(ptestPlan == null)
			throw new InvalidTestPlanConfigurationException("Missing required test plan");
		
		return xstream.toXML(ptestPlan);
	}
		
}
