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
package com.birdstudio.mars.registry;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.extension.Adaptive;
import com.birdstudio.eirene.utils.extension.SPI;

/**
 * RegistryFactory. (SPI, Singleton, ThreadSafe)
 * 
 * @see com.birdstudio.mars.registry.support.AbstractRegistryFactory
 * @author 
 */
@SPI("mars")
public interface RegistryFactory {

	/**
	 * 连接注册中心.
	 * 
	 * 连接注册中心需处理契约：<br>
	 * 1. 当设置check=false时表示不检查连接，否则在连接不上时抛出异常。<br>
	 * 2. 支持URL上的username:password权限认证。<br>
	 * 3. 支持backup=10.20.153.10备选注册中心集群地址。<br>
	 * 4. 支持file=registry.cache本地磁盘文件缓存。<br>
	 * 5. 支持timeout=1000请求超时设置。<br>
	 * 6. 支持session=60000会话超时或过期设置。<br>
	 * 
	 * @param url
	 *            注册中心地址，不允许为空
	 * @return 注册中心引用，总不返回空
	 */
	@Adaptive({ "protocol" })
	Registry getRegistry(URL url);

}