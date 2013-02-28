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

import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.RpcException;
import com.birdstudio.mars.cluster.Cluster;
import com.birdstudio.mars.cluster.Directory;

/**
 * 失败自动恢复，后台记录失败请求，定时重发，通常用于消息通知操作。
 * 
 * <a href="http://en.wikipedia.org/wiki/Failback">Failback</a>
 * 
 * @author 
 */
public class FailbackCluster implements Cluster {

    public final static String NAME = "failback";    

    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new FailbackClusterInvoker<T>(directory);
    }

}