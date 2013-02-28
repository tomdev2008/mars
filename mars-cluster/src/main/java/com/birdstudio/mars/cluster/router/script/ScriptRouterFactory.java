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
package com.birdstudio.mars.cluster.router.script;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.cluster.Router;
import com.birdstudio.mars.cluster.RouterFactory;

/**
 * ScriptRouterFactory
 * 
 * Script Router Factory用到的URL形如：
 * <ol>
 * <li> script://registyAddress?type=js&rule=xxxx
 * <li> script:///path/to/routerfile.js?type=js&rule=xxxx
 * <li> script://D:\path\to\routerfile.js?type=js&rule=xxxx
 * <li> script://C:/path/to/routerfile.js?type=js&rule=xxxx
 * </ol>
 * URL的Host一段包含的是Script Router内容的来源，Registry、File etc
 * 
 * @author 
 */
public class ScriptRouterFactory implements RouterFactory {
    
    public static final String NAME = "script";

    public Router getRouter(URL url) {
        return new ScriptRouter(url);
    }

}