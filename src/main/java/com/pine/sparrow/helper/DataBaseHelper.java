package com.pine.sparrow.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pine.sparrow.config.ConfigHelper;
import com.pine.sparrow.exception.SparrowException;

public class DataBaseHelper {

	private static Logger logger = LoggerFactory.getLogger(DataBaseHelper.class);

	private static ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();

	public static Connection getConnection() {
		Connection conn = connContainer.get();

		try {
			if (Objects.isNull(conn)) {
				Class.forName(ConfigHelper.getJdbcDriver());
				conn = DriverManager.getConnection(ConfigHelper.getJdbcUrl(), ConfigHelper.getJdbcUserName(),
						ConfigHelper.getJdbcPassword());
			}
		} catch (Exception e) {
			logger.error("fail to get connection", e);
			throw new SparrowException("fail to get connection");
		} finally {
			connContainer.set(conn);
		}
		return conn;
	}

	public static void closeConnection() {
		Connection conn = connContainer.get();

		try {
			if (!Objects.isNull(conn)) {
				conn.close();
			}
		} catch (Exception e) {
			logger.error("fail to close connection", e);
			throw new SparrowException("fail to close connection");
		} finally {
			connContainer.remove();
		}
	}

	/**
	 * 开启事物，设置自动提交为false
	 */
	public static void beginTransaction() {
		Connection conn = getConnection();

		if (!Objects.isNull(conn)) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error("fail to begin transaction", e);
				throw new SparrowException("fail to begin transaction", e);
			} finally {
				connContainer.set(conn);
			}
		}
	}

	public static void commitTransaction() {
		Connection conn = getConnection();
		if (!Objects.isNull(conn)) {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				logger.error("fail to commit transaction", e);
				throw new SparrowException("fail to commit transaction", e);
			} finally {
				connContainer.remove();
			}
		}
	}

	public static void rollbackTransaction() {
		Connection conn = getConnection();

		if (!Objects.isNull(conn)) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				logger.error("fail to rollback transaction", e);
				throw new SparrowException("fail to rollback transaction", e);
			} finally {
				connContainer.remove();
			}
		}
	}

}
