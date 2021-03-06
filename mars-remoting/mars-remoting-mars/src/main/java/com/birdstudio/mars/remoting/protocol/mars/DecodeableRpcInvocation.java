/*
 * Copyright 1999-2011 Birdstudio Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.birdstudio.mars.remoting.protocol.mars;

import static com.birdstudio.mars.remoting.protocol.mars.CallbackServiceCodec.decodeInvocationArgument;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.birdstudio.eirene.utils.Assert;
import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.ReflectUtils;
import com.birdstudio.eirene.utils.StringUtils;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.eirene.utils.serialize.ObjectInput;
import com.birdstudio.mars.network.Channel;
import com.birdstudio.mars.network.Decodeable;
import com.birdstudio.mars.network.exchange.Request;
import com.birdstudio.mars.network.transport.CodecSupport;
import com.birdstudio.mars.remoting.RpcInvocation;

public class DecodeableRpcInvocation extends RpcInvocation implements
		Decodeable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(DecodeableRpcInvocation.class);

	private final Channel channel;

	private final byte serializationType;

	private final InputStream inputStream;

	private final Request request;

	private volatile boolean hasDecoded;

	public DecodeableRpcInvocation(Channel channel, Request request,
			InputStream is, byte id) {
		Assert.notNull(channel, "channel == null");
		Assert.notNull(request, "request == null");
		Assert.notNull(is, "inputStream == null");
		this.channel = channel;
		this.request = request;
		this.inputStream = is;
		this.serializationType = id;
	}

	@Override
	public void decode() throws Exception {
		if (!hasDecoded && channel != null && inputStream != null) {
			try {
				decode(channel, inputStream);
			} catch (Throwable e) {
				if (log.isWarnEnabled()) {
					log.warn("Decode rpc invocation failed: " + e.getMessage(),
							e);
				}
				request.setBroken(true);
				request.setData(e);
			} finally {
				hasDecoded = true;
			}
		}
	}

	public void encode(Channel channel, OutputStream output, Object message)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public Object decode(Channel channel, InputStream input) throws IOException {
		ObjectInput in = CodecSupport.getSerialization(channel.getUrl(),
				serializationType).deserialize(channel.getUrl(), input);

		setAttachment(Constants.MARS_VERSION_KEY, in.readUTF());
		setAttachment(Constants.PATH_KEY, in.readUTF());
		setAttachment(Constants.VERSION_KEY, in.readUTF());

		setMethodName(in.readUTF());
		try {
			Object[] args;
			Class<?>[] pts;
			String desc = in.readUTF();
			if (desc.length() == 0) {
				pts = MarsCodec.EMPTY_CLASS_ARRAY;
				args = MarsCodec.EMPTY_OBJECT_ARRAY;
			} else {
				pts = ReflectUtils.desc2classArray(desc);
				args = new Object[pts.length];
				for (int i = 0; i < args.length; i++) {
					try {
						args[i] = in.readObject(pts[i]);
					} catch (Exception e) {
						if (log.isWarnEnabled()) {
							log.warn(
									"Decode argument failed: " + e.getMessage(),
									e);
						}
					}
				}
			}
			setParameterTypes(pts);

			Map<String, String> map = in.readObject(Map.class);
			if (map != null && map.size() > 0) {
				Map<String, String> attachment = getAttachments();
				if (attachment == null) {
					attachment = new HashMap<String, String>();
				}
				attachment.putAll(map);
				setAttachments(attachment);
			}
			// decode argument ,may be callback
			for (int i = 0; i < args.length; i++) {
				args[i] = decodeInvocationArgument(channel, this, pts, i,
						args[i]);
			}

			setArguments(args);

		} catch (ClassNotFoundException e) {
			throw new IOException(StringUtils.toString(
					"Read invocation data failed.", e));
		}
		return this;
	}

}
