package com.xieyao.fcm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xieyao.fcm.controller.PushNotificationController;
import com.xieyao.fcm.model.device.DeviceRegistrationRequest;

@Component
public class DBHelper {

	private static Logger logger = LoggerFactory.getLogger(DBHelper.class);

	private static String dbPath;

	private DBHelper() {
	}

	@Value("${app.db}")
	public void setDatabasePath(String dbPath) {
		this.dbPath = dbPath;
	}

	/**
	 * create a database Connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbPath));
	}

	/**
	 * reate a database Statement
	 * 
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private static Statement getStatement(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		return statement;
	}

	private static void createTableIfNotExsits(Statement statement) throws SQLException {
		if (null != statement) {
			// statement.executeUpdate("drop table if exists devices");
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS devices (deviceId TEXT PRIMARY KEY NOT NULL, token TEXT)");
		}
	}

	private static void printDevices(Statement statement) throws SQLException {
		ResultSet rs = statement.executeQuery("select * from devices");
		while (rs.next()) {// read the result set
			logger.error("deviceId = " + rs.getString("deviceId"));
			logger.error("token = " + rs.getString("token"));
		}
	}

	public static void testDb() {
		Connection connection = null;
		try {
			// create a database connection
			connection = getConnection();
			Statement statement = getStatement(connection);
			createTableIfNotExsits(statement);
			statement.executeUpdate("INSERT OR REPLACE INTO devices VALUES('deviceId1', 'token1')");
			statement.executeUpdate("INSERT OR REPLACE INTO devices VALUES('deviceId2', 'token2')");
			printDevices(statement);
		} catch (SQLException e) {
			// if the error message is "out of memory", it probably means no database file
			// is found
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * insert the device information into database
	 * 
	 * @param device
	 */
	public static boolean registerDevice(DeviceRegistrationRequest device) {
		if (null != device && !TextUtils.isEmpty(device.getDeviceId()) && !TextUtils.isEmpty(device.getToken())) {
			Connection connection = null;
			try {
				connection = getConnection();
				Statement statement = getStatement(connection);
				createTableIfNotExsits(statement);
				String update = String.format("INSERT OR REPLACE INTO devices VALUES('%s', '%s')", device.getDeviceId(),
						device.getToken());
				statement.executeUpdate(update);
				printDevices(statement);
				return true;
			} catch (SQLException e) {
				logger.error(e.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				try {
					if (connection != null)
						connection.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return false;
	}

}
