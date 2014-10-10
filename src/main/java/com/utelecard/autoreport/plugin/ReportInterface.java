/**
 * ReportInteface.java
 *
 * This interfase defines methods to make the report
 * 
 *@author Ronny Z. Suero
 * */
package com.utelecard.autoreport.plugin;

import java.util.logging.Logger;

public interface ReportInterface extends Runnable
{
	/**
	 * This method get the report name
	 * @return The name of the report
	 * */
	public String getReportName();

	/**
	 * This method get the report name to execute
	 * @return The name of the report to execute
	 * */
	public String getReportNameToExecute();

	/**
	 * This method that load the report
	 * @param resources the resources parameter defines the resource to the report
	 * @param log the log parameter defines the class to make the log file
	 * @return void
	 * */
	public void load(String resources, Logger log);
	
	@Override
	public void run();
}