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
package com.birdstudio.mars.network.transport;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.Resetable;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.extension.ExtensionLoader;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.mars.network.ChannelHandler;
import com.birdstudio.mars.network.Codec;
import com.birdstudio.mars.network.transport.codec.TransportCodec;

/**
 * AbstractEndpoint
 * 
 * @author 
 */
public abstract class AbstractEndpoint extends AbstractPeer implements
		Resetable {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractEndpoint.class);

	private Codec codec;

	private int timeout;

	private int connectTimeout;

	public AbstractEndpoint(URL url, ChannelHandler handler) {
		super(url, handler);
		this.codec = getChannelCodec(url);
		this.timeout = url.getPositiveParameter(Constants.TIMEOUT_KEY,
				Constants.DEFAULT_TIMEOUT);
		this.connectTimeout = url.getPositiveParameter(
				Constants.CONNECT_TIMEOUT_KEY,
				Constants.DEFAULT_CONNECT_TIMEOUT);
	}

	public void reset(URL url) {
		if (isClosed()) {
			throw new IllegalStateException("Failed to reset parameters " + url
					+ ", cause: Channel closed. channel: " + getLocalAddress());
		}
		try {
			if (url.hasParameter(Constants.HEARTBEAT_KEY)) {
				int t = url.getParameter(Constants.TIMEOUT_KEY, 0);
				if (t > 0) {
					this.timeout = t;
				}
			}
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
		try {
			if (url.hasParameter(Constants.CONNECT_TIMEOUT_KEY)) {
				int t = url.getParameter(Constants.CONNECT_TIMEOUT_KEY, 0);
				if (t > 0) {
					this.connectTimeout = t;
				}
			}
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
		try {
			if (url.hasParameter(Constants.CODEC_KEY)) {
				this.codec = getChannelCodec(url);
			}
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
	}

	protected Codec getCodec() {
		return codec;
	}

	protected int getTimeout() {
		return timeout;
	}

	protected int getConnectTimeout() {
		return connectTimeout;
	}

	protected static Codec getChannelCodec(URL url) {
		String codecName = url.getParameter(Constants.CODEC_KEY, "telnet");
		if (ExtensionLoader.getExtensionLoader(Codec.class).hasExtension(
				codecName)) {
			return ExtensionLoader.getExtensionLoader(Codec.class)
					.getExtension(codecName);
		} else {
			return new TransportCodec();
			// return new CodecAdapter(ExtensionLoader.getExtensionLoader(
			// Codec.class).getExtension(codecName));
		}
	}

}