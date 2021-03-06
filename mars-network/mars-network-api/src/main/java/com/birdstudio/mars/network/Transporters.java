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

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.Version;
import com.birdstudio.eirene.utils.extension.ExtensionLoader;
import com.birdstudio.mars.network.transport.ChannelHandlerAdapter;
import com.birdstudio.mars.network.transport.ChannelHandlerDispatcher;

/**
 * Transporter facade. (API, Static, ThreadSafe)
 * 
 * @author 
 */
public class Transporters {

	public static Server bind(String url, ChannelHandler... handler)
			throws RemotingException {
		return bind(URL.valueOf(url), handler);
	}

	public static Server bind(URL url, ChannelHandler... handlers)
			throws RemotingException {
		if (url == null) {
			throw new IllegalArgumentException("url == null");
		}
		if (handlers == null || handlers.length == 0) {
			throw new IllegalArgumentException("handlers == null");
		}
		ChannelHandler handler;
		if (handlers.length == 1) {
			handler = handlers[0];
		} else {
			handler = new ChannelHandlerDispatcher(handlers);
		}
		return getTransporter().bind(url, handler);
	}

	public static Client connect(String url, ChannelHandler... handler)
			throws RemotingException {
		return connect(URL.valueOf(url), handler);
	}

	public static Client connect(URL url, ChannelHandler... handlers)
			throws RemotingException {
		if (url == null) {
			throw new IllegalArgumentException("url == null");
		}
		ChannelHandler handler;
		if (handlers == null || handlers.length == 0) {
			handler = new ChannelHandlerAdapter();
		} else if (handlers.length == 1) {
			handler = handlers[0];
		} else {
			handler = new ChannelHandlerDispatcher(handlers);
		}
		return getTransporter().connect(url, handler);
	}

	public static Transporter getTransporter() {
		return ExtensionLoader.getExtensionLoader(Transporter.class)
				.getAdaptiveExtension();
	}

	static {
		// check duplicate jar package
		Version.checkDuplicate(Transporters.class);
		Version.checkDuplicate(RemotingException.class);
	}

	private Transporters() {
	}

}