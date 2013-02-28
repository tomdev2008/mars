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
import com.birdstudio.mars.remoting.Filter;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.Result;
import com.birdstudio.mars.remoting.RpcException;
import com.birdstudio.mars.remoting.RpcResult;

/**
 * EchoInvokerFilter
 * 
 * @author 
 */
@Activate(group = Constants.PROVIDER, order = -110000)
public class EchoFilter implements Filter {

	public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
		if(inv.getMethodName().equals(Constants.$ECHO) && inv.getArguments() != null && inv.getArguments().length == 1 )
			return new RpcResult(inv.getArguments()[0]);
		return invoker.invoke(inv);
	}

}