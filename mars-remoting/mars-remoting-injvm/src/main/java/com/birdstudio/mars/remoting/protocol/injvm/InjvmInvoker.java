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
package com.birdstudio.mars.remoting.protocol.injvm;

import java.util.Map;

import com.birdstudio.eirene.utils.NetUtils;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.remoting.Exporter;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Result;
import com.birdstudio.mars.remoting.RpcContext;
import com.birdstudio.mars.remoting.RpcException;
import com.birdstudio.mars.remoting.protocol.AbstractInvoker;

/**
 * InjvmInvoker
 * 
 * @author 
 */
class InjvmInvoker<T> extends AbstractInvoker<T> {

	private final String key;

	private final Map<String, Exporter<?>> exporterMap;

	InjvmInvoker(Class<T> type, URL url, String key,
			Map<String, Exporter<?>> exporterMap) {
		super(type, url);
		this.key = key;
		this.exporterMap = exporterMap;
	}

	@Override
	public boolean isAvailable() {
		InjvmExporter<?> exporter = (InjvmExporter<?>) exporterMap.get(key);
		if (exporter == null) {
			return false;
		} else {
			return super.isAvailable();
		}
	}

	@Override
	public Result doInvoke(Invocation invocation) throws Throwable {
		Exporter<?> exporter = InjvmProtocol.getExporter(exporterMap, getUrl());
		if (exporter == null) {
			throw new RpcException("Service [" + key + "] not found.");
		}
		RpcContext.getContext().setRemoteAddress(NetUtils.LOCALHOST, 0);
		return exporter.getInvoker().invoke(invocation);
	}
}