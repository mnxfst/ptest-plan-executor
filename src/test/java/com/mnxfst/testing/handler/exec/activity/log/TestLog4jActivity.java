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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Test case for {@link Log4jActivity}
 * @author mnxfst
 * @since 07.05.2012
 */
public class TestLog4jActivity {

	@Test
	public void testConfigureWithNullConfiguration() throws InvalidConfigurationException {
		Map<String, String> expVars = new HashMap<String, String>();
		expVars.put("test", "test-1");		
		try {
			(new Log4jActivity()).configure(null, expVars);
			Assert.fail("Invalid configuration");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConfigureWithEmptyConfiguration() throws InvalidConfigurationException {
		Map<String, String> expVars = new HashMap<String, String>();
		expVars.put("test", "test-1");		
		try {
			(new Log4jActivity()).configure(new HashMap<String, String>(), expVars);
			Assert.fail("Invalid configuration");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConfigureWithMissingLogTemplate() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("test", "test-1");		
		try {
			(new Log4jActivity()).configure(cfg, new HashMap<String, String>());
			Assert.fail("Invalid/missing log template");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConfigureWithNullLogTemplate() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("test", "test-1");	
		cfg.put(Log4jActivity.CFG_LOG_MESSAGE_TEMPLATE, null);
		try {
			(new Log4jActivity()).configure(cfg, new HashMap<String, String>());
			Assert.fail("Invalid/missing log template");
		} catch(InvalidConfigurationException e) {			
			//
		}
	}

	@Test
	public void testConfigureWithEmptyLogTemplate() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("test", "test-1");		
		cfg.put(Log4jActivity.CFG_LOG_MESSAGE_TEMPLATE, "");
		try {
			(new Log4jActivity()).configure(cfg, new HashMap<String, String>());
			Assert.fail("Invalid/missing log template");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConfigureWithEmptySpacedLogTemplate() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("test", "test-1");		
		cfg.put(Log4jActivity.CFG_LOG_MESSAGE_TEMPLATE, "\t\t\n\n\t\r");
		try {
			(new Log4jActivity()).configure(cfg, new HashMap<String, String>());
			Assert.fail("Invalid/missing log template");
		} catch(InvalidConfigurationException e) {
			//
		}
	}
	
}
