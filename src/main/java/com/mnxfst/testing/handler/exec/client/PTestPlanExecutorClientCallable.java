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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;

import com.mnxfst.testing.handler.exec.response.PTestPlanExecutionStartedResponse;
import com.mnxfst.testing.handler.exec.response.PTestPlanExecutorResponseBuilder;

/**
 * Executes a test plan on a single ptest-server instance and collects the result 
 * @author mnxfst
 * @since 08.05.2012
 */
public class PTestPlanExecutorClientCallable implements Callable<NameValuePair> {
	
	private DefaultHttpClient httpClient = null;
	private HttpHost httpHost = null;
	private HttpPost postMethod = null;

	/**
	 * Initializes the plan executor
	 * @param hostname
	 * @param port
	 * @param uri
	 * @param testplan
	 */
	public PTestPlanExecutorClientCallable(String hostname, int port, String uri, byte[] testplan) {
		
		this.httpHost = new HttpHost(hostname, port);
		this.postMethod = new HttpPost(uri);
		
		try {
			String encodedTestplan = new String(testplan, "UTF-8");
			postMethod.setEntity(new StringEntity(PTestPlanExecutorClient.REQUEST_PARAMETER_TESTPLAN + "=" + URLEncoder.encode(encodedTestplan, "UTF-8"), "UTF-8"));
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding exception. Error: " + e.getMessage());
		}
		
		// TODO setting?
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		
		ThreadSafeClientConnManager threadSafeClientConnectionManager = new ThreadSafeClientConnManager(schemeRegistry);
		threadSafeClientConnectionManager.setMaxTotal(20);
		threadSafeClientConnectionManager.setDefaultMaxPerRoute(20);

		this.httpClient = new DefaultHttpClient(threadSafeClientConnectionManager);
	}

	/**
	 * @see java.util.concurrent.Callable#call()
	 */
	public NameValuePair call() throws Exception {
		
		InputStream ptestServerInputStream = null;
		try {
			HttpResponse httpResponse = httpClient.execute(httpHost, postMethod);
			ptestServerInputStream = httpResponse.getEntity().getContent();
			
			PTestPlanExecutionStartedResponse response = PTestPlanExecutorResponseBuilder.buildTestplanExecutionStartedResponse(ptestServerInputStream);

			if(response.getResponseCode() != null && response.getResponseCode().intValue() == PTestPlanExecutionStartedResponse.RESPONSE_OK) {
				return new BasicNameValuePair(httpHost.getHostName(), response.getResultIdentifier());
			} else {
				
				StringBuffer errors = new StringBuffer();
				for(Iterator<String> keyIter = response.getErrors().keySet().iterator(); keyIter.hasNext();) {
					String errKey = keyIter.next();
					
					errors.append(errKey);
					if(keyIter.hasNext())
						errors.append(", ");
				}
				
				throw new RuntimeException("Failed to execute test plan on " + httpHost.getHostName() + ":" + httpHost.getPort() + ". Errors: " + errors.toString());
				
			}
			
		} catch(ClientProtocolException e) {
			throw new RuntimeException("Failed to call " + httpHost.getHostName() + ":" + httpHost.getPort() + "/"+ postMethod.getURI() + ". Error: " + e.getMessage());
		} catch(IOException e) {
			throw new RuntimeException("Failed to call " + httpHost.getHostName() + ":" + httpHost.getPort() + "/"+ postMethod.getURI() + ". Error: " + e.getMessage());
		} finally {
			if(ptestServerInputStream != null) {
				try {
					ptestServerInputStream.close();
					httpClient.getConnectionManager().shutdown();
					
				} catch(Exception e) {
					System.out.println("Failed to close ptest-server connection");
				} 
			}
		}
	}
	
}
