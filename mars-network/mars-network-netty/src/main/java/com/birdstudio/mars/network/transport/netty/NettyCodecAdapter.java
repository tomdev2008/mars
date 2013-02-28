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
package com.birdstudio.mars.network.transport.netty;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.network.Codec;
import com.birdstudio.mars.network.buffer.DynamicChannelBuffer;

/**
 * NettyCodecAdapter.
 * 
 * @author qian.lei
 */
final class NettyCodecAdapter {

	private final ChannelHandler encoder = new InternalEncoder();

	private final ChannelHandler decoder = new InternalDecoder();

	private final Codec codec;

	private final URL url;

	private final int bufferSize;

	private final com.birdstudio.mars.network.ChannelHandler handler;

	public NettyCodecAdapter(Codec codec, URL url,
			com.birdstudio.mars.network.ChannelHandler handler) {
		this.codec = codec;
		this.url = url;
		this.handler = handler;
		int b = url.getPositiveParameter(Constants.BUFFER_KEY,
				Constants.DEFAULT_BUFFER_SIZE);
		this.bufferSize = b >= Constants.MIN_BUFFER_SIZE
				&& b <= Constants.MAX_BUFFER_SIZE ? b
				: Constants.DEFAULT_BUFFER_SIZE;
	}

	public ChannelHandler getEncoder() {
		return encoder;
	}

	public ChannelHandler getDecoder() {
		return decoder;
	}

	@Sharable
	private class InternalEncoder extends OneToOneEncoder {

		@Override
		protected Object encode(ChannelHandlerContext ctx, Channel ch,
				Object msg) throws Exception {
			com.birdstudio.mars.network.buffer.ChannelBuffer buffer = com.birdstudio.mars.network.buffer.ChannelBuffers
					.dynamicBuffer(1024);
			NettyChannel channel = NettyChannel.getOrAddChannel(ch, url,
					handler);
			try {
				codec.encode(channel, buffer, msg);
			} finally {
				NettyChannel.removeChannelIfDisconnected(ch);
			}
			return ChannelBuffers.wrappedBuffer(buffer.toByteBuffer());
		}
	}

	private class InternalDecoder extends SimpleChannelUpstreamHandler {

		private com.birdstudio.mars.network.buffer.ChannelBuffer buffer = com.birdstudio.mars.network.buffer.ChannelBuffers.EMPTY_BUFFER;

		@Override
		public void messageReceived(ChannelHandlerContext ctx,
				MessageEvent event) throws Exception {
			Object o = event.getMessage();
			if (!(o instanceof ChannelBuffer)) {
				ctx.sendUpstream(event);
				return;
			}

			ChannelBuffer input = (ChannelBuffer) o;
			int readable = input.readableBytes();
			if (readable <= 0) {
				return;
			}

			com.birdstudio.mars.network.buffer.ChannelBuffer message;
			if (buffer.readable()) {
				if (buffer instanceof DynamicChannelBuffer) {
					buffer.writeBytes(input.toByteBuffer());
					message = buffer;
				} else {
					int size = buffer.readableBytes() + input.readableBytes();
					message = com.birdstudio.mars.network.buffer.ChannelBuffers
							.dynamicBuffer(size > bufferSize ? size
									: bufferSize);
					message.writeBytes(buffer, buffer.readableBytes());
					message.writeBytes(input.toByteBuffer());
				}
			} else {
				message = com.birdstudio.mars.network.buffer.ChannelBuffers
						.wrappedBuffer(input.toByteBuffer());
			}

			NettyChannel channel = NettyChannel.getOrAddChannel(
					ctx.getChannel(), url, handler);
			Object msg;
			int saveReaderIndex;

			try {
				// decode object.
				do {
					saveReaderIndex = message.readerIndex();
					try {
						msg = codec.decode(channel, message);
					} catch (IOException e) {
						buffer = com.birdstudio.mars.network.buffer.ChannelBuffers.EMPTY_BUFFER;
						throw e;
					}
					if (msg == Codec.DecodeResult.NEED_MORE_INPUT) {
						message.readerIndex(saveReaderIndex);
						break;
					} else {
						if (saveReaderIndex == message.readerIndex()) {
							buffer = com.birdstudio.mars.network.buffer.ChannelBuffers.EMPTY_BUFFER;
							throw new IOException("Decode without read data.");
						}
						if (msg != null) {
							Channels.fireMessageReceived(ctx, msg,
									event.getRemoteAddress());
						}
					}
				} while (message.readable());
			} finally {
				if (message.readable()) {
					message.discardReadBytes();
					buffer = message;
				} else {
					buffer = com.birdstudio.mars.network.buffer.ChannelBuffers.EMPTY_BUFFER;
				}
				NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			ctx.sendUpstream(e);
		}
	}
}