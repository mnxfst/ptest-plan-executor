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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.exception.ContextVariableEvaluationFailedException;

/**
 * Test cases for {@link PTestPlanExecutionContext}
 * @author mnxfst
 * @since 23.04.2012
 */
public class TestPTestPlanExecutionContext {

	@Test
	public void testEvaluateWithNullReplacementPattern() throws ContextVariableEvaluationFailedException {
		try {
			new PTestPlanExecutionContext().evaluate(null);
			Assert.fail("Missing replacement pattern");
		} catch(ContextVariableEvaluationFailedException e) {
			//
		}
	}

	@Test
	public void testEvaluateWithEmptyReplacementPattern() throws ContextVariableEvaluationFailedException {
		try {
			new PTestPlanExecutionContext().evaluate("\t\t\t     \n\n");
			Assert.fail("Missing replacement pattern");
		} catch(ContextVariableEvaluationFailedException e) {
			//
		}
	}

	@Test
	public void testEvaluateWithInvalidReplacementPattern() throws ContextVariableEvaluationFailedException {
		try {
			new PTestPlanExecutionContext().evaluate("dsdsd");
			Assert.fail("Invalid replacement pattern");
		} catch(ContextVariableEvaluationFailedException e) {			
		}
	}

	@Test
	public void testEvaluateWithReplacementPatternReferencingAnUnknownVariable() throws ContextVariableEvaluationFailedException {
		Assert.assertNull(new PTestPlanExecutionContext().evaluate("${dsdsd}"));
	}

	@Test
	public void testEvaluateWithReplacementPatternReferencingValidVariable() throws ContextVariableEvaluationFailedException {
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.addContextVariable("test", "testVar");
		Assert.assertEquals("The value must be " + ctx.getContextVariable("test"), ctx.getContextVariable("test"), ctx.evaluate("${test}"));
		Assert.assertEquals("The value must be " + String.class.getName(), String.class.getName(), ctx.evaluate("${test.class.name}"));
	}

	@Test
	public void testEvaluateObjectWithNullCtxObject() throws ContextVariableEvaluationFailedException {
		Assert.assertNull("The response must be null", new PTestPlanExecutionContext().evaluateObject(null, new ArrayList<Method>()));
	}
	
	@Test
	public void testExtractContextVariableNameFromEmptyPattern() {
		String str = new PTestPlanExecutionContext().extractContextVariableName("${}");
		Assert.assertNotNull("The result string must not be null", str);
		Assert.assertTrue("The string must be empty", str.trim().isEmpty());
	}
	
	@Test
	public void testExtractContextVariableNameFromValidString() {
		String str = new PTestPlanExecutionContext().extractContextVariableName("${a}");
		Assert.assertNotNull("The result string must not be null", str);
		Assert.assertFalse("The string must not be empty", str.trim().isEmpty());
		Assert.assertEquals("The string must contain the character 'a'", "a", str);
	}
	
	@Test
	public void testExtractGetterMethodNamesWithNullPattern() {
		Assert.assertNotNull("The result must not be null", new PTestPlanExecutionContext().extractGetterMethodNames(null));
		Assert.assertTrue("The result must be empty", new PTestPlanExecutionContext().extractGetterMethodNames(null).length == 0);
	}
	
	@Test
	public void testExtractGetterMethodNamesWithEmptyPattern() {
		Assert.assertNotNull("The result must not be null", new PTestPlanExecutionContext().extractGetterMethodNames(""));
		Assert.assertTrue("The result must be empty", new PTestPlanExecutionContext().extractGetterMethodNames("").length == 0);
	}
	
	@Test
	public void testExtractGetterMethodNamesWithValidPatternButNoPath() {		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.addContextVariable("test", "testVar");
		String[] accessPath = ctx.extractGetterMethodNames("${test}");
		Assert.assertNotNull("The path must not be null", accessPath);
		Assert.assertEquals("The size of the result array must be 0", 0, accessPath.length);		
	}
	
	@Test
	public void testExtractGetterMethodNamesWithValidPattern() {		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.addContextVariable("test", "testVar");
		String[] accessPath = ctx.extractGetterMethodNames("${test.name}");
		Assert.assertNotNull("The path must not be null", accessPath);
		Assert.assertEquals("The size of the result array must be 1", 1, accessPath.length);		
	}
	
	@Test
	public void testExtractGetterMethodNamesWithValidPatternAndEmptyAccessElements() {		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		ctx.addContextVariable("test", "testVar");
		String[] accessPath = ctx.extractGetterMethodNames("${test......name}");
		Assert.assertNotNull("The path must not be null", accessPath);
		Assert.assertEquals("The size of the result array must be 1", 1, accessPath.length);		
	}
	
	@Test
	public void testExtractGetterMethodsWithNullClass() throws ContextVariableEvaluationFailedException {
		new PTestPlanExecutionContext().extractGetterMethods(null, new String[]{"class", "name"}, new ArrayList<Method>());
	}
	
	@Test
	public void testExtractGetterMethodsWithNullMethodNames() throws ContextVariableEvaluationFailedException {
		new PTestPlanExecutionContext().extractGetterMethods(String.class, null, new ArrayList<Method>());
	}
	
	@Test
	public void testExtractGetterMethodsWithEmptyMethodNames() throws ContextVariableEvaluationFailedException {
		new PTestPlanExecutionContext().extractGetterMethods(String.class, new String[0], new ArrayList<Method>());
	}
	
	@Test
	public void testExtractGetterMethodsWithInvalidMethodNames() throws ContextVariableEvaluationFailedException {
		try {
			new PTestPlanExecutionContext().extractGetterMethods(String.class, new String[]{"no", "such", "name"}, new ArrayList<Method>());
			Assert.fail("Invalid access path");
		} catch(ContextVariableEvaluationFailedException e) {
			//
		}
	}
	
	@Test
	public void testExtractGetterMethodsWithValidMethodNames() throws ContextVariableEvaluationFailedException {
		List<Method> methods = new ArrayList<Method>();
		new PTestPlanExecutionContext().extractGetterMethods(String.class, new String[]{"getClass", "getName"}, methods);
		Assert.assertFalse("The list of methods must not be empty", methods.isEmpty());
		Assert.assertEquals("The list must contain 2 elements", 2, methods.size());		
	}

}
