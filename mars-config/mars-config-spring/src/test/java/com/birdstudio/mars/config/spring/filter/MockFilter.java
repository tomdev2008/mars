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
package com.birdstudio.mars.config.spring.filter;

import com.birdstudio.mars.cluster.LoadBalance;
import com.birdstudio.mars.remoting.Filter;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.Protocol;
import com.birdstudio.mars.remoting.Result;
import com.birdstudio.mars.remoting.RpcException;

/**
 * MockFilter
 * 
 * @author 
 */
public class MockFilter implements Filter {

	private LoadBalance loadBalance;

	private Protocol protocol;

	private MockDao mockDao;

	public MockDao getMockDao() {
		return mockDao;
	}

	public void setMockDao(MockDao mockDao) {
		this.mockDao = mockDao;
	}

	public LoadBalance getLoadBalance() {
		return loadBalance;
	}

	public void setLoadBalance(LoadBalance loadBalance) {
		this.loadBalance = loadBalance;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		return invoker.invoke(invocation);
	}

}
