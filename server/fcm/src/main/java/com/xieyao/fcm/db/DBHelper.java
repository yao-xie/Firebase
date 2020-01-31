package com.xieyao.fcm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xieyao.fcm.controller.PushNotificationController;

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

	public static void testDb() {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbPath));
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			statement.executeUpdate("drop table if exists person");
			statement.executeUpdate("create table person (id integer, name string)");
			statement.executeUpdate("insert into person values(1, 'Lion')");
			statement.executeUpdate("insert into person values(2, 'Tiger')");
			ResultSet rs = statement.executeQuery("select * from person");
			while (rs.next()) {
				// read the result set
				logger.error("name = " + rs.getString("name"));
				logger.error("id = " + rs.getInt("id"));
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
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

}
