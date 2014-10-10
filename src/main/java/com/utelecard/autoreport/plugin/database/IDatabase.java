/**
 * IDatabase.java
 *
 * This interfase defines methods to override in the classes to connect to the databases
 * 
 *@author Ronny Z. Suero
 * */
package com.utelecard.autoreport.plugin.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public interface IDatabase 
{
	/**
	 * This method close the connection to the database.
	 * @exception SQLException
	 * @return void 
	 * */
	public void close() throws SQLException;
	
	/**
	 * This method execute the Query Statement
	 * @param sql the sql parameter defines the query to be sent to the database
	 * @exception SQLException
	 * @return void
	 * */
	public void execute(String sql) throws SQLException;

	/**
	 * This method return the state of the Connection
	 * 
	 * @return The Connection
	 * */
	public Connection getConnection();

	/**
	 * This method return one list of the tables's Titles
	 * 
	 * @return The List<String>
	 * */
	public List<String> getHeaders();

	/**
	 * This method return the ResultSet of the query
	 * 
	 * @return The ResultSet
	 * */
	public ResultSet getResultSet();

	/**
	 * This method return the metadata of the tables
	 * 
	 * @return The ResultSetMetaData
	 * */
	public ResultSetMetaData getResultSetMetaData();
}
