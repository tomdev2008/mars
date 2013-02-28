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
package com.birdstudio.mars.remoting;

import com.birdstudio.eirene.utils.Node;

/**
 * Invoker. (API/SPI, Prototype, ThreadSafe)
 * 
 * @see com.birdstudio.mars.remoting.Protocol#refer(Class,
 *      com.birdstudio.eirene.utils.URL)
 * @see com.birdstudio.mars.remoting.InvokerListener
 * @see com.birdstudio.mars.remoting.protocol.AbstractInvoker
 * @author 
 */
public interface Invoker<T> extends Node {

	/**
	 * get service interface.
	 * 
	 * @return service interface.
	 */
	Class<T> getInterface();

	/**
	 * invoke.
	 * 
	 * @param invocation
	 * @return result
	 * @throws RpcException
	 */
	Result invoke(Invocation invocation) throws RpcException;

}