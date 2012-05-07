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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanActivitySettings;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanBuilder;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Test case for {@link PTestPlanExecutor}
 * @author mnxfst
 * @since 11.04.2012
 */
public class TestPTestPlanExecutor {

	@Test
	public void testConstructorWithNullTestPlan() throws InvalidConfigurationException {
		try {
			new PTestPlanExecutor(null, "id",  1, 1, 1);
			Assert.fail("Invalid plan");
		} catch(InvalidConfigurationException e) {
			//
		}
	}
	
	@Test
	public void testConstructorWithTestPlanNullName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName(null);
		plan.addActivityConfigOption(new PTestPlanActivitySettings());
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid name");
		} catch(InvalidConfigurationException e) {
		}
	}
	
	@Test
	public void testConstructorWithTestPlanEmptyName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("");
		plan.addActivityConfigOption(new PTestPlanActivitySettings());
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid name");
		} catch(InvalidConfigurationException e) {
		}
	}
	
	@Test
	public void testConstructorWithTestPlanEmptySpacedName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("    \t\t\t\r\r");
		plan.addActivityConfigOption(new PTestPlanActivitySettings());
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid name");
		} catch(InvalidConfigurationException e) {
		}
	}
	
	@Test
	public void testConstructorWithTestPlanMissingActivities() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("name");
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Missing activities");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithNullResultIdentifier() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, null, 1, 1, 1);
			Assert.fail("Invalid identifier");
		} catch(InvalidConfigurationException e) {
		}		
	}

	@Test
	public void testConstructorWithEmptyResultIdentifier() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "", 1, 1, 1);
			Assert.fail("Invalid identifier");
		} catch(InvalidConfigurationException e) {
		}		
	}

	@Test
	public void testConstructorWithSpacedResultIdentifier() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "    \t\t\r", 1, 1, 1);
			Assert.fail("Invalid identifier");
		} catch(InvalidConfigurationException e) {
		}		
	}

	@Test
	public void testConstructorWithInvalidNumOfRuns() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 0, 1, 1);
			Assert.fail("Invalid identifier");
		} catch(InvalidConfigurationException e) {
		}		
	}

	@Test
	public void testConstructorWithInvalidWorkQueueSize() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 0, 1);
			Assert.fail("Invalid work queue size");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithInvalidNumOfWorkerThreads() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 0);
			Assert.fail("Invalid number of worker threads");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithNullClassName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(null);
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid class name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithEmptyClassName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz("");
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid class name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithSpacedClassName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz("   \t\t\r");
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid class name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithUnknownClassName() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz("no such class");
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid class name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithClassOfDifferentType() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(String.class.getName());
		plan.addActivityConfigOption(setting);
		try {
			new PTestPlanExecutor(plan, "id", 1, 1, 1);
			Assert.fail("Invalid class name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConstructorWithValidClass() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		new PTestPlanExecutor(plan, "id", 1, 1, 1);
	}
	
	@Test
	public void testGetMaxRuntimeWithNullInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The max runtime must be 0", 0, executor.getMaxRuntime(null));
	}

	@Test
	public void testGetMaxRuntimeWithEmptyInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The max runtime must be 0", 0, executor.getMaxRuntime(new ArrayList<Long>()));
	}
	
	@Test
	public void testGetMaxRuntimeWithValidInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		
		List<Long> runtimes = new ArrayList<Long>();
		runtimes.add(Long.valueOf(0));
		runtimes.add(Long.valueOf(12));
		runtimes.add(Long.valueOf(2));
		runtimes.add(Long.valueOf(22));
		runtimes.add(Long.valueOf(100));
		runtimes.add(Long.valueOf(1));
		Collections.sort(runtimes);
		Assert.assertEquals("The max runtime must be 100", 100, executor.getMaxRuntime(runtimes));
	}
	
	@Test
	public void testGetMinRuntimeWithNullInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The min runtime must be 0", 0, executor.getMinRuntime(null));
	}

	@Test
	public void testGetMinRuntimeWithEmptyInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The min runtime must be 0", 0, executor.getMinRuntime(new ArrayList<Long>()));
	}

	@Test
	public void testGetMinRuntimeWithValidInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);

		List<Long> runtimes = new ArrayList<Long>();
		runtimes.add(Long.valueOf(12));
		runtimes.add(Long.valueOf(2));
		runtimes.add(Long.valueOf(22));
		runtimes.add(Long.valueOf(100));
		runtimes.add(Long.valueOf(1));
		Collections.sort(runtimes);
		Assert.assertEquals("The min runtime must be 1", 1, executor.getMinRuntime(runtimes));
	}

	@Test
	public void testGetAverageRuntimeWithNullInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The average runtime must be 0", 0.0, executor.getAverageRuntime(null), 0);
	}

	@Test
	public void testGetAverageRuntimeWithEmptyInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The average runtime must be 0", 0, executor.getAverageRuntime(new ArrayList<Long>()), 0);
	}

	@Test
	public void testGetAverageRuntimeWithValidInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);

		List<Long> runtimes = new ArrayList<Long>();
		runtimes.add(Long.valueOf(12));
		runtimes.add(Long.valueOf(2));
		runtimes.add(Long.valueOf(22));
		runtimes.add(Long.valueOf(100));
		runtimes.add(Long.valueOf(1));
		Collections.sort(runtimes);
		Assert.assertEquals("The average runtime must be 27,4", 27.4, executor.getAverageRuntime(runtimes), 0);
	}

	@Test
	public void testGetMedianRuntimeWithNullInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The median runtime must be 0", 0, executor.getMedianRuntime(null), 0);
	}

	@Test
	public void testGetMedianRuntimeWithEmptyInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The median runtime must be 0", 0, executor.getMedianRuntime(new ArrayList<Long>()), 0);
	}

	@Test
	public void testGetMedianRuntimeWithValidInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);

		List<Long> runtimes = new ArrayList<Long>();
		runtimes.add(Long.valueOf(1));
		runtimes.add(Long.valueOf(2));
		runtimes.add(Long.valueOf(12));
		runtimes.add(Long.valueOf(22));
		runtimes.add(Long.valueOf(100));
		Collections.sort(runtimes);
		Assert.assertEquals("The median runtime must be 12", 12, executor.getMedianRuntime(runtimes), 0);
	}

	@Test
	public void testGetTotalRuntimeWithNullInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The min runtime must be 0", 0, executor.getTotalRuntime(null));
	}

	@Test
	public void testGetTotalRuntimeWithEmptyInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);
		Assert.assertEquals("The min runtime must be 0", 0, executor.getTotalRuntime(new ArrayList<Long>()));
	}

	@Test
	public void testGetTotalRuntimeWithValidInput() throws InvalidConfigurationException {
		PTestPlan plan = new PTestPlan();
		plan.setName("test");
		PTestPlanActivitySettings setting = new PTestPlanActivitySettings();
		setting.setClazz(TestDummyActivity.class.getName());
		setting.setDescription("test-dummy activity");
		setting.setId("test-dummy");
		plan.addActivityConfigOption(setting);
		PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 1, 1, 1);

		List<Long> runtimes = new ArrayList<Long>();
		runtimes.add(Long.valueOf(12));
		runtimes.add(Long.valueOf(2));
		runtimes.add(Long.valueOf(22));
		runtimes.add(Long.valueOf(100));
		runtimes.add(Long.valueOf(1));
		Collections.sort(runtimes);
		Assert.assertEquals("The total runtime must be 137", 137, executor.getTotalRuntime(runtimes));
	}
	
	@Test
	public void testRunJMSSample() throws InvalidConfigurationException, FileNotFoundException {
		try {
			ExecutorService testPlanExecutorService = Executors.newCachedThreadPool();
			PTestPlan plan = PTestPlanBuilder.build(new FileInputStream("src/test/resources/plan/jms-sample-plan.xml"));
			PTestPlanExecutor executor = new PTestPlanExecutor(plan, "id", 2000, 2000, 8);
			testPlanExecutorService.execute(executor);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
