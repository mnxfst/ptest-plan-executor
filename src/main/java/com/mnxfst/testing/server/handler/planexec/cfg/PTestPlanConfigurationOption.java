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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;

/**
 * Defines the layout of a single configuration block within the test plan
 * @author mnxfst
 * @since 02.04.2012
 */
@XStreamAlias ("configuration")
public class PTestPlanConfigurationOption implements Serializable {

	private static final long serialVersionUID = -3374173336547174118L;

	@XStreamAsAttribute
	private String id = null;
	@XStreamConverter (MapConverter.class)
	private Map<String, String> options = new HashMap<String, String>();
	
	public PTestPlanConfigurationOption() {		
	}
	
	public void addOption(String key, String value) {
		this.options.put(key, value);
	}
	
	public void removeOption(String key) {
		this.options.remove(key);
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
	 * @return the options
	 */
	public Map<String, String> getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", this.id);
		
		StringBuffer buf = new StringBuffer("[");
		if(options != null && !options.isEmpty()) {
			for(Iterator<String> keyIter = options.keySet().iterator(); keyIter.hasNext();) {
				String key = keyIter.next();
				buf.append("(").append(key).append(", ").append(options.get(key)).append(")");
				if(keyIter.hasNext())
					buf.append(", ");
			}
		}
		buf.append("]");
		builder.append("options", buf.toString());
		return builder.toString();
	}
	
}
