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
package com.birdstudio.mars.remoting.protocol.mars.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.NetUtils;
import com.birdstudio.mars.container.page.Menu;
import com.birdstudio.mars.container.page.Page;
import com.birdstudio.mars.container.page.PageHandler;
import com.birdstudio.mars.network.exchange.ExchangeServer;
import com.birdstudio.mars.remoting.protocol.mars.MarsProtocol;

/**
 * ServersPageHandler
 * 
 * @author william.liangf
 */
@Menu(name = "Servers", desc="Show exported service servers.", order = 14000)
public class ServersPageHandler implements PageHandler {

    public Page handle(URL url) {
        List<List<String>> rows = new ArrayList<List<String>>();
        Collection<ExchangeServer> servers = MarsProtocol.getDubboProtocol().getServers();
        int clientCount = 0;
        if (servers != null && servers.size() > 0) {
            for (ExchangeServer s : servers) {
                List<String> row = new ArrayList<String>();
                String address = s.getUrl().getAddress();
                row.add(NetUtils.getHostName(address) + "/" + address);
                int clientSize = s.getExchangeChannels().size();
                clientCount += clientSize;
                row.add("<a href=\"clients.html?port=" + s.getUrl().getPort() + "\">Clients(" + clientSize + ")</a>");
                rows.add(row);
            }
        }
        return new Page("Servers", "Servers (" + rows.size() + ")", new String[]{"Server Address:", "Clients(" + clientCount + ")"}, rows);
    }

}