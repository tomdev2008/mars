/*
 * Copyright 1999-2012 Birdstudio Group.
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
package com.birdstudio.mars.cluster.configurator.override;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.cluster.configurator.AbstractConfigurator;

/**
 * AbsentConfigurator
 * 
 * @author 
 */
public class OverrideConfigurator extends AbstractConfigurator {
    
    public OverrideConfigurator(URL url) {
        super(url);
    }

    public URL doConfigure(URL currentUrl, URL configUrl) {
        return currentUrl.addParameters(configUrl.getParameters());
    }

}
