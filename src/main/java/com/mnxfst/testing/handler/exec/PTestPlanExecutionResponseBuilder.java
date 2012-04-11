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

import com.mnxfst.testing.server.PTestServerResponseBuilder;

/**
 * @author mnxfst
 * @since 12.04.2012
 */
public class PTestPlanExecutionResponseBuilder extends PTestServerResponseBuilder {

	public static final String RESPONSE_XML_RESULT_IDENTIFIER_TAG = "resultIdentifier";
	
	public static String buildTestExecutionStartedResponse(String hostname, int port, String resultIdentifier) {
		
		StringBuffer response = new StringBuffer();
		response.append("<").append(RESPONSE_XML_ROOT).append(">");
		response.append("<").append(RESPONSE_XML_RESPONSE_CODE_TAG).append(">").append(RESPONSE_CODE_OK).append("</").append(RESPONSE_XML_RESPONSE_CODE_TAG).append(">");
		response.append("<").append(RESPONSE_XML_HOSTNAME_TAG).append(">").append(hostname).append("</").append(RESPONSE_XML_HOSTNAME_TAG).append(">");
		response.append("<").append(RESPONSE_XML_PORT_TAG).append(">").append(port).append("</").append(RESPONSE_XML_PORT_TAG).append(">");
		response.append("<").append(RESPONSE_XML_RESULT_IDENTIFIER_TAG).append(">").append(resultIdentifier).append("</").append(RESPONSE_XML_RESULT_IDENTIFIER_TAG).append(">");
		response.append("</").append(RESPONSE_XML_ROOT).append(">");
		return response.toString();
	}

	
}
