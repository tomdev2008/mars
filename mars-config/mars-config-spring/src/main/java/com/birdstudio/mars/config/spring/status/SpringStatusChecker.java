package com.birdstudio.mars.config.spring.status;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;

import com.birdstudio.eirene.utils.extension.Activate;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.eirene.utils.status.Status;
import com.birdstudio.eirene.utils.status.StatusChecker;
import com.birdstudio.mars.config.spring.ServiceBean;

@Activate
public class SpringStatusChecker implements StatusChecker {

	private static final Logger logger = LoggerFactory
			.getLogger(SpringStatusChecker.class);

	@Override
	public Status check() {
		ApplicationContext context = ServiceBean.getSpringContext();
		if (context == null) {
			return new Status(Status.Level.UNKNOWN);
		}
		Status.Level level = Status.Level.OK;
		if (context instanceof Lifecycle) {
			if (((Lifecycle) context).isRunning()) {
				level = Status.Level.OK;
			} else {
				level = Status.Level.ERROR;
			}
		} else {
			level = Status.Level.UNKNOWN;
		}
		StringBuilder buf = new StringBuilder();
		try {
			Class<?> cls = context.getClass();
			Method method = null;
			while (cls != null && method == null) {
				try {
					method = cls.getDeclaredMethod("getConfigLocations",
							new Class<?>[0]);
				} catch (NoSuchMethodException t) {
					cls = cls.getSuperclass();
				}
			}
			if (method != null) {
				if (!method.isAccessible()) {
					method.setAccessible(true);
				}
				String[] configs = (String[]) method.invoke(context,
						new Object[0]);
				if (configs != null && configs.length > 0) {
					for (String config : configs) {
						if (buf.length() > 0) {
							buf.append(",");
						}
						buf.append(config);
					}
				}
			}
		} catch (Throwable t) {
			logger.warn(t.getMessage(), t);
		}
		return new Status(level, buf.toString());
	}

}