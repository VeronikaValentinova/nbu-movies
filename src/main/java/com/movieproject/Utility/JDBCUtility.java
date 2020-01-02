package com.movieproject.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtility {

	private static final String jdbcURL = "jdbc:mysql://localhost:3306/moviedatabase";
	private static final String jdbcUsername = "root";
	private static final String jdbcPassword = "root";
	
	// Oracle用
	// private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	// private static final String DB_URL = "jdbc:oracle:thin:@dbserver:1521:xe";
	// private static final String ID = "sys";
	// private static final String PASS = "orcl";
	
	
	
	public static void printSQLException(SQLException ex) {

	    for (Throwable e : ex) {
	        if (e instanceof SQLException) {
	       
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " +
	                    ((SQLException)e).getSQLState());

	                System.err.println("Error Code: " +
	                    ((SQLException)e).getErrorCode());

	                System.err.println("Message: " + e.getMessage());

	                Throwable t = ex.getCause();
	                while(t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	
	//Getting sql connection
	
	public static Connection getConnection() 
	{
		Connection connection = null;
		try 
		{
			connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
		}catch(Exception e){
			e.printStackTrace();
		}
		return connection;
	}
	//Closing sql connection
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	//Closing Statement
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
	
