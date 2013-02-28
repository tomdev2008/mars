package com.birdstudio.mars.network.zookeeper.curator;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.network.zookeeper.ZookeeperClient;
import com.birdstudio.mars.network.zookeeper.ZookeeperTransporter;

public class CuratorZookeeperTransporter implements ZookeeperTransporter {

	public ZookeeperClient connect(URL url) {
		return new CuratorZookeeperClient(url);
	}

}
