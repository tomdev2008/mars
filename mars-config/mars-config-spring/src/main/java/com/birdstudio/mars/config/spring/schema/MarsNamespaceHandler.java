package com.birdstudio.mars.config.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.birdstudio.eirene.utils.Version;
import com.birdstudio.mars.config.ApplicationConfig;
import com.birdstudio.mars.config.ConsumerConfig;
import com.birdstudio.mars.config.ModuleConfig;
import com.birdstudio.mars.config.MonitorConfig;
import com.birdstudio.mars.config.ProtocolConfig;
import com.birdstudio.mars.config.ProviderConfig;
import com.birdstudio.mars.config.RegistryConfig;
import com.birdstudio.mars.config.spring.AnnotationBean;
import com.birdstudio.mars.config.spring.ReferenceBean;
import com.birdstudio.mars.config.spring.ServiceBean;

public class MarsNamespaceHandler extends NamespaceHandlerSupport {

	static {
		Version.checkDuplicate(MarsNamespaceHandler.class);
	}

	@Override
	public void init() {
		registerBeanDefinitionParser("application",
				new MarsBeanDefinitionParser(ApplicationConfig.class, true));
		registerBeanDefinitionParser("module", new MarsBeanDefinitionParser(
				ModuleConfig.class, true));
		registerBeanDefinitionParser("registry", new MarsBeanDefinitionParser(
				RegistryConfig.class, true));
		registerBeanDefinitionParser("monitor", new MarsBeanDefinitionParser(
				MonitorConfig.class, true));
		registerBeanDefinitionParser("provider", new MarsBeanDefinitionParser(
				ProviderConfig.class, true));
		registerBeanDefinitionParser("consumer", new MarsBeanDefinitionParser(
				ConsumerConfig.class, true));
		registerBeanDefinitionParser("protocol", new MarsBeanDefinitionParser(
				ProtocolConfig.class, true));
		registerBeanDefinitionParser("service", new MarsBeanDefinitionParser(
				ServiceBean.class, true));
		registerBeanDefinitionParser("reference", new MarsBeanDefinitionParser(
				ReferenceBean.class, false));
		registerBeanDefinitionParser("annotation",
				new MarsBeanDefinitionParser(AnnotationBean.class, true));
	}

}