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
package com.birdstudio.mars.remoting.protocol.mars.status;

import java.util.Collection;

import com.birdstudio.eirene.utils.extension.Activate;
import com.birdstudio.eirene.utils.status.Status;
import com.birdstudio.eirene.utils.status.StatusChecker;
import com.birdstudio.mars.network.exchange.ExchangeServer;
import com.birdstudio.mars.remoting.protocol.mars.MarsProtocol;

/**
 * ServerStatusChecker
 * 
 * @author william.liangf
 */
@Activate
public class ServerStatusChecker implements StatusChecker {

    public Status check() {
        Collection<ExchangeServer> servers = MarsProtocol.getDubboProtocol().getServers();
        if (servers == null || servers.size() == 0) {
            return new Status(Status.Level.UNKNOWN);
        }
        Status.Level level = Status.Level.OK;
        StringBuilder buf = new StringBuilder();
        for (ExchangeServer server : servers) {
            if (! server.isBound()) {
                level = Status.Level.ERROR;
                buf.setLength(0);
                buf.append(server.getLocalAddress());
                break;
            }
            if (buf.length() > 0) {
                buf.append(",");
            }
            buf.append(server.getLocalAddress());
            buf.append("(clients:");
            buf.append(server.getChannels().size());
            buf.append(")");
        }
        return new Status(level, buf.toString());
    }

}