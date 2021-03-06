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
package com.birdstudio.mars.network.transport.dispatcher.connection;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.network.ChannelHandler;
import com.birdstudio.mars.network.Dispatcher;

/**
 * connect disconnect 保证顺序.
 * 
 * @author chao.liuc
 */
public class ConnectionOrderedDispatcher implements Dispatcher {

    public static final String NAME = "connection";

    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return new ConnectionOrderedChannelHandler(handler, url);
    }

}