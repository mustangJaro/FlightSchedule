package com.db;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.flightSchedule.core.ExceptionHandler;

public class Database {
	private String connectionString = "";
	private Connection connection = null;

	public Database(String serverName, String dbName){
		connectionString =  
                "jdbc:sqlserver://" + serverName + ";" 
                + "database=" + dbName + ";"
                + "Integratedsecurity=true;"
                + "loginTimeout=5;";  


	}
	public Connection connect(){

        try {  
            connection = DriverManager.getConnection(connectionString);  

        }  
        catch (Exception e) {  
        	ExceptionHandler.logException("Failure to establish a connection", e);
        }  
        return connection;
		
	}
	
	public void close(){
		if(connection != null)
        	try { connection.close(); } catch(Exception e) {}  
	}
	
	public ResultSet executeQuery(String sql) throws SQLException{
		if(connection != null){
			Statement st = connection.createStatement();
			return st.executeQuery(sql);
		}else{
			throw new SQLException("connection is null on executeQuery");
		}
	}
	
	public int executeUpdate(String sql) throws SQLException{
		if(connection != null){
			Statement st = connection.createStatement();
			return st.executeUpdate(sql);
		}else{
			throw new SQLException("connection is null on executeUpdate");
		}
	}
	
	public BigInteger executeUpdateWithId(String sql) throws SQLException{
		if(connection != null){
			Statement st = connection.createStatement();
			int result = st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if(result > 0){
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				return new BigInteger(rs.getString(1));
			}else{
				return BigInteger.ZERO;
			}
		}else{
			throw new SQLException("connection is null on executeUpdateWithId");
		}
	}
}
