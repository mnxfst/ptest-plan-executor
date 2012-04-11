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

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.MessageEvent;

/**
 * Provides a simple provide and replay implementation of the {@link MessageEvent} interface
 * @author mnxfst
 * @since 10.04.2012
 */
public class TestMessageEvent implements MessageEvent {

	private Object message = null;
	private TestMessageEventChannel testChannel = null;

	/**
	 * Initializes the test event
	 * @param message
	 */
	public TestMessageEvent(Object message, TestMessageEventChannel testChannel) {
		this.message = message;
		this.testChannel = testChannel;
	}
	
	/**
	 * @see org.jboss.netty.channel.ChannelEvent#getChannel()
	 */
	public Channel getChannel() {
		return testChannel;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelEvent#getFuture()
	 */
	public ChannelFuture getFuture() {
		throw new UnsupportedOperationException("Operation not supported");
	}

	/**
	 * @see org.jboss.netty.channel.MessageEvent#getMessage()
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * @see org.jboss.netty.channel.MessageEvent#getRemoteAddress()
	 */
	public SocketAddress getRemoteAddress() {
		throw new UnsupportedOperationException("Operation not supported");
	}

}
