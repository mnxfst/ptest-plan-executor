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

package com.mnxfst.testing.handler.exec.cfg;

import java.util.Date;

import org.junit.Test;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanActivitySettings;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanConfigurationOption;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Test cases for {@link PTestPlan
 * @author mnxfst
 * @since 02.04.2012
 */
public class TestPTestPlan {

	@Test
	public void testExportXML() {
		XStream xstream = new XStream(new StaxDriver());
		xstream.autodetectAnnotations(true);
		
		PTestPlan plan = new PTestPlan();
		plan.setCreatedBy("mnxfst");
		plan.setCreationDate(new Date());
		plan.setDescription("test description");		
		plan.setName("my-test-plan");
		
		PTestPlanConfigurationOption options = new PTestPlanConfigurationOption();
		options.setId("id-1");
		options.addOption("test-1", "value-1");
		options.addOption("test-2", "value-2");		
		plan.addGlobalCfgOption(options);
		
		options = new PTestPlanConfigurationOption();
		options.setId("id-2");
		options.addOption("test-3", "value-3");
		options.addOption("test-4", "value-4");		
		plan.addGlobalCfgOption(options);
		
		options = new PTestPlanConfigurationOption();
		options.setId("activity-1");
		options.addOption("set-1", "val-1");
		PTestPlanActivitySettings activityOpt = new PTestPlanActivitySettings();
		activityOpt.addConfigOption("set-1", "val-1");
		activityOpt.setClazz("test-class");
		activityOpt.setDescription("test class description");
		plan.addActivityConfigOption(activityOpt);
		
		xstream.fromXML(xstream.toXML(plan));
		
		
	}
	
}
