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
package com.birdstudio.mars.network.telnet.support.command;

import com.birdstudio.eirene.utils.extension.Activate;
import com.birdstudio.mars.network.Channel;
import com.birdstudio.mars.network.telnet.TelnetHandler;
import com.birdstudio.mars.network.telnet.support.Help;

/**
 * ExitTelnetHandler
 * 
 * @author 
 */
@Activate
@Help(parameter = "", summary = "Exit the telnet.", detail = "Exit the telnet.")
public class ExitTelnetHandler implements TelnetHandler {

    public String telnet(Channel channel, String message) {
        channel.close();
        return null;
    }

}