/*
 * Copyright 1999-2011 Birdstudio Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.birdstudio.mars.network;

import com.birdstudio.eirene.utils.extension.SPI;

/**
 * ChannelHandler. (API, Prototype, ThreadSafe)
 * 
 * @see com.birdstudio.mars.network.Transporter#bind(com.birdstudio.eirene.utils.URL,
 *      ChannelHandler)
 * @see com.birdstudio.mars.network.Transporter#connect(com.birdstudio.eirene.utils.URL,
 *      ChannelHandler)
 * @author qian.lei
 * @author 
 */
@SPI
public interface ChannelHandler {

	/**
	 * on channel connected.
	 * 
	 * @param channel
	 *            channel.
	 */
	void connected(Channel channel) throws RemotingException;

	/**
	 * on channel disconnected.
	 * 
	 * @param channel
	 *            channel.
	 */
	void disconnected(Channel channel) throws RemotingException;

	/**
	 * on message sent.
	 * 
	 * @param channel
	 *            channel.
	 * @param message
	 *            message.
	 */
	void sent(Channel channel, Object message) throws RemotingException;

	/**
	 * on message received.
	 * 
	 * @param channel
	 *            channel.
	 * @param message
	 *            message.
	 */
	void received(Channel channel, Object message) throws RemotingException;

	/**
	 * on exception caught.
	 * 
	 * @param channel
	 *            channel.
	 * @param exception
	 *            exception.
	 */
	void caught(Channel channel, Throwable exception) throws RemotingException;

}