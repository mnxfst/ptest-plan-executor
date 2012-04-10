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

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * Dummy implementation of {@link ChannelFuture} for enabling unit testing
 * @author mnxfst
 * @since 10.04.2012
 */
public class TestMessageEventChannelFuture implements ChannelFuture {

	private Object message = null;

	public void setMessage(Object message) {
		this.message = message;
	}
	
	public Object getMessage() {
		return message;
	}
	
	/**
	 * @see org.jboss.netty.channel.ChannelFuture#getChannel()
	 */
	public Channel getChannel() {
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#isDone()
	 */
	public boolean isDone() {
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#isCancelled()
	 */
	public boolean isCancelled() {
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#isSuccess()
	 */
	public boolean isSuccess() {
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#getCause()
	 */
	public Throwable getCause() {
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#cancel()
	 */
	public boolean cancel() {
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#setSuccess()
	 */
	public boolean setSuccess() {
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#setFailure(java.lang.Throwable)
	 */
	public boolean setFailure(Throwable cause) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#setProgress(long, long, long)
	 */
	public boolean setProgress(long amount, long current, long total) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#addListener(org.jboss.netty.channel.ChannelFutureListener)
	 */
	public void addListener(ChannelFutureListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#removeListener(org.jboss.netty.channel.ChannelFutureListener)
	 */
	public void removeListener(ChannelFutureListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#rethrowIfFailed()
	 */
	public ChannelFuture rethrowIfFailed() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#await()
	 */
	public ChannelFuture await() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#awaitUninterruptibly()
	 */
	public ChannelFuture awaitUninterruptibly() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#await(long, java.util.concurrent.TimeUnit)
	 */
	public boolean await(long timeout, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#await(long)
	 */
	public boolean await(long timeoutMillis) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#awaitUninterruptibly(long, java.util.concurrent.TimeUnit)
	 */
	public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelFuture#awaitUninterruptibly(long)
	 */
	public boolean awaitUninterruptibly(long timeoutMillis) {
		// TODO Auto-generated method stub
		return false;
	}

}
