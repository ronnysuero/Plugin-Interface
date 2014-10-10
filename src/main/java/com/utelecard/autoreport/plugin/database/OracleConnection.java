/**
 * OracleConnection.java
 *
 * This class defines methods for connecting to Oracle via JDBC
 * 
 *@author Ronny Z. Suero
 * */

package com.utelecard.autoreport.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OracleConnection implements IDatabase
{
	
	private Connection connection;
	private final String connectionString = "jdbc:oracle:thin:@%s:%s:%s";
	private List<String> headers;
	private ResultSet resultSet;
	private ResultSetMetaData rsmd;
	private Statement stm;
	
	/**
	 * class constructor
	 * 
	 * @param host the host parameter defines the host of Connect to Oracle
	 * @param sid the sid parameter defines the Oracle System ID for the connection
	 * @param user the user parameter defines the username to Connect to Oracle
	 * @param password the password parameter defines the password to Connect to Oracle
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException, 
	 * @throws InstantiationException
	 * @throws IllegalAccessException 
	 * 
	 * */
	public OracleConnection(final String host, final String sid,
			final String user, final String password)
					throws ClassNotFoundException, SQLException, 
					InstantiationException, IllegalAccessException 
	{
		this.connect(
				String.format(this.connectionString, host, "1521", sid),
				user, password);
	}

	/**
	 * This method make the connection to the database
	 * @param cs the cs parameter is the query connection to connect to the database
	 * @param user the user parameter defines the username to Connect to Oracle
	 * @param password the password parameter defines the password to Connect to Oracle
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessExceptionClassNotFoundException, 
	 * @throws SQLException
	 * @return void
	 * */
	private void connect(final String cs, final String user,final String password) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, 
			SQLException 
	{
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		this.connection = DriverManager.getConnection(cs, user, password);

		if (this.connection != null) 
			this.stm = this.connection.createStatement();
	}

	@Override
	public void close() throws SQLException {
		if (!this.connection.isClosed())
			this.connection.close();
	}
	
	@Override
	public void execute(final String sql) throws SQLException 
	{
		this.resultSet = this.stm.executeQuery(sql);
		this.rsmd = this.resultSet.getMetaData();

		this.headers = new ArrayList<String>();

		for (int i = 1; i <= this.rsmd.getColumnCount(); i++) 
			this.headers.add(this.rsmd.getColumnName(i));
	}
	
	@Override
	public Connection getConnection() 
	{
		return this.connection;
	}
	
	@Override
	public List<String> getHeaders() 
	{
		return this.headers;
	}
	
	@Override
	public ResultSet getResultSet() 
	{
		return this.resultSet;
	}
	
	@Override
	public ResultSetMetaData getResultSetMetaData() 
	{
		return this.rsmd;
	}
}