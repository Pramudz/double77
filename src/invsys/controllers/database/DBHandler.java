package invsys.controllers.database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.mysql.cj.jdbc.PreparedStatementWrapper;


public final class DBHandler {

	private static Connection conn = null;
	private static Statement stmt = null;
	private static DBHandler handler = null;

	private DBHandler() {
		createConnection();
	}

	void createConnection() {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("resources/dbConfig.properties"));
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String dbUrl = prop.getProperty("dbUrl");
		//	Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(dbUrl, user, password);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Database Error Occured",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static DBHandler getInstance() {
		if (handler == null) {
			handler = new DBHandler();
		}
		return handler;
	}

	public static Connection getConnection() {
		return conn;
	}

	public static ResultSet executeQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			return null;
		} finally {

		}
		return result;
	}

	public static boolean executeAction(String query) {
		try {

			stmt = conn.createStatement();
			stmt.execute(query);
			return true;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;

		} finally {

		}

	}

	public static PreparedStatementWrapper executeAfterbinding(String sql) {
		try {
			stmt = conn.prepareStatement(sql, PreparedStatementWrapper.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return (PreparedStatementWrapper) stmt;
	}
}
