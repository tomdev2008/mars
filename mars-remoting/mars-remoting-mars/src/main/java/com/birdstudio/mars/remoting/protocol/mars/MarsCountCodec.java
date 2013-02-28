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

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.mars.network.Channel;
import com.birdstudio.mars.network.Codec;
import com.birdstudio.mars.network.buffer.ChannelBuffer;
import com.birdstudio.mars.network.exchange.Request;
import com.birdstudio.mars.network.exchange.Response;
import com.birdstudio.mars.network.exchange.support.MultiMessage;
import com.birdstudio.mars.remoting.RpcInvocation;
import com.birdstudio.mars.remoting.RpcResult;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public final class MarsCountCodec implements Codec {

	private final MarsCodec codec = new MarsCodec();

	@Override
	public void encode(Channel channel, ChannelBuffer buffer, Object msg)
			throws IOException {
		codec.encode(channel, buffer, msg);
	}

	@Override
	public Object decode(Channel channel, ChannelBuffer buffer)
			throws IOException {
		int save = buffer.readerIndex();
		MultiMessage result = MultiMessage.create();
		do {
			Object obj = codec.decode(channel, buffer);
			if (Codec.DecodeResult.NEED_MORE_INPUT == obj) {
				buffer.readerIndex(save);
				break;
			} else {
				result.addMessage(obj);
				logMessageLength(obj, buffer.readerIndex() - save);
				save = buffer.readerIndex();
			}
		} while (true);
		if (result.isEmpty()) {
			return Codec.DecodeResult.NEED_MORE_INPUT;
		}
		if (result.size() == 1) {
			return result.get(0);
		}
		return result;
	}

	private void logMessageLength(Object result, int bytes) {
		if (bytes <= 0) {
			return;
		}
		if (result instanceof Request) {
			try {
				((RpcInvocation) ((Request) result).getData()).setAttachment(
						Constants.INPUT_KEY, String.valueOf(bytes));
			} catch (Throwable e) {
				/* ignore */
			}
		} else if (result instanceof Response) {
			try {
				((RpcResult) ((Response) result).getResult()).setAttachment(
						Constants.OUTPUT_KEY, String.valueOf(bytes));
			} catch (Throwable e) {
				/* ignore */
			}
		}
	}

}
