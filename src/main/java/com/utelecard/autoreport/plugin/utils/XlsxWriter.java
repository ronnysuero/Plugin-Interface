/**
 * XlsxWriter.java
 *
 * This class defines methods to make the excel files
 *
 *@author Ronny Z. Suero
 * */
package com.utelecard.autoreport.plugin.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class XlsxWriter
{
	protected Map<Integer, Integer> columnsWidth;
	protected WritableWorkbook excel;
	private WritableFont font;
	private WritableCellFormat format;
	protected List<String> headers;
	protected WritableSheet sheet;

	/**
	 * class constructor
	 *
	 * @param fileName the fileName parameter defines the name to the file .xls
	 * @param sheetName the sheetName parameter defines the name of the sheet of the .xls
	 * @param URL the URL parameter defines the route where the report will created
	 * @param headers the headers parameter defines the headers to the columns in the .xls
	 *
	 * */
	public XlsxWriter(final String fileName, final String sheetName,
			final String URL, final List<String> headers)
	{
		try
		{
			this.excel = Workbook.createWorkbook(new File(String.format(
					"./reports/%s/generated_files/%s.xls", URL, fileName)));
			this.sheet = this.excel.createSheet(sheetName, 0);
			this.headers = headers;
			this.addHeader();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method add the headers to the columns in the .xls
	 * @return void
	 * */
	private void addHeader()
	{
		try
		{
			this.columnsWidth = new HashMap<Integer, Integer>();

			// Add headers
			for (int h = 0; h < this.headers.size(); h++) {

				final String value = this.headers.get(h);
				double w = 0;

				if (value.length() < 7)
					w = 0.9;
				else
					w = 1.25;

				// Register value size
				final Integer width = (int) Math.floor(value.length()
						* (512 / w) + 100);
				this.columnsWidth.put(h, width);
				font = new WritableFont(WritableFont.createFont("Verdana"), 12);
				format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setBorder(Border.ALL, BorderLineStyle.THIN);
				this.format.setBackground(Colour.GRAY_25);
				this.sheet.addCell(new Label(h, 0, value, format));

				// Set column size
				final CellView column = this.sheet.getColumnView(h);
				column.setSize(columnsWidth.get(h));
				this.sheet.setColumnView(h, column);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method add one sheet to the .xls
	 * @param fileName the fileName parameter defines the name to the file .xls
	 * @param index the index add the number of sheet to the .xls
	 * @param headers the headers parameter defines the headers to the columns in the .xls
	 * @return void
	 * */
	public void addSheet(final String fileName, final int index,
			final List<String> headers)
	{
		this.sheet = this.excel.createSheet(fileName, index);
		this.headers = headers;
		this.addHeader();
	}

	/**
	 * This method calculate the width of the columns
	 * @param position the position parameter is the actual size of the cell in the .xls
	 * @param value the value parametrer is the value that will be put in the cell
	 * @return void
	 * */
	private void calculateWidth(final int position, String value)
	{
		if (value == null)
			value = "";

		double w = 0;

		if (value.length() < 7)
			w = 0.9;
		else
			w = 1.25;

		// Check value size
		final int width = (int) Math.floor(value.toString().length()
				* (512 / w) + 100);

		if (width > this.columnsWidth.get(position))
		{
			// Update column
			this.columnsWidth.replace(position, width);
			final CellView column = sheet.getColumnView(position);
			column.setSize(width);
			sheet.setColumnView(position, column);
		}
	}

	/**
	 * This method close the writting to the .xls
	 * @return void
	 * */
	public void closeBook()
	{
		try
		{
			this.excel.write();
			this.excel.close();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method becomes String format cell
	 * @param position the position parameter defines the position that will be to put the value of the cell
	 * @param row the row parameter defines the number of the row to put the value
	 * @param value the value parameter defines the value to put in the cell
	 * @return void
	 * */
	public void setString(final int position, final int row, final String value)
	{
		try
		{
			this.calculateWidth(position, value);
			this.font = new WritableFont(WritableFont.createFont("Verdana"), 12);
			this.format = new WritableCellFormat(font);
			this.format.setBorder(Border.ALL, BorderLineStyle.THIN);

			if(this.isNumber(value))
			{
				if(this.isLong(value))
				{
					if(value.length() > 2)
					{
						this.format.setAlignment(Alignment.LEFT);
						this.sheet.addCell(new Number(position, row, Long.parseLong(value), format));
					}
					else
					{
						this.format.setAlignment(Alignment.RIGHT);
						this.sheet.addCell(new Number(position, row, Integer.parseInt(value), format));
					}
				}
				else
				{
					format.setAlignment(Alignment.RIGHT);
					this.sheet.addCell(new Number(position, row, Double.parseDouble(value), format));
				}
			}
			else
			{
				this.format.setAlignment(Alignment.LEFT);
				this.sheet.addCell(new Label(position, row, value, this.format));
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method validate if a string is a number
	 * @param string the string parameter define a string to validate
	 * @return
	 */
	private boolean isNumber(String string)
	{
		int i = 0;
		int flag = 0;

		if (string == null || string.isEmpty())
	        return false;

	    if (string.charAt(0) == '-')
	    {
	        if (string.length() > 1)
	            i++;
	        else
	            return false;
	    }

	    String aux = string;

	    while (aux.indexOf(".") > -1)
	    {
	        aux = aux.substring(aux.indexOf(".") + ".".length(), aux.length());
	        flag++;
	    }

	    if(flag > 1)
	    	return false;
	    else
	    	for (; i < string.length(); i++)
	    		if (!Character.isDigit(string.charAt(i)) && string.charAt(i) != '.')
	    			return false;

	    return true;
	}

	/**
	 * This method validate if a string is a long number
	 * @param string the string parameter define a string to validate
	 * @return
	 */
	private boolean isLong(String string)
	{
	    for (int i = 0; i < string.length(); i++)
	    {
	        if (string.charAt(i) == '.')
	            return false;
	    }
		return true;
	}
}