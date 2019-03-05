package com.sys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {	

	public Connection getConnection() {		
		return makeConnection();
	}

	private Connection makeConnection(){
		Connection conn = null;        
		String url = "jdbc:mariadb://localhost:3306/dbname";		
        String id  = "";
        String pw  = "";

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return conn;
	}
}