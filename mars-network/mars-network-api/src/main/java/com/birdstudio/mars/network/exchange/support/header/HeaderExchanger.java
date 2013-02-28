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
package com.birdstudio.mars.network.exchange.support.header;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.network.RemotingException;
import com.birdstudio.mars.network.Transporters;
import com.birdstudio.mars.network.exchange.ExchangeClient;
import com.birdstudio.mars.network.exchange.ExchangeHandler;
import com.birdstudio.mars.network.exchange.ExchangeServer;
import com.birdstudio.mars.network.exchange.Exchanger;
import com.birdstudio.mars.network.transport.DecodeHandler;

/**
 * DefaultMessenger
 * 
 * @author 
 */
public class HeaderExchanger implements Exchanger {

	public static final String NAME = "header";

	public ExchangeClient connect(URL url, ExchangeHandler handler)
			throws RemotingException {
		return new HeaderExchangeClient(Transporters.connect(url,
				new DecodeHandler(new HeaderExchangeHandler(handler))));
	}

	public ExchangeServer bind(URL url, ExchangeHandler handler)
			throws RemotingException {
		return new HeaderExchangeServer(Transporters.bind(url,
				new DecodeHandler(new HeaderExchangeHandler(handler))));
	}

}