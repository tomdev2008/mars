package com.birdstudio.mars.network.transport;

import com.birdstudio.mars.network.Channel;
import com.birdstudio.mars.network.ChannelHandler;
import com.birdstudio.mars.network.RemotingException;
import com.birdstudio.mars.network.exchange.support.MultiMessage;

/**
 * @author <a href="mailto:gang.lvg@birdstudio-inc.com">kimi</a>
 * @see MultiMessage
 */
public class MultiMessageHandler extends AbstractChannelHandlerDelegate {

	public MultiMessageHandler(ChannelHandler handler) {
		super(handler);
	}

	@Override
	public void received(Channel channel, Object message)
			throws RemotingException {
		if (message instanceof MultiMessage) {
			MultiMessage list = (MultiMessage) message;
			for (Object obj : list) {
				handler.received(channel, obj);
			}
		} else {
			handler.received(channel, message);
		}
	}
}
