package com.birdstudio.mars.config.spring.status;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import com.birdstudio.eirene.utils.extension.Activate;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.eirene.utils.status.Status;
import com.birdstudio.eirene.utils.status.StatusChecker;
import com.birdstudio.mars.config.spring.ServiceBean;

@Activate
public class DataSourceStatusChecker implements StatusChecker {

	private static final Logger logger = LoggerFactory
			.getLogger(DataSourceStatusChecker.class);

	@Override
	public Status check() {
		ApplicationContext context = ServiceBean.getSpringContext();
		if (context == null) {
			return new Status(Status.Level.UNKNOWN);
		}
		Map<String, DataSource> dataSources = context.getBeansOfType(
				DataSource.class, false, false);
		if (dataSources == null || dataSources.size() == 0) {
			return new Status(Status.Level.UNKNOWN);
		}
		Status.Level level = Status.Level.OK;
		StringBuilder buf = new StringBuilder();
		for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
			DataSource dataSource = entry.getValue();
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append(entry.getKey());
			try {
				Connection connection = dataSource.getConnection();
				try {
					DatabaseMetaData metaData = connection.getMetaData();
					ResultSet resultSet = metaData.getTypeInfo();
					try {
						if (!resultSet.next()) {
							level = Status.Level.ERROR;
						}
					} finally {
						resultSet.close();
					}
					buf.append(metaData.getURL());
					buf.append("(");
					buf.append(metaData.getDatabaseProductName());
					buf.append("-");
					buf.append(metaData.getDatabaseProductVersion());
					buf.append(")");
				} finally {
					connection.close();
				}
			} catch (Throwable e) {
				logger.warn(e.getMessage(), e);
				return new Status(level, e.getMessage());
			}
		}
		return new Status(level, buf.toString());
	}

}