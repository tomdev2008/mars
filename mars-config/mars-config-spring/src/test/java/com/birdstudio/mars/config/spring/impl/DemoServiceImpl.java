package com.birdstudio.mars.config.spring.impl;

import com.birdstudio.mars.config.spring.api.Box;
import com.birdstudio.mars.config.spring.api.DemoService;

public class DemoServiceImpl implements DemoService {

	private String prefix = "say:";

	@Override
	public String sayName(String name) {
		return prefix + name;
	}

	@Override
	public Box getBox() {
		return null;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}