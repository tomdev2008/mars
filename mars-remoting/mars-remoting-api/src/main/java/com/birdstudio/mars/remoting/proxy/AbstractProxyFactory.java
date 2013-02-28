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
package com.birdstudio.mars.remoting.proxy;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.ReflectUtils;
import com.birdstudio.mars.remoting.service.EchoService;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.ProxyFactory;
import com.birdstudio.mars.remoting.RpcException;

/**
 * AbstractProxyFactory
 * 
 * @author 
 */
public abstract class AbstractProxyFactory implements ProxyFactory {

    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        Class<?>[] interfaces = null;
        String config = invoker.getUrl().getParameter("interfaces");
        if (config != null && config.length() > 0) {
            String[] types = Constants.COMMA_SPLIT_PATTERN.split(config);
            if (types != null && types.length > 0) {
                interfaces = new Class<?>[types.length + 2];
                interfaces[0] = invoker.getInterface();
                interfaces[1] = EchoService.class;
                for (int i = 0; i < types.length; i ++) {
                    interfaces[i + 1] = ReflectUtils.forName(types[i]);
                }
            }
        }
        if (interfaces == null) {
            interfaces = new Class<?>[] {invoker.getInterface(), EchoService.class};
        }
        return getProxy(invoker, interfaces);
    }
    
    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);

}