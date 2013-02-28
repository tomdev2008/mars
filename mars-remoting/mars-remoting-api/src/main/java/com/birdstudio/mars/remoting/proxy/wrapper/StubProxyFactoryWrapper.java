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
package com.birdstudio.mars.remoting.proxy.wrapper;

import java.lang.reflect.Constructor;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.Version;
import com.birdstudio.eirene.utils.bytecode.Wrapper;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.eirene.utils.ConfigUtils;
import com.birdstudio.eirene.utils.NetUtils;
import com.birdstudio.eirene.utils.ReflectUtils;
import com.birdstudio.eirene.utils.StringUtils;
import com.birdstudio.mars.remoting.service.GenericService;
import com.birdstudio.mars.remoting.Exporter;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.Protocol;
import com.birdstudio.mars.remoting.ProxyFactory;
import com.birdstudio.mars.remoting.RpcException;

/**
 * StubProxyFactoryWrapper
 * 
 * @author 
 */
public class StubProxyFactoryWrapper implements ProxyFactory {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StubProxyFactoryWrapper.class);
    
    private final ProxyFactory proxyFactory;
    
    private Protocol protocol;
    
    public StubProxyFactoryWrapper(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }
    
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        T proxy = proxyFactory.getProxy(invoker);
        if (GenericService.class != invoker.getInterface()) {
            String stub = invoker.getUrl().getParameter(Constants.STUB_KEY, invoker.getUrl().getParameter(Constants.LOCAL_KEY));
            if (ConfigUtils.isNotEmpty(stub)) {
                Class<?> serviceType = invoker.getInterface();
                if (ConfigUtils.isDefault(stub)) {
                    if (invoker.getUrl().hasParameter(Constants.STUB_KEY)) {
                        stub = serviceType.getName() + "Stub";
                    } else {
                        stub = serviceType.getName() + "Local";
                    }
                }
                try {
                    Class<?> stubClass = ReflectUtils.forName(stub);
                    if (! serviceType.isAssignableFrom(stubClass)) {
                        throw new IllegalStateException("The stub implemention class " + stubClass.getName() + " not implement interface " + serviceType.getName());
                    }
                    try {
                        Constructor<?> constructor = ReflectUtils.findConstructor(stubClass, serviceType);
                        proxy = (T) constructor.newInstance(new Object[] {proxy});
                        //export stub service
                        URL url = invoker.getUrl();
                        if (url.getParameter(Constants.STUB_EVENT_KEY, Constants.DEFAULT_STUB_EVENT)){
                            url = url.addParameter(Constants.STUB_EVENT_METHODS_KEY, StringUtils.join(Wrapper.getWrapper(proxy.getClass()).getDeclaredMethodNames(), ","));
                            url = url.addParameter(Constants.IS_SERVER_KEY, Boolean.FALSE.toString());
                            try{
                                export(proxy, (Class)invoker.getInterface(), url);
                            }catch (Exception e) {
                                LOGGER.error("export a stub service error.", e);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("No such constructor \"public " + stubClass.getSimpleName() + "(" + serviceType.getName() + ")\" in stub implemention class " + stubClass.getName(), e);
                    }
                } catch (Throwable t) {
                    LOGGER.error("Failed to create stub implemention class " + stub + " in consumer " + NetUtils.getLocalHost() + " use mars version " + Version.getVersion() + ", cause: " + t.getMessage(), t);
                    // ignore
                }
            }
        }
        return proxy;
    }
    
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        return proxyFactory.getInvoker(proxy, type, url);
    }
    
    private <T> Exporter<T> export(T instance, Class<T> type, URL url) {
        return protocol.export(proxyFactory.getInvoker(instance, type, url));
    }
    
}