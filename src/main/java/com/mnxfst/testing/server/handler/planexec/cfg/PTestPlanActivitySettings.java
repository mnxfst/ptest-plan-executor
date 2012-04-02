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

package com.mnxfst.testing.server.handler.planexec.cfg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;

/**
 * Activity settings
 * @author mnxfst
 * @since 02.04.2012
 */
@XStreamAlias ("activity")
public class PTestPlanActivitySettings implements Serializable {

	private static final long serialVersionUID = 7375242892213637803L;

	@XStreamAsAttribute
	private String id = null;
	@XStreamAlias ("description")
	private String description = null;
	@XStreamAlias ("class")
	private String clazz = null;
	@XStreamAlias ("nextActivity")
	private String nextActivity = null;
	@XStreamConverter (CollectionConverter.class)
	private List<PTestPlanConfigurationOption> configuration = new ArrayList<PTestPlanConfigurationOption>();
	
	public PTestPlanActivitySettings() {		
	}

	public void addConfigOption(PTestPlanConfigurationOption option) {
		configuration.add(option);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the nextActivity
	 */
	public String getNextActivity() {
		return nextActivity;
	}

	/**
	 * @param nextActivity the nextActivity to set
	 */
	public void setNextActivity(String nextActivity) {
		this.nextActivity = nextActivity;
	}

	/**
	 * @return the configuration
	 */
	public List<PTestPlanConfigurationOption> getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(List<PTestPlanConfigurationOption> configuration) {
		this.configuration = configuration;
	}
	
	
}
