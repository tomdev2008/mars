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
package com.birdstudio.mars.cluster;

import java.util.List;

import com.birdstudio.eirene.utils.Node;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.RpcException;

/**
 * Directory. (SPI, Prototype, ThreadSafe)
 * 
 * <a href="http://en.wikipedia.org/wiki/Directory_service">Directory Service</a>
 * 
 * @see com.birdstudio.mars.cluster.Cluster#join(Directory)
 * @author 
 */
public interface Directory<T> extends Node {
    
    /**
     * get service type.
     * 
     * @return service type.
     */
    Class<T> getInterface();

    /**
     * list invokers.
     * 
     * @return invokers
     */
    List<Invoker<T>> list(Invocation invocation) throws RpcException;
    
}