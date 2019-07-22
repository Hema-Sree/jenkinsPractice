package com.ggk.orangehrm;

import org.testng.annotations.Test;

import com.ggk.applicationLibrary.HrmApplicationLibrary;

public class AddVerifyUser extends HrmApplicationLibrary{

	@Test
	public void addVerify()
	{
		className = this.getClass().getSimpleName();
		long time=10;
		openBrowser("chrome");
		openURL();
		login(time);
		addEmployee(time);
		verify(time);
		addMoreDetails(time);
		int rowNumber = searchEmployee(time);
		if(rowNumber != -1)
		{
			log.info("Searched the added employee in the list");
			verifyAllDetails(rowNumber,time);
			log.info("verified all the details of employee");
		}
		else
		{
			log.error("Employee is not added into employee list");
		}
		logout(time);
		log.info("logged out of application");
		closeBrowser();
		String message = output.readData(output.getRowNumber(className,"TestCaseName"),output.getColumnNumber("status"));
		if(message.equals("NIL"))
		{
			output.writeData(output.getRowNumber(className,"TestCaseName"),output.getColumnNumber("status"),"PASSED");
		}
	}
	
}
