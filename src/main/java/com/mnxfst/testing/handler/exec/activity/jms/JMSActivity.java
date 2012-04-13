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
import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.mnxfst.testing.handler.exec.PTestPlanExecutionContext;
import com.mnxfst.testing.handler.exec.activity.AbstractActivity;
import com.mnxfst.testing.handler.exec.exception.ActivityExecutionFailedException;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * @author mnxfst
 * @since 12.04.2012
 */
public class JMSActivity extends AbstractActivity {

	/** logging facility */
	public static final Logger logger = Logger.getLogger(JMSActivity.class);
	
	public static final String CFG_PAYLOAD_TEMPLATE = "payloadTemplate";
	public static final String CFG_DESTINATION_NAME = "destinationName";
	public static final String CFG_DELIVERY_MODE = "deliveryMode";
	public static final String CFG_CLIENT_ID = "clientId";
	
	public static final String CFG_JNDI_CONNECTION_FACTORY_CLASS = "connectionFactoryClass";
	public static final String CFG_JNDI_CONNECTION_FACTORY_LOOKUP_NAME = "connectionFactoryLookupName";
	public static final String CFG_JNDI_PROVIDER_URL_LOOKUP_NAME = "providerUrl";
	public static final String CFG_JNDI_SECURITY_PRINCIPAL_LOOKUP_NAME = "principal";
	public static final String CFG_JNDI_SECURITY_CREDENTIALS_LOOKUP_NAME = "credentials";
	public static final String CFG_JNDI_VENDOR_SPECIFIC_PREFIX = "vendor.config.";
	
	public static final String DELIVERY_MODE_PERSISTENT = "persistent";
	public static final String DELIVERY_MODE_NON_PERSISTENT = "non_persistent";

	public static final String CFG_EXP_VAR_PAYLOAD = "jmsPayload";
	public static final String CFG_EXP_VAR_EXCEPTION = "jmsException";
		
	/** destination name, eg. myTopic or myQueue */
	private String destinationName = null;
	/** jndi context */
	private InitialContext initialJNDIContext = null;
	/** connection factory */
	private ConnectionFactory jmsConnectionFactory = null;
	/** maintains the jms connection */
	private Connection jmsConnection = null;
	/** holds the jms session */
	private Session jmsSession = null;
	/** jms destination used for delivering messages to */
	private Destination jmsDestination = null;
	/** message producer */
	private MessageProducer jmsMessageProducer = null;
	/** payload template */
	private String jmsMessageTemplate = null;
	/** holds the identified payload variables */
	private Map<String, String> jmsPayloadVariables = new HashMap<String, String>();
	
	/**
	 * @see com.mnxfst.testing.handler.exec.activity.PTestPlanActivity#configure(java.util.Map, java.util.Map)
	 */
	public void configure(Map<String, String> configuration, Map<String, String> exportVariables) throws InvalidConfigurationException {
		
		if(configuration == null || configuration.isEmpty())
			throw new InvalidConfigurationException("Required configuration set is either null or empty");
		
		this.destinationName = (String)configuration.get(CFG_DESTINATION_NAME);
		if(this.destinationName == null || this.destinationName.trim().isEmpty())
			throw new InvalidConfigurationException("Missing required destination name for activity '"+getActivityId()+"'");

		this.jmsMessageTemplate = (String)configuration.get(CFG_PAYLOAD_TEMPLATE);
		if(jmsMessageTemplate == null || jmsMessageTemplate.trim().isEmpty())
			throw new InvalidConfigurationException("Required payload template not provided for activity '"+getActivityId()+"'");		

		this.jmsPayloadVariables = getContextVariablesFromString(jmsMessageTemplate);
		
		String connectionFactoryClass = (String)configuration.get(CFG_JNDI_CONNECTION_FACTORY_CLASS);
		if(connectionFactoryClass == null || connectionFactoryClass.trim().isEmpty())
			throw new InvalidConfigurationException("Missing required connection factory class for activity '"+getActivityId()+"'");
		
		String connectionFactoryLookupName = (String)configuration.get(CFG_JNDI_CONNECTION_FACTORY_LOOKUP_NAME);
		if(connectionFactoryLookupName == null || connectionFactoryLookupName.trim().isEmpty())
			throw new InvalidConfigurationException("Missing required connection factory lookup name for activity '"+getActivityId()+"'");
		
		String providerUrl = (String)configuration.get(CFG_JNDI_PROVIDER_URL_LOOKUP_NAME);
		if(providerUrl == null || providerUrl.trim().isEmpty())
			throw new InvalidConfigurationException("Missing required provider url for activity '"+getActivityId()+"'");
		
		String clientId = (String)configuration.get(CFG_CLIENT_ID);		
		String securityPrincipal = (String)configuration.get(CFG_JNDI_SECURITY_PRINCIPAL_LOOKUP_NAME);
		String securityCredentials = (String)configuration.get(CFG_JNDI_SECURITY_CREDENTIALS_LOOKUP_NAME);
		String deliveryMode = (String) configuration.get(CFG_DELIVERY_MODE);
		
		Hashtable<String, String> jndiEnvironment = new Hashtable<String, String>();
		jndiEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, connectionFactoryClass);
		jndiEnvironment.put(Context.PROVIDER_URL, providerUrl);
		
		if(securityPrincipal != null && !securityPrincipal.trim().isEmpty())
			jndiEnvironment.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		if(securityCredentials != null && !securityCredentials.trim().isEmpty())
			jndiEnvironment.put(Context.SECURITY_CREDENTIALS, securityCredentials);

		StringBuffer additionalJndiProps = new StringBuffer();
		// add additional vendor specific configuration options
		for(String cfgKey : configuration.keySet()) {
			if(cfgKey.startsWith(CFG_JNDI_VENDOR_SPECIFIC_PREFIX)) {
				String value = (String)configuration.get(cfgKey);
				jndiEnvironment.put(cfgKey.substring(CFG_JNDI_VENDOR_SPECIFIC_PREFIX.length()), value);
				additionalJndiProps.append(", ").append(cfgKey.substring(CFG_JNDI_VENDOR_SPECIFIC_PREFIX.length())).append("=").append(value);
			}
		}
		
	}

	/**
	 * @see com.mnxfst.testing.handler.exec.activity.PTestPlanActivity#execute(int, com.mnxfst.testing.handler.exec.PTestPlanExecutionContext)
	 */
	public void execute(int elementId, PTestPlanExecutionContext contextElement) throws ActivityExecutionFailedException {

	}

}
