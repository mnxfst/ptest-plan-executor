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

package com.mnxfst.testing.server.handler.planexec;

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;

/**
 * Provides a dummy implementation for {@link Channel}
 * @author mnxfst
 * @since 10.04.2012
 */
public class TestMessageEventChannel implements Channel {

	private TestMessageEventChannelFuture testChannelFuture = null;
	
	public TestMessageEventChannel(TestMessageEventChannelFuture testChannelFuture) {
		this.testChannelFuture = testChannelFuture;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Channel arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getId()
	 */
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getFactory()
	 */
	public ChannelFactory getFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getParent()
	 */
	public Channel getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getConfig()
	 */
	public ChannelConfig getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getPipeline()
	 */
	public ChannelPipeline getPipeline() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#isOpen()
	 */
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#isBound()
	 */
	public boolean isBound() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#isConnected()
	 */
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getLocalAddress()
	 */
	public SocketAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getRemoteAddress()
	 */
	public SocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#write(java.lang.Object)
	 */
	public ChannelFuture write(Object message) {
		testChannelFuture.setMessage(message);
		return testChannelFuture;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#write(java.lang.Object, java.net.SocketAddress)
	 */
	public ChannelFuture write(Object message, SocketAddress remoteAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#bind(java.net.SocketAddress)
	 */
	public ChannelFuture bind(SocketAddress localAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#connect(java.net.SocketAddress)
	 */
	public ChannelFuture connect(SocketAddress remoteAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#disconnect()
	 */
	public ChannelFuture disconnect() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#unbind()
	 */
	public ChannelFuture unbind() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#close()
	 */
	public ChannelFuture close() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getCloseFuture()
	 */
	public ChannelFuture getCloseFuture() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#getInterestOps()
	 */
	public int getInterestOps() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#isReadable()
	 */
	public boolean isReadable() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#isWritable()
	 */
	public boolean isWritable() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#setInterestOps(int)
	 */
	public ChannelFuture setInterestOps(int interestOps) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.Channel#setReadable(boolean)
	 */
	public ChannelFuture setReadable(boolean readable) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
