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
import com.birdstudio.eirene.utils.StringUtils;
import com.birdstudio.mars.network.Channel;
import com.birdstudio.mars.network.telnet.TelnetHandler;
import com.birdstudio.mars.network.telnet.support.Help;

/**
 * ClearTelnetHandler
 * 
 * @author 
 */
@Activate
@Help(parameter = "[lines]", summary = "Clear screen.", detail = "Clear screen.")
public class ClearTelnetHandler implements TelnetHandler {

    public String telnet(Channel channel, String message) {
        int lines = 100;
        if (message.length() > 0) {
            if (! StringUtils.isInteger(message)) {
                return "Illegal lines " + message + ", must be integer.";
            }
            lines = Integer.parseInt(message);
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < lines; i ++) {
            buf.append("\r\n");
        }
        return buf.toString();
    }

}