/**
 * MySQLConnection.java
 *
 * This class defines methods for connecting to MySQL via JDBC
 * 
 *@author Ronny Z. Suero
 * */
package com.utelecard.autoreport.plugin.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

public class MySQLConnection implements IDatabase 
{
	private Connection connection;
	private List<String> headers;
	private ResultSet resultSet;
	private ResultSetMetaData rsmd;
	private Statement stm;

	/**
	 * class constructor
	 * 
	 * @param host the host parameter defines the host of Connect to MySQL
	 * @param port the port parameter defines the port of Connect to MySQL
	 * @param database the database parameter defines the name of the MySQL's Database
	 * @param user the user parameter defines the username to Connect to MySQL
	 * @param password the password parameter defines the password to Connect to MySQL
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException, 
	 * @throws InstantiationException
	 * @throws IllegalAccessException 
	 * */
	public MySQLConnection(final String host, final Integer port,
			final String database, final String user, final String password)
					throws ClassNotFoundException, SQLException,
					InstantiationException, IllegalAccessException 
	{
		this.connect(String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
				host, port, database, user, password));
	}

	/**
	 * class constructor
	 * 
	 * @param host the host parameter defines the host of Connect to MySQL
	 * @param database the database parameter defines the name of the MySQL's Database
	 * @param user the user parameter defines the username to Connect to MySQL
	 * @param password the password parameter defines the password to Connect to MySQL
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException, 
	 * @throws InstantiationException
	 * @throws IllegalAccessException 
	 * */
	public MySQLConnection(final String host, final String database,
			final String user, final String password)
					throws ClassNotFoundException, SQLException,
					InstantiationException, IllegalAccessException 
	{
		this.connect(String.format(
				"jdbc:mysql://%s:3306/%s?user=%s&password=%s", host, database,
				user, password));
	}

	@Override
	public void close() throws SQLException {
		if (!this.connection.isClosed())
			this.connection.close();
	}

	/**
	 * This method make the connection to the database
	 * @param connectionString the connectionString parameter is the query connection to connect to the database
	 * @throws InstantiationException
	 * @throws IllegalAccessExceptionClassNotFoundException, 
	 * @throws SQLException
	 * @return void
	 * */
	private void connect(final String connectionString)
			throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException 
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.connection = (Connection) DriverManager
				.getConnection(connectionString);

		if (this.connection != null) 
			this.stm = this.connection.createStatement();
	}

	@Override
	public void execute(final String sql) throws SQLException 
	{
		this.resultSet = this.stm.executeQuery(sql);
		this.rsmd = this.resultSet.getMetaData();
		this.headers = new ArrayList<String>();

		while (this.resultSet.next()) 
		{
			for (int i = 1; i <= this.rsmd.getColumnCount(); i++)
				this.headers.add(this.rsmd.getColumnName(i));
		}
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public List<String> getHeaders() {
		return this.headers;
	}

	@Override
	public ResultSet getResultSet() {
		return this.resultSet;
	}

	@Override
	public ResultSetMetaData getResultSetMetaData() {
		return this.rsmd;
	}

	public void setResultSet(final ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}
