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

import javax.sound.midi.Receiver;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.extension.Adaptive;
import com.birdstudio.eirene.utils.extension.SPI;

/**
 * Transporter. (SPI, Singleton, ThreadSafe)
 * 
 * <a href="http://en.wikipedia.org/wiki/Transport_Layer">Transport Layer</a> <a
 * href
 * ="http://en.wikipedia.org/wiki/Client%E2%80%93server_model">Client/Server</a>
 * 
 * @see com.birdstudio.mars.network.Transporters
 * @author ding.lid
 * @author 
 */
@SPI("netty")
public interface Transporter {

	/**
	 * Bind a server.
	 * 
	 * @see com.birdstudio.mars.network.Transporters#bind(URL, Receiver,
	 *      ChannelHandler)
	 * @param url
	 *            server url
	 * @param handler
	 * @return server
	 * @throws RemotingException
	 */
	@Adaptive({ Constants.SERVER_KEY, Constants.TRANSPORTER_KEY })
	Server bind(URL url, ChannelHandler handler) throws RemotingException;

	/**
	 * Connect to a server.
	 * 
	 * @see com.birdstudio.mars.network.Transporters#connect(URL, Receiver,
	 *      ChannelListener)
	 * @param url
	 *            server url
	 * @param handler
	 * @return client
	 * @throws RemotingException
	 */
	@Adaptive({ Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY })
	Client connect(URL url, ChannelHandler handler) throws RemotingException;

}