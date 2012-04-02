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
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;

/**
 * @author mnxfst
 * @since 02.04.2012
 */
@XStreamAlias( "ptestplan" )
public class PTestPlan implements Serializable {

	private static final long serialVersionUID = 3592067281373324879L;

	@XStreamAlias ("name")
	private String name = null;	
	@XStreamAlias ("description")
	private String description = null;	
	@XStreamAlias ("creationDate")
	@XStreamConverter(SingleValueDateConverter.class)
	private Date creationDate = null;
	@XStreamAlias ("createdBy")
	private String createdBy = null;
	@XStreamConverter (CollectionConverter.class)
	private List<PTestPlanConfigurationOption> globalCfgOptions = new ArrayList<PTestPlanConfigurationOption>();
	@XStreamConverter (CollectionConverter.class)
	private List<PTestPlanActivitySettings> activities = new ArrayList<PTestPlanActivitySettings>();
	
	public PTestPlan() {
		
	}
	
	public void addActivityConfigOption(PTestPlanActivitySettings cfgOption) {
		activities.add(cfgOption);
	}

	public void addGlobalCfgOption(PTestPlanConfigurationOption option) {
		globalCfgOptions.add(option);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
