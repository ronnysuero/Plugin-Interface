/**
 * SqlServerConnection.java
 *
 * This class defines methods for connecting to MS SQL Server via JDBC
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

public class SqlServerConnection implements IDatabase 
{
	private Connection connection;
	private final String connectionString = "jdbc:sqlserver://%s:%s;"
			+ "databaseName=%s;user=%s;password=%s";
	private List<String> headers;
	private ResultSet resultSet;
	private ResultSetMetaData rsmd;
	private Statement stm;

	/**
	 * class constructor
	 * 
	 * @param host the host parameter defines the host of Connect to MS SQL
	 * @param database the database parameter defines the name of the MS SQL's Database
	 * @param user the user parameter defines the username to Connect to MS SMQ
	 * @param password the password parameter defines the password to Connect to MS SQL
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException, 
	 * @throws InstantiationException
	 * @throws IllegalAccessException 
	 * 
	 * */
	public SqlServerConnection(final String host, final String database,
			final String user, final String password)
					throws ClassNotFoundException, SQLException, 
					InstantiationException, IllegalAccessException 
	{
		this.connect(String.format(this.connectionString, host, "1433",
				database, user, password));
	}

	@Override
	public void close() throws SQLException 
	{
		if (!this.connection.isClosed())
			this.connection.close();
	}

	/**
	 * This method make the connection to the database
	 * @param cs the cs parameter is the query connection to connect to the database
	 * @throws InstantiationException
	 * @throws IllegalAccessExceptionClassNotFoundException, 
	 * @throws SQLException
	 * @return void
	 * */
	private void connect(final String cs) throws ClassNotFoundException,
	SQLException, InstantiationException, IllegalAccessException 
	{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
		.newInstance();
		this.connection = DriverManager.getConnection(cs);

		if (this.connection != null) {
			this.stm = this.connection.createStatement();
		}
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
