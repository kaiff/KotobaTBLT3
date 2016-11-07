package com.github.orgs.kotobaminers.kotobaapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseManager {
	
	protected DatabaseManager() {
	}
	
	public abstract String getDatabase();
	public abstract String getUser();
	public abstract String getPass();

	public abstract void loadConfig();
	public synchronized Connection openConnection() {
		try {
			return DriverManager.getConnection(getDatabase() + "?useUnicode=true&characterEncoding=utf8", getUser(), getPass());
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void closeConnection(Connection connection) {
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void closeStatement(Statement statement) {
		try {
			if(statement != null && !statement.isClosed()) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void closeResultSet(ResultSet result) {
		try {
			if(result != null && !result.isClosed()) {
				result.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
