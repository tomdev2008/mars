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

import com.birdstudio.eirene.utils.Resetable;

/**
 * Remoting Client. (API/SPI, Prototype, ThreadSafe)
 * 
 * <a href="http://en.wikipedia.org/wiki/Client%E2%80%93server_model">Client/
 * Server</a>
 * 
 * @see com.birdstudio.mars.network.Transporter#connect(com.birdstudio.eirene.utils.URL,
 *      ChannelHandler)
 * @author qian.lei
 */
public interface Client extends Endpoint, Channel, Resetable {

	/**
	 * reconnect.
	 */
	void reconnect() throws RemotingException;

}