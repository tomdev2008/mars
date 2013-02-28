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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import com.birdstudio.eirene.utils.Assert;
import com.birdstudio.eirene.utils.StringUtils;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.eirene.utils.serialize.ObjectInput;
import com.birdstudio.mars.network.Channel;
import com.birdstudio.mars.network.Decodeable;
import com.birdstudio.mars.network.exchange.Response;
import com.birdstudio.mars.network.transport.CodecSupport;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.RpcResult;
import com.birdstudio.mars.remoting.support.RpcUtils;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public class DecodeableRpcResult extends RpcResult implements Decodeable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(DecodeableRpcResult.class);

	private final Channel channel;

	private final byte serializationType;

	private final InputStream inputStream;

	private final Response response;

	private final Invocation invocation;

	private volatile boolean hasDecoded;

	public DecodeableRpcResult(Channel channel, Response response,
			InputStream is, Invocation invocation, byte id) {
		Assert.notNull(channel, "channel == null");
		Assert.notNull(response, "response == null");
		Assert.notNull(is, "inputStream == null");
		this.channel = channel;
		this.response = response;
		this.inputStream = is;
		this.invocation = invocation;
		this.serializationType = id;
	}

	public void encode(Channel channel, OutputStream output, Object message)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public Object decode(Channel channel, InputStream input) throws IOException {
		ObjectInput in = CodecSupport.getSerialization(channel.getUrl(),
				serializationType).deserialize(channel.getUrl(), input);

		byte flag = in.readByte();
		switch (flag) {
		case MarsCodec.RESPONSE_NULL_VALUE:
			break;
		case MarsCodec.RESPONSE_VALUE:
			try {
				Type[] returnType = RpcUtils.getReturnTypes(invocation);
				setValue(returnType == null || returnType.length == 0 ? in
						.readObject() : (returnType.length == 1 ? in
						.readObject((Class<?>) returnType[0]) : in.readObject(
						(Class<?>) returnType[0], returnType[1])));
			} catch (ClassNotFoundException e) {
				throw new IOException(StringUtils.toString(
						"Read response data failed.", e));
			}
			break;
		case MarsCodec.RESPONSE_WITH_EXCEPTION:
			try {
				Object obj = in.readObject();
				if (obj instanceof Throwable == false)
					throw new IOException(
							"Response data error, expect Throwable, but get "
									+ obj);
				setException((Throwable) obj);
			} catch (ClassNotFoundException e) {
				throw new IOException(StringUtils.toString(
						"Read response data failed.", e));
			}
			break;
		default:
			throw new IOException(
					"Unknown result flag, expect '0' '1' '2', get " + flag);
		}
		return this;
	}

	@Override
	public void decode() throws Exception {
		if (!hasDecoded && channel != null && inputStream != null) {
			try {
				decode(channel, inputStream);
			} catch (Throwable e) {
				if (log.isWarnEnabled()) {
					log.warn("Decode rpc result failed: " + e.getMessage(), e);
				}
				response.setStatus(Response.CLIENT_ERROR);
				response.setErrorMessage(StringUtils.toString(e));
			} finally {
				hasDecoded = true;
			}
		}
	}

}
