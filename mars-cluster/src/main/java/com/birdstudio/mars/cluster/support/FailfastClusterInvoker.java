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
package com.birdstudio.mars.cluster.support;

import java.util.List;

import com.birdstudio.eirene.utils.Version;
import com.birdstudio.eirene.utils.NetUtils;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.Result;
import com.birdstudio.mars.remoting.RpcException;
import com.birdstudio.mars.cluster.Directory;
import com.birdstudio.mars.cluster.LoadBalance;

/**
 * 快速失败，只发起一次调用，失败立即报错，通常用于非幂等性的写操作。
 * 
 * <a href="http://en.wikipedia.org/wiki/Fail-fast">Fail-fast</a>
 * 
 * @author 
 * @author chao.liuc
 */
public class FailfastClusterInvoker<T> extends AbstractClusterInvoker<T>{

    public FailfastClusterInvoker(Directory<T> directory) {
        super(directory);
    }
    
    public Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
        checkInvokers(invokers, invocation);
        Invoker<T> invoker = select(loadbalance, invocation, invokers, null);
        try {
            return invoker.invoke(invocation);
        } catch (Throwable e) {
            if (e instanceof RpcException && ((RpcException)e).isBiz()) { // biz exception.
                throw (RpcException) e;
            }
            throw new RpcException(e instanceof RpcException ? ((RpcException)e).getCode() : 0, "Failfast invoke providers " + invoker.getUrl() + " " + loadbalance.getClass().getSimpleName() + " select from all providers " + invokers + " for service " + getInterface().getName() + " method " + invocation.getMethodName() + " on consumer " + NetUtils.getLocalHost() + " use mars version " + Version.getVersion() + ", but no luck to perform the invocation. Last error is: " + e.getMessage(), e.getCause() != null ? e.getCause() : e);
        }
    }
}