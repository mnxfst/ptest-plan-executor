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

package com.mnxfst.testing.handler.exec.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;

import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;
import com.mnxfst.testing.server.cli.CommandLineOption;
import com.mnxfst.testing.server.cli.CommandLineProcessor;

/**
 * @author mnxfst
 * @since 08.05.2012
 */
public class PTestPlanExecutorClient {

	public static final String CMD_OPT_MODE_EXECUTE = "execute";
	public static final String CMD_OPT_MODE_EXECUTE_SHORT = "exec";
	public static final String CMD_OPT_THREADS = "threads";
	public static final String CMD_OPT_THREADS_SHORT = "t";
	public static final String CMD_OPT_RECURRENCES = "recurrences";
	public static final String CMD_OPT_RECURRENCES_SHORT = "r";
	public static final String CMD_OPT_RECURRENCE_TYPE = "recurrenceType";
	public static final String CMD_OPT_RECURRENCE_TYPE_SHORT = "rt";
	public static final String CMD_OPT_TESTPLAN = "testPlan";
	public static final String CMD_OPT_TESTPLAN_SHORT = "tp";
	public static final String CMD_OPT_PTEST_SERVER_HOSTS = "ptestHosts";
	public static final String CMD_OPT_PTEST_SERVER_HOSTS_SHORT = "h";
	public static final String CMD_OPT_PTEST_SERVER_PORT = "ptestPort";
	public static final String CMD_OPT_PTEST_SERVER_PORT_SHORT = "p";

	public static final String CLI_VALUE_MAP_KEY_EXECUTE = "executeMode";
	public static final String CLI_VALUE_MAP_KEY_THREADS = "threads";
	public static final String CLI_VALUE_MAP_KEY_RECURRENCES = "recurrences";
	public static final String CLI_VALUE_MAP_KEY_RECURRENCE_TYPE = "recurrenceType";
	public static final String CLI_VALUE_MAP_KEY_TESTPLAN = "testplan";
	public static final String CLI_VALUE_MAP_KEY_SERVER_HOSTS = "serverHosts";
	public static final String CLI_VALUE_MAP_KEY_SERVER_PORT = "serverPort";
	
	public static final String REQUEST_PARAMETER_EXECUTE = "execute";
	public static final String REQUEST_PARAMETER_THREADS = "threads";
	public static final String REQUEST_PARAMETER_RECURRENCES = "recurrences";
	public static final String REQUEST_PARAMETER_RECURRENCE_TYPE = "recurrencetype";
	public static final String REQUEST_PARAMETER_TESTPLAN = "testplan";

	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		PTestPlanExecutorClient client = new PTestPlanExecutorClient();
		client.executeClient(args);
		System.exit(0);		
	}
	
	/**
	 * Executes the client
	 * @param args
	 */
	protected void executeClient(String[] args) {

		try {
			CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
			Map<String, Serializable> commandLineValues = commandLineProcessor.parseCommandLine(PTestPlanExecutorClient.class.getName(), args, getCommandLineOptions());
			if(commandLineValues != null && !commandLineValues.isEmpty()) {
	
				Long threads = (Long)commandLineValues.get(CLI_VALUE_MAP_KEY_THREADS);
				Long recurrences = (Long)commandLineValues.get(CLI_VALUE_MAP_KEY_RECURRENCES);
				String recurrenceType = (String)commandLineValues.get(CLI_VALUE_MAP_KEY_RECURRENCE_TYPE);
				String testplanFile = (String)commandLineValues.get(CLI_VALUE_MAP_KEY_TESTPLAN);
				String hosts = (String)commandLineValues.get(CLI_VALUE_MAP_KEY_SERVER_HOSTS);
				Long port = (Long)commandLineValues.get(CLI_VALUE_MAP_KEY_SERVER_PORT);
			
				byte[] testplan = loadTestplan(testplanFile);
				
				System.out.println(threads + "-" + recurrences + "-"  + hosts);
				executeTestplan(threads.intValue(), recurrences.intValue(), recurrenceType, testplan, hosts.split(","), port.intValue());
			}
		} catch(Exception e) {
			System.out.println("Error while executing client: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Executes the referenced test plan on all given hosts  
	 * @param threads
	 * @param recurrences
	 * @param recurrenceType
	 * @param testplan
	 * @param hosts
	 * @param port
	 * @return
	 */
	protected Map<String, String> executeTestplan(int threads, int recurrences, String recurrenceType, byte[] testplan, String[] hosts, int port) {

		StringBuffer buf = new StringBuffer("/planexec?action=execute");
		buf.append("&threads=").append(threads);
		buf.append("&recurrences=").append(recurrences);
		buf.append("&recurrenceType=").append(recurrenceType);
		
		
		StringBuffer hn = new StringBuffer();
		for(int i = 0; i < hosts.length; i++) {
			hn.append(hosts[i]);
			if(i < hosts.length - 1)
				hn.append(", ");
		}

		System.out.println("Execute testplan:");
		System.out.println("\thostNames: " + hn.toString());
		System.out.println("\tport: " + port);
		System.out.println("\tthreads: " +threads);
		System.out.println("\trecurrences: " + recurrences);
		System.out.println("\trecurrenceType: " + recurrenceType);
		System.out.println("\n\turi: "+ buf.toString());

		PTestPlanExecutorClientCallable callables[] = new PTestPlanExecutorClientCallable[hosts.length];
		for(int i = 0; i < hosts.length; i++) {
			callables[i] = new PTestPlanExecutorClientCallable(hosts[i], port, buf.toString(), testplan);
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(hosts.length);
		List<Future<NameValuePair>> executionResults = new ArrayList<Future<NameValuePair>>();
		try {
			executionResults = executorService.invokeAll(Arrays.asList(callables));
		} catch (InterruptedException e) {
			System.out.println("Test execution interrupted: " + e.getMessage());
		}
		
		// collect results from callables
		Map<String, String> result = new HashMap<String, String>();
		for(Future<NameValuePair> r : executionResults) {			
			try {
				NameValuePair nvp = r.get();
				result.put(nvp.getName(), nvp.getValue());
			} catch (InterruptedException e) {
				System.out.println("Interrupted while waiting for results. Error: " + e.getMessage());
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("Interrupted while waiting for results. Error: " + e.getMessage());
				e.printStackTrace();
			}			
		}
		
		return result;
	}
	
	
	/**
	 * Returns the available command-line options 
	 * @return
	 */
	protected static List<CommandLineOption> getCommandLineOptions() {
	
		List<CommandLineOption> options = new ArrayList<CommandLineOption>();
		options.add(new CommandLineOption(CMD_OPT_MODE_EXECUTE, CMD_OPT_MODE_EXECUTE_SHORT, false, false, String.class, "Executes a test plan on the ptest-server instance(s)", CLI_VALUE_MAP_KEY_EXECUTE, "Required mode 'execute' missing"));
		options.add(new CommandLineOption(CMD_OPT_THREADS, CMD_OPT_THREADS_SHORT, true, true, Long.class, "Number of threads to be used for executing the test plan", CLI_VALUE_MAP_KEY_THREADS, "Required number of threads missing"));
		options.add(new CommandLineOption(CMD_OPT_RECURRENCES, CMD_OPT_RECURRENCES_SHORT, true, true, Long.class, "Number of times to execute the test plan", CLI_VALUE_MAP_KEY_RECURRENCES, "Required number of test plan execution recurrences missing"));
		options.add(new CommandLineOption(CMD_OPT_TESTPLAN, CMD_OPT_TESTPLAN_SHORT, true, true, String.class, "Name of the testplan file", CLI_VALUE_MAP_KEY_TESTPLAN, "Required test plan file"));
		options.add(new CommandLineOption(CMD_OPT_PTEST_SERVER_HOSTS, CMD_OPT_PTEST_SERVER_HOSTS_SHORT, true, true, String.class, "Comma-separated list of hosts running the ptest-server being provided the testplan setup", CLI_VALUE_MAP_KEY_SERVER_HOSTS, "Required server host missing"));
		options.add(new CommandLineOption(CMD_OPT_PTEST_SERVER_PORT, CMD_OPT_PTEST_SERVER_PORT_SHORT, true, true, Long.class, "Port the ptest-server instances listen to", CLI_VALUE_MAP_KEY_SERVER_PORT, "Required ptest-server port missing"));
		options.add(new CommandLineOption(CMD_OPT_RECURRENCE_TYPE, CMD_OPT_RECURRENCE_TYPE_SHORT, true, true, String.class, "Recurrence type specifying the number recurrences (TIMES, MILLIS, SECONDS, MINUTES, HOURS ...)", CLI_VALUE_MAP_KEY_RECURRENCE_TYPE, "Required recurrence type missing"));
		
		return options;
	}
	
	
	/**
	 * Reads in a test plan
	 * @param fileName
	 * @return
	 * @throws TSClientConfigurationException
	 */
	protected byte[] loadTestplan(String fileName) throws InvalidConfigurationException  {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(fileName);
			return IOUtils.toByteArray(fin);
		} catch(IOException e) {
			throw new InvalidConfigurationException("Failed to read test plan file '"+fileName+"'. Error: " + e.getMessage());
		} finally {
			if(fin != null) {
				try {
					fin.close();
				} catch(IOException e) {
					System.out.println("Failed to close test plan file '"+fileName+"'. Error: " + e.getMessage());
				}
			}
		}
		
	}
	
	
}
