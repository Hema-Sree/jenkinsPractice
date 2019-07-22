package com.ggk.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.testng.log4testng.Logger;

public class ExcelOperations {

	 public String fileLocation,sheetName;
	 public FileInputStream fis;
	 public HSSFWorkbook workbook;
	 public HSSFSheet sheet;
	 public Row row;
	 public Cell cell;
	 public Logger logger = Logger.getLogger(ExcelOperations.class);

	    public void loadExcel(String fileLocation,String sheetName)
	    {
	    	this.fileLocation = fileLocation;
	    	this.sheetName = sheetName;
		    try 
		    {
			   fis = new FileInputStream(fileLocation);
			   workbook = new HSSFWorkbook(fis);
			   sheet = workbook.getSheet(sheetName);
		    } 
		    catch (FileNotFoundException e) 
		    {
			   logger.error(e);
		    }
		    catch (IOException e) 
		    {
			   logger.error(e);
		    }
	    }
	 
	 	public String readData(int rowNumber,int columnNumber)
		{
			String data= null;
			Cell cell =null;
			row = sheet.getRow(rowNumber);
			cell = row.getCell(columnNumber);
			data = cell.toString();
//			try 
//			{
//				workbook.close();
//			} 
//			catch (IOException e) 
//			{
//				e.printStackTrace();
//			}
			return data;
			
		}
	 	
	 	public int getRowNumber(String rowName,String columnName)
	 	{	
	 		int rowNumber = -1;
	 		int totalRows = sheet.getLastRowNum();
	 		int columnNumber = getColumnNumber(columnName);
	 		for(int i=0;i<=totalRows;i++)
	 		{
	 			if(sheet.getRow(i).getCell(columnNumber).getStringCellValue().replaceAll("\\s", "").equals(rowName.replaceAll("\\s", "")))
	 			{
	 				return i;
	 			}
	 		}
	 		return rowNumber;
	 	}
		
		public int getColumnNumber(String columnName)
		{
			int columnNumber = -1;
			row = sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++)
			{
				if(row.getCell(i)!=null)
				{
					if(row.getCell(i).getStringCellValue().replaceAll("\\s", "").equalsIgnoreCase(columnName.replaceAll("\\s", "")))
					{
						return i;
					}
				}
				else
				{
					continue;
				}
			}
			return columnNumber;
		}
		
		public void writeData(int rowNumber,int columnNumber,String status)
		 {
			 try
			 {
				 fis = new FileInputStream(fileLocation);
				 workbook = new HSSFWorkbook(fis);
				 sheet = workbook.getSheet(sheetName);
				 row = sheet.getRow(rowNumber);
				 cell = row.createCell(columnNumber);
				 cell.setCellValue(status);
				 FileOutputStream fos = new FileOutputStream(fileLocation);
				 workbook.write(fos);
				 workbook.close();
				 fos.close();

			 }
			 catch(FileNotFoundException e)
			 {
				 logger.error(e);
			 }
			 catch(IOException e)
			 {
				 logger.error(e);
			 }
		 }
}
