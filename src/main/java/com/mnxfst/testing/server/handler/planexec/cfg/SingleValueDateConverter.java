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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Implements a conversion from string to date and backwards using the pattern "yyyy-MM-dd"
 * @author mnxfst
 * @since 02.04.2012
 */
public class SingleValueDateConverter extends AbstractSingleValueConverter {

	/** required date format */
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.equals(Date.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.SingleValueConverter#fromString(java.lang.String)
	 */
	public Object fromString(String str) {
		try {
			return simpleDateFormat.parse(str);
		} catch(ParseException e) {
			throw new ConversionException("Cannot convert string '"+str+"' into a date object");
		}
	}

	/**
	 * @see com.thoughtworks.xstream.converters.SingleValueConverter#toString(java.lang.Object)
	 */
	public String toString(Object date) {
		return simpleDateFormat.format((Date)date);
	}

}
