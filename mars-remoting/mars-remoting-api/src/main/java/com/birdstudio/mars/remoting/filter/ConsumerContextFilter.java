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
package com.birdstudio.mars.remoting.filter;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.extension.Activate;
import com.birdstudio.eirene.utils.NetUtils;
import com.birdstudio.mars.remoting.Filter;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.Result;
import com.birdstudio.mars.remoting.RpcContext;
import com.birdstudio.mars.remoting.RpcException;
import com.birdstudio.mars.remoting.RpcInvocation;

/**
 * ConsumerContextInvokerFilter
 * 
 * @author 
 */
@Activate(group = Constants.CONSUMER, order = -10000)
public class ConsumerContextFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext()
                .setInvoker(invoker)
                .setInvocation(invocation)
                .setLocalAddress(NetUtils.getLocalHost(), 0)
                .setRemoteAddress(invoker.getUrl().getHost(), 
                                  invoker.getUrl().getPort());
        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation)invocation).setInvoker(invoker);
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            RpcContext.getContext().clearAttachments();
        }
    }

}