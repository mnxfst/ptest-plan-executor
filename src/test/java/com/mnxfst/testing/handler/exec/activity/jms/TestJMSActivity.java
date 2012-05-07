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

package com.mnxfst.testing.handler.exec.activity.jms;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mnxfst.testing.handler.exec.PTestPlanExecutionContext;
import com.mnxfst.testing.handler.exec.exception.ActivityExecutionFailedException;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Test cases for {@link JMSActivity}
 * @author mnxfst
 * @since 13.04.2012
 */
public class TestJMSActivity {

	@Test
	public void testConfigureWithNullConfigurationMap() throws InvalidConfigurationException {
		
		Map<String, String> expVars = new HashMap<String, String>();
		expVars.put("var1", "exp1");
		try {
			new JMSActivity().configure(null, expVars);
			Assert.fail("Invalid configuration map");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithEmptyConfigurationMap() throws InvalidConfigurationException {
		
		Map<String, String> expVars = new HashMap<String, String>();
		expVars.put("var1", "exp1");
		try {
			new JMSActivity().configure(new HashMap<String, String>(), expVars);
			Assert.fail("Invalid configuration map");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithMissingDestinationName() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing destination name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithNullDestinationName() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing destination name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithEmptyDestinationName() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "   \n\n\t\t");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing destination name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithMissingTemplate() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing template");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithNullTemplate() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", null);
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing template");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithEmptyTemplate() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "     \t\t\n");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing template name");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithMissingConnectionFactory() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing connection factory");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithNullConnectionFactory() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", null);
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing connection factory");
		} catch(InvalidConfigurationException e) {
			//
		}		
	}

	@Test
	public void testConfigureWithEmptyConnectionFactory() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "\t\t\t    \n");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing connection factory");
		} catch(InvalidConfigurationException e) {
		}		
	}


	@Test
	public void testConfigureWithMissingConnectionFactoryLookUpName() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing connection factory lookup name");
		} catch(InvalidConfigurationException e) {
		}		
	}

	@Test
	public void testConfigureWithEmptyConnectionFactoryLookUpName() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "    \n\n\t");
		cfg.put("providerUrl", "sampleProviderUrl");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing connection factory lookup name");
		} catch(InvalidConfigurationException e) {
		}		
	}

	@Test
	public void testConfigureWithMissingProviderUrl() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	@Test
	public void testConfigureWithNullProviderUrl() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", null);
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	@Test
	public void testConfigureWithEmptyProviderUrl() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("deliveryMode", "sampleDeliveryMode");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("providerUrl", "    \t\t\n");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	@Test
	public void testConfigureWithMissingDeliveryMode() throws InvalidConfigurationException {
		
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		cfg.put("providerUrl", "sampleProviderUrl");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	@Test
	public void testConfigureWithNullDeliveryMode() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("deliveryMode", null);
		cfg.put("destinationName", "sampleDestination");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		cfg.put("providerUrl", "sampleProviderUrl");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	@Test
	public void testConfigureWithEmptyDeliveryMode() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("deliveryMode", "\t\t    \n\n");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		cfg.put("providerUrl", "sampleProviderUrl");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	@Test
	public void testConfigureWithInvalidDeliveryMode() throws InvalidConfigurationException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("deliveryMode", "testMode");
		cfg.put("destinationName", "sampleDestination");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "sampleConnectionFactoryClass");
		cfg.put("connectionFactoryLookupName", "sampleConnectionFactoryLookupName");
		cfg.put("principal", "samplePrincipal");
		cfg.put("credential", "sampleCredentials");
		cfg.put("providerUrl", "sampleProviderUrl");
		try {
			new JMSActivity().configure(cfg, null);
			Assert.fail("Missing provider url");
		} catch(InvalidConfigurationException e) {
		}		
	}
	
	
	public void testConfigureWithValidConfiguration() throws InvalidConfigurationException, ActivityExecutionFailedException {
		Map<String, String> cfg = new HashMap<String, String>();
		cfg.put("payloadTemplate", "sampleTemplate");
		cfg.put("deliveryMode", "non_persistent");
		cfg.put("destinationName", "testTopic");
		cfg.put("vendor.config.topic.testTopic", "test.topic");
		cfg.put("clientId", "sampleClientId");
		cfg.put("connectionFactoryClass", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		cfg.put("connectionFactoryLookupName", "ConnectionFactory");
		cfg.put("principal", "");
		cfg.put("credential", "");
		cfg.put("providerUrl", "tcp://localhost:61616");
		JMSActivity activity = new JMSActivity();
		activity.configure(cfg, null);
		
		PTestPlanExecutionContext ctx = new PTestPlanExecutionContext();
		long start = System.currentTimeMillis();
		int msgCount = 0;
		int millis = 8000;
		while(System.currentTimeMillis() - start < millis) {
			activity.execute(1, ctx);
			msgCount = msgCount + 1;
		}
		
		System.out.println(msgCount / millis + " messages per millis. Per second: " + (msgCount / millis)*1000);
			
	}
	
	
}
