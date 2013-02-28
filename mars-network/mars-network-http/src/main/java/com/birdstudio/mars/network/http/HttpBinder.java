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
package com.birdstudio.mars.network.http;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.extension.Adaptive;
import com.birdstudio.eirene.utils.extension.SPI;

/**
 * HttpBinder
 * 
 * @author 
 */
@SPI("jetty")
public interface HttpBinder {
    
    /**
     * bind the server.
     * 
     * @param url server url.
     * @return server.
     */
    @Adaptive({Constants.SERVER_KEY})
    HttpServer bind(URL url, HttpHandler handler);
    
}