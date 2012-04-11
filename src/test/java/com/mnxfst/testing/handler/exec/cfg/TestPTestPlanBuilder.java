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

import org.apache.kahadb.util.ByteArrayInputStream;
import org.junit.Assert;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanActivitySettings;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanBuilder;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanConfigurationOption;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Test case for {@link PTestPlanBuilder}
 * @author mnxfst
 * @since 10.04.2012
 */
public class TestPTestPlanBuilder {

	@Test
	public void testBuild() throws InvalidConfigurationException {
		
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
		
		PTestPlanActivitySettings activityOpt = new PTestPlanActivitySettings();
		activityOpt.addConfigOption("set-1", "val-1");
		activityOpt.addConfigOption("set-2", "val-2");
		activityOpt.setClazz("test-class");
		activityOpt.setDescription("test class description");
		activityOpt.addExportVariable("testInternal", "junitSampleVar");
		plan.addActivityConfigOption(activityOpt);

		String xml = PTestPlanBuilder.export(plan);
		Assert.assertNotNull("The result must not be null", xml);
		Assert.assertFalse("The result must not be empty", xml.isEmpty());
		
		PTestPlan recoveredPlan = PTestPlanBuilder.build(new ByteArrayInputStream(xml.getBytes()));
		String recoveredPlanXml = PTestPlanBuilder.export(recoveredPlan);
		Assert.assertEquals("The original xml and the xml of the recovered plan must be equal", xml, recoveredPlanXml);
		// TODO write a sufficient equals method
		System.out.println(recoveredPlanXml);
		
		
	}
	
}
