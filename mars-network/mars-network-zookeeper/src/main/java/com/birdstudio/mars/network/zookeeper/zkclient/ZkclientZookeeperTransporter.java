package com.birdstudio.mars.network.zookeeper.zkclient;

import com.birdstudio.eirene.utils.URL;
import com.birdstudio.mars.network.zookeeper.ZookeeperClient;
import com.birdstudio.mars.network.zookeeper.ZookeeperTransporter;

public class ZkclientZookeeperTransporter implements ZookeeperTransporter {

	public ZookeeperClient connect(URL url) {
		return new ZkclientZookeeperClient(url);
	}

}
