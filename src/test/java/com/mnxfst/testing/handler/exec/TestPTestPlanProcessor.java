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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.activity.PTestPlanActivity;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;
import com.mnxfst.testing.handler.exec.exception.StateAlreadyVisitedException;

/**
 * Test case for {@link PTestPlanProcessor}
 * @author mnxfst
 * @since 11.04.2012
 */
public class TestPTestPlanProcessor {
	
	@Test
	public void testConstructorWithNullWorkerId() throws InvalidConfigurationException {
		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("nextState", new TestDummyActivity());		
		try {
			new PTestPlanProcessor(null, ctx, activities);
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithEmptyWorkerId() throws InvalidConfigurationException {
		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("nextState", new TestDummyActivity());		
		try {
			new PTestPlanProcessor("", ctx, activities);
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithEmptyWorkerIdHavingSpaces() throws InvalidConfigurationException {
		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("nextState", new TestDummyActivity());		
		try {
			new PTestPlanProcessor("     \t\t\t\r", ctx, activities);
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithNullContext() throws InvalidConfigurationException {

		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("nextState", new TestDummyActivity());		
		try {
			new PTestPlanProcessor("worker1", null, activities);
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithEmptyContext() throws InvalidConfigurationException {

		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("nextState", new TestDummyActivity());		
		try {
			new PTestPlanProcessor("worker1", new PTestPlanExecutionContext(), activities);
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithNullActivities() throws InvalidConfigurationException {
		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		try {
			new PTestPlanProcessor("worker1", ctx, null);
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithEmptyActivities() throws InvalidConfigurationException {
		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		try {
			new PTestPlanProcessor("worker1", ctx, new HashMap<String, PTestPlanActivity>());
			Assert.fail("Invalid worker identifier");
		} catch(InvalidConfigurationException e) {
			//
		}
	}

	@Test
	public void testConstructorWithValidValues() throws InvalidConfigurationException {
		
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("nextState", new TestDummyActivity());		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		new PTestPlanProcessor("worker1", ctx, activities);
	}

	@Test
	public void testCallWithMissingNextStateActivity() throws Exception {
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put("firstState", new TestDummyActivity());		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId("nextState");
		PTestPlanProcessor processor = new PTestPlanProcessor("worker1", ctx, activities);
		Assert.assertEquals("The number of activities called must be 0", 0, ctx.getActionCalls());
		Assert.assertEquals("The initial timestamp must be 0", 0, ctx.getInitTimestamp());
		Assert.assertEquals("The final timestamp must be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNull("The current state attribute value must be null", ctx.getCurrentActivityId());
		processor.call();
		Assert.assertEquals("The number of activities called must be 0", 0, ctx.getActionCalls());
		Assert.assertNotSame("The initial timestamp must not be 0", 0, ctx.getInitTimestamp());
		Assert.assertNotSame("The final timestamp must not be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNull("The current state attribute value must be null", ctx.getCurrentActivityId());
	}

	@Test
	public void testCallWithOnlyOneState() throws Exception {
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put(PTestPlanProcessor.FINAL_STATE, new TestDummyActivity(PTestPlanProcessor.FINAL_STATE, "testType", PTestPlanProcessor.FINAL_STATE, "testDescription", "junit-test-value"));		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId(PTestPlanProcessor.FINAL_STATE);
		PTestPlanProcessor processor = new PTestPlanProcessor("worker1", ctx, activities);
		Assert.assertEquals("The number of activities called must be 0", 0, ctx.getActionCalls());
		Assert.assertEquals("The initial timestamp must be 0", 0, ctx.getInitTimestamp());
		Assert.assertEquals("The final timestamp must be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNull("The current state attribute value must be null", ctx.getCurrentActivityId());
		processor.call();
		Assert.assertEquals("The number of activities called must be 1", 1, ctx.getActionCalls());
		Assert.assertNotSame("The initial timestamp must not be 0", 0, ctx.getInitTimestamp());
		Assert.assertNotSame("The final timestamp must not be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNotNull("The current state attribute value must not be null", ctx.getCurrentActivityId());
		Assert.assertEquals("The current state attribute value must be '"+PTestPlanProcessor.FINAL_STATE+"'", PTestPlanProcessor.FINAL_STATE, ctx.getCurrentActivityId());
		Assert.assertEquals("The value of context attribute '"+TestDummyActivity.DUMMY_CONTEXT_VARIABLE+"' must be 'junit-test-value'", "junit-test-value", ctx.getContextVariable(TestDummyActivity.DUMMY_CONTEXT_VARIABLE));
	}

	@Test
	public void testCallWithCycle() throws Exception {
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put(PTestPlanProcessor.INIT_STATE, new TestDummyActivity(PTestPlanProcessor.INIT_STATE, "testType", PTestPlanProcessor.INIT_STATE, "testDescription", "junit-test-value"));		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId(PTestPlanProcessor.INIT_STATE); // ensure that there is no FINAL state thus there MUST be a cycle
		PTestPlanProcessor processor = new PTestPlanProcessor("worker1", ctx, activities);
		Assert.assertEquals("The number of activities called must be 0", 0, ctx.getActionCalls());
		Assert.assertEquals("The initial timestamp must be 0", 0, ctx.getInitTimestamp());
		Assert.assertEquals("The final timestamp must be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNull("The current state attribute value must be null", ctx.getCurrentActivityId());
		try {
			processor.call();
			Assert.fail("State already visited");
		} catch(StateAlreadyVisitedException e) {
			//
		}
	}


	@Test
	public void testCallWithTwoStates() throws Exception {
		Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
		activities.put(PTestPlanProcessor.INIT_STATE, new TestDummyActivity(PTestPlanProcessor.INIT_STATE, "testType", PTestPlanProcessor.FINAL_STATE, "testDescription", "junit-test-value"));		
		activities.put(PTestPlanProcessor.FINAL_STATE, new TestDummyActivity(PTestPlanProcessor.FINAL_STATE, "testType", PTestPlanProcessor.FINAL_STATE, "testDescription", "junit-test-value"));		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.setNextActivityId(PTestPlanProcessor.INIT_STATE);
		PTestPlanProcessor processor = new PTestPlanProcessor("worker1", ctx, activities);
		Assert.assertEquals("The number of activities called must be 0", 0, ctx.getActionCalls());
		Assert.assertEquals("The initial timestamp must be 0", 0, ctx.getInitTimestamp());
		Assert.assertEquals("The final timestamp must be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNull("The current state attribute value must be null", ctx.getCurrentActivityId());
		processor.call();
		Assert.assertEquals("The number of activities called must be 2", 2, ctx.getActionCalls());
		Assert.assertNotSame("The initial timestamp must not be 0", 0, ctx.getInitTimestamp());
		Assert.assertNotSame("The final timestamp must not be 0", 0, ctx.getFinalTimestamp());
		Assert.assertNotNull("The current state attribute value must not be null", ctx.getCurrentActivityId());
		Assert.assertEquals("The current state attribute value must be '"+PTestPlanProcessor.FINAL_STATE+"'", PTestPlanProcessor.FINAL_STATE, ctx.getCurrentActivityId());
		Assert.assertEquals("The value of context attribute '"+TestDummyActivity.DUMMY_CONTEXT_VARIABLE+"' must be 'junit-test-value'", "junit-test-value", ctx.getContextVariable(TestDummyActivity.DUMMY_CONTEXT_VARIABLE));
	}
	
}

