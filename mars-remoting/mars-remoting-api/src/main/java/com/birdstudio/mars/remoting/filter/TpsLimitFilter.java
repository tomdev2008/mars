/*
 * Copyright 1999-2012 Birdstudio Group.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.birdstudio.mars.remoting.filter;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.extension.Activate;
import com.birdstudio.mars.remoting.filter.tps.DefaultTPSLimiter;
import com.birdstudio.mars.remoting.filter.tps.TPSLimiter;
import com.birdstudio.mars.remoting.Filter;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.Result;
import com.birdstudio.mars.remoting.RpcException;

/**
 * 限制 service 或方法的 tps.
 *
 * @author <a href="mailto:gang.lvg@birdstudio-inc.com">kimi</a>
 */
@Activate(group = Constants.PROVIDER, value = Constants.TPS_LIMIT_RATE_KEY)
public class TpsLimitFilter implements Filter {

    private final TPSLimiter tpsLimiter = new DefaultTPSLimiter();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        if (!tpsLimiter.isAllowable(invoker.getUrl(), invocation)) {
            throw new RpcException(
                    new StringBuilder(64)
                            .append("Failed to invoke service ")
                            .append(invoker.getInterface().getName())
                            .append(".")
                            .append(invocation.getMethodName())
                            .append(" because exceed max service tps.")
                            .toString());
        }

        return invoker.invoke(invocation);
    }

}
