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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.mnxfst.testing.handler.exec.activity.PTestPlanActivity;
import com.mnxfst.testing.handler.exec.cfg.PTestPlan;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanActivitySettings;
import com.mnxfst.testing.handler.exec.cfg.PTestPlanConfigurationOption;
import com.mnxfst.testing.handler.exec.exception.ActivityExecutionFailedException;
import com.mnxfst.testing.handler.exec.exception.InvalidConfigurationException;

/**
 * Receives a {@link PTestPlan test plan configuration}, extracts the {@link PTestPlanActivity activities} and instantiates the {@link PTestPlanProcessor} instances
 * @author mnxfst
 * @since 11.04.2012
 */
public class PTestPlanExecutor implements Runnable {
	
	private static final Logger logger = Logger.getLogger(PTestPlanExecutor.class);

	private static final String GLOBAL_CONST_CONFIGURATION_ID = "globalConstants";
	
	private final PTestPlan testPlanConfiguration;
	private final long numOfRuns;
	private final int workQueueSize;
	private final int workerThreads;
	private final String resultIdentifier;
	private Map<String, PTestPlanActivity> activities = new HashMap<String, PTestPlanActivity>();
	
	/**
	 * Initializes the test plan executor
	 * @param testPlanConfiguration
	 */
	public PTestPlanExecutor(final PTestPlan testPlanConfiguration, final String resultIdentifier, final long numOfRuns, final int workQueueSize, final int workerThreads) throws InvalidConfigurationException {
		
		// validate plan
		if(testPlanConfiguration == null)
			throw new InvalidConfigurationException("Required test plan configuration missing");

		if(testPlanConfiguration.getName() == null || testPlanConfiguration.getName().trim().isEmpty())
			throw new InvalidConfigurationException("The provided test plan configuration misses a name");

		if(testPlanConfiguration.getActivities() == null || testPlanConfiguration.getActivities().isEmpty())
			throw new InvalidConfigurationException("The provided test plan configuration does not contain any activities");
		
		if(resultIdentifier == null || resultIdentifier.trim().isEmpty())
			throw new InvalidConfigurationException("Missing required result identifier");
		
		if(numOfRuns < 1)
			throw new InvalidConfigurationException("Invalid number of runs: " + numOfRuns);
		
		if(workQueueSize < 1)
			throw new InvalidConfigurationException("Invalid work queue size: " + workQueueSize);
		
		if(workerThreads < 1)
			throw new InvalidConfigurationException("Invalid number of worker threads: " + workerThreads);
				
		this.testPlanConfiguration = testPlanConfiguration;
		this.resultIdentifier = resultIdentifier;
		this.numOfRuns = numOfRuns;
		this.workQueueSize = workQueueSize;
		this.workerThreads = workerThreads;
		
		// step through activity settings
		for(PTestPlanActivitySettings activitySetting : testPlanConfiguration.getActivities()) {
			
			// ensure that the current activity is not null
			if(activitySetting != null) {
				
				// extract all required information: class, config, description, identifier, next activity, export variables
				String activityClazz = activitySetting.getClazz();
				PTestPlanConfigurationOption configuration = activitySetting.getConfiguration();
				String description = activitySetting.getDescription();
				String activityId = activitySetting.getId();
				String nextActivityId = activitySetting.getNextActivity();
				Map<String, String> exportVariables = activitySetting.getExportVariables();
				
				// instantiate the activity, configure it properly and add it to the map of available activities
				PTestPlanActivity activity = getActivityInstance(activityClazz);
				activity.setActivityId(activityId);
				activity.setActivityType(activityClazz);
				activity.setDescription(description);
				activity.setNextActivityId(nextActivityId);
				activity.configure(configuration.getOptions(), exportVariables);

				activities.put(activityId, activity);
				
				if(logger.isDebugEnabled())
					logger.debug("Successfully instantiated and added activity [id="+activity.getActivityId() + ", type="+activity.getActivityType()+"]");
			}
		}
		
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		// fetch global constants to be used for initializing the context 
		Map<String, String> contextElementInitCfg = new HashMap<String, String>();
		if(testPlanConfiguration.getGlobalCfgOptions() != null && !testPlanConfiguration.getGlobalCfgOptions().isEmpty()) {
			for(PTestPlanConfigurationOption globalCfg : testPlanConfiguration.getGlobalCfgOptions()) {
				if(globalCfg != null && globalCfg.getId() != null && globalCfg.getId().equalsIgnoreCase(GLOBAL_CONST_CONFIGURATION_ID)) {
					contextElementInitCfg.putAll(globalCfg.getOptions());
				}
			}
		}

		// initialize the queue being used for submitting waiting tasks
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(workQueueSize);
		
		// initialize the thread executor being in charge for processing the single context elements / activities
		ThreadPoolExecutor executor = new ThreadPoolExecutor(workerThreads, workerThreads, 0L, TimeUnit.MILLISECONDS, queue);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		// set for collecting results
		Set<Future<PTestPlanExecutionContext>> executionResults = new HashSet<Future<PTestPlanExecutionContext>>();
		
		/////////////////////////////////// TASK SUBMISSION ///////////////////////////////////
		long startSubmission = System.currentTimeMillis();

		// instantiate context elements, provide required information and pass them to executor
		for(int i = 1; i <= numOfRuns; i++) {
			
			PTestPlanExecutionContext contextElement = new PTestPlanExecutionContext();
			contextElement.setNextActivityId(PTestPlanProcessor.INIT_STATE);
			contextElement.setCurrentActivityId(PTestPlanProcessor.INIT_STATE);
			contextElement.getContextVariables().putAll(contextElementInitCfg);
			contextElement.setRunId(i);
			
			try {
				executionResults.add(executor.submit(new PTestPlanProcessor("worker-#"+i, contextElement, activities)));
			} catch (InvalidConfigurationException e) {
				logger.error("Failed to submit test plan processor although the input values are valid. Error: " + e.getMessage());
			}
			
		}
		
		// calculate submission duration
		long submissionDuration = System.currentTimeMillis() - startSubmission;
		
		if(logger.isDebugEnabled())
			logger.debug("Thread pool task submission: " + submissionDuration + "ms");
		/////////////////////////////////// END: TASK SUBMISSION ///////////////////////////////////

		/////////////////////////////////// RESULT COLLECTION ///////////////////////////////////
		List<Long> runtimes = new ArrayList<Long>();
		long overallActionCalls = 0;
		long startResultCollection = System.currentTimeMillis();
		
		// iterate through available results
		for(Future<PTestPlanExecutionContext> resultElement : executionResults) {
			try {
				// fetch context element, add execution duration to average runtime and add the number of action alls
				PTestPlanExecutionContext contextElement = resultElement.get();				
				// add runtime
				runtimes.add(Long.valueOf(contextElement.getFinalTimestamp() - contextElement.getInitTimestamp()));				
				// add number of action calls
				overallActionCalls = overallActionCalls + contextElement.getActionCalls();				
			} catch (ExecutionException e) {
				logger.error("Exception found during activity execution: " + e.getMessage());
			} catch (InterruptedException e) {
				logger.error("Interrupted while collecting results: " + e.getMessage());
			}			
		}
		
		long resultCollectionDuration = System.currentTimeMillis() - startResultCollection;
		
		if(logger.isDebugEnabled())
			logger.debug("Result collection: " + resultCollectionDuration + "ms");
		/////////////////////////////////// END: RESULT COLLECTION ///////////////////////////////////

		Collections.sort(runtimes);

		PTestPlanExecutorResult result = new PTestPlanExecutorResult(resultIdentifier, testPlanConfiguration.getName(), getTotalRuntime(runtimes), getMinRuntime(runtimes), getMaxRuntime(runtimes), getAverageRuntime(runtimes), getMedianRuntime(runtimes), numOfRuns, workQueueSize, workerThreads);
		
		PTestPlanExecutionContextHandler.addResponse(resultIdentifier, result);
		
		for(PTestPlanActivity activity : activities.values()) {
			try {
				activity.shutdown();
			} catch(ActivityExecutionFailedException e) {
				logger.error("Failed to shutdown activity '"+activity.getActivityId()+"'");
			}
		}
	}
	
	///////////////////////////////// UTILITY METHODS FOR CALCULATING REQUIRED RESULT INFORMATION /////////////////////////////////
	
	/**
	 * Calculate the median runtime from the provided set of runtimes 
	 * @param runtimes
	 * @return
	 */
	protected double getMedianRuntime(List<Long> runtimes) {
		if(runtimes == null || runtimes.isEmpty())
			return 0.0;

		double medianRuntime = 0.0;
		if(runtimes.size() % 2 == 1) {
			medianRuntime = runtimes.get((runtimes.size() + 1 ) / 2 - 1);
		} else {
			double lower = runtimes.get(runtimes.size() / 2 - 1);
			double upper = runtimes.get(runtimes.size() / 2);
			medianRuntime = (lower + upper) / 2.0;		
		}
		return medianRuntime;		
	}
	
	/**
	 * Calculates the average runtime from the provided set of runtimes
	 * @param runtimes
	 * @return
	 */
	protected double getAverageRuntime(List<Long> runtimes) {
		if(runtimes == null || runtimes.isEmpty())
			return 0;
		
		long averageRuntime = 0;
		for(Long r : runtimes) {
			if(r != null) {
				averageRuntime = averageRuntime + r.longValue();
			}
		}
		return ((double)averageRuntime / (double)runtimes.size());
	}
	
	/**
	 * Returns the min runtime from the provided set of runtimes
	 * @param runtimes
	 * @return
	 */
	protected long getMinRuntime(List<Long> runtimes) {
		if(runtimes == null || runtimes.isEmpty())
			return 0;

		return runtimes.get(0);
	}
	
	/**
	 * Returns the max runtime from the provided set of runtimes
	 * @param runtimes
	 * @return
	 */
	protected long getMaxRuntime(List<Long> runtimes) {
		if(runtimes == null || runtimes.isEmpty())
			return 0;

		return runtimes.get(runtimes.size() - 1);
	}

	/**
	 * Returns the total runtime computed as sum over all runtimes 
	 * @param runtimes
	 * @return
	 */
	protected long getTotalRuntime(List<Long> runtimes) {
		if(runtimes == null || runtimes.isEmpty())
			return 0;

		long totalRuntime = 0;
		for(Long r : runtimes) {
			if(r != null) {
				totalRuntime = totalRuntime + r.longValue();
			}
		}
		
		return totalRuntime;
	}
	
	/**
	 * Instantiates an instance of type {@link PTestPlanActivity}
	 * @param activityClass
	 * @return
	 * @throws InvalidConfigurationException
	 */
	protected PTestPlanActivity getActivityInstance(String activityClass) throws InvalidConfigurationException {
		if(activityClass == null || activityClass.trim().isEmpty())
			throw new InvalidConfigurationException("Missing required activity class");
		
		try {
			Class<?> clazz = Class.forName(activityClass);
			return (PTestPlanActivity)clazz.newInstance();
		} catch(InstantiationException e) {
			throw new InvalidConfigurationException("Failed to instantiate activity '"+activityClass+"'. Error: " + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new InvalidConfigurationException("Failed to instantiate activity '"+activityClass+"'. Error: " + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new InvalidConfigurationException("Failed to instantiate activity '"+activityClass+"'. Error: " + e.getMessage(), e);			
		} catch(ClassCastException e) {
			throw new InvalidConfigurationException("Failed to instantiate activity '"+activityClass+"'. Error: " + e.getMessage(), e);
		}
	}

}
