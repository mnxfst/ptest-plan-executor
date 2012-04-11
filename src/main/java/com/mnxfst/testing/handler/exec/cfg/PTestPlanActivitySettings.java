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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;

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
	@XStreamAlias ( "configuration" )
	private PTestPlanConfigurationOption configuration = new PTestPlanConfigurationOption();
	@XStreamConverter (MapConverter.class)
	private Map<String, String> exportVariables = new HashMap<String, String>();
	
	public PTestPlanActivitySettings() {		
	}

	public void addConfigOption(String key, String value) {
		configuration.addOption(key, value);
	}
	
	public void addExportVariable(String internalName, String contextVariableName) {
		this.exportVariables.put(internalName, contextVariableName);
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
	public PTestPlanConfigurationOption getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(PTestPlanConfigurationOption configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * @return the exportVariables
	 */
	public Map<String, String> getExportVariables() {
		return exportVariables;
	}

	/**
	 * @param exportVariables the exportVariables to set
	 */
	public void setExportVariables(Map<String, String> exportVariables) {
		this.exportVariables = exportVariables;
	}

	/** 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", this.id);
		builder.append("description", this.description);
		builder.append("class", this.clazz);
		builder.append("nextActivity", this.nextActivity);
		
		StringBuffer buf = new StringBuffer("[");
//		if(configuration != null) {				
//			for(Iterator<PTestPlanConfigurationOption> cfgIter = configuration.getOptions().keySet().iterator(); cfgIter.hasNext();) {
//				PTestPlanConfigurationOption cfg = cfgIter.next();
//				buf.append("(");
//				if(cfg != null)
//					buf.append(cfg.toString());
//				buf.append(")");
//				if(cfgIter.hasNext())
//					buf.append(", ");
//				
//			}
//		}
		buf.append("]");
		builder.append("configuration", buf.toString());
		return builder.toString();
	}
	
}
