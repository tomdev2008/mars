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
package com.birdstudio.mars.registry.zookeeper;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.registry.Registry;
import com.birdstudio.mars.registry.support.AbstractRegistryFactory;
import com.birdstudio.mars.network.zookeeper.ZookeeperTransporter;

/**
 * ZookeeperRegistryFactory.
 * 
 * @author 
 */
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {
	
	private ZookeeperTransporter zookeeperTransporter;

    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
		this.zookeeperTransporter = zookeeperTransporter;
	}

	public Registry createRegistry(URL url) {
        return new ZookeeperRegistry(url, zookeeperTransporter);
    }

}