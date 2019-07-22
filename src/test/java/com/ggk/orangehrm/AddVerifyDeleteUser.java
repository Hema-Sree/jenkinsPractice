package com.ggk.orangehrm;

import org.testng.annotations.Test;

import com.ggk.applicationLibrary.HrmApplicationLibrary;

public class AddVerifyDeleteUser extends HrmApplicationLibrary{

	@Test
	void addVerifyDelete()
	{
		className = this.getClass().getSimpleName();
		long time=10;
		openBrowser("ie");
		openURL();
		login(time);
		addEmployee(time);
		verify(time);
		addMoreDetails(time);
		deleteEmployee(time);
		int rowNumber = searchEmployee(time);
		if(rowNumber == -1)
		{
			log.info("Employee is deleted from employee list");
		}
		else
		{
			log.error("Employee is not deleted from employee list");
		}
		logout(time);
		log.info("logged out of application");
		quitBrowser();
		String message = output.readData(output.getRowNumber(className,"TestCaseName"),output.getColumnNumber("status"));
		if(message.equals("NIL"))
		{
			output.writeData(output.getRowNumber(className,"TestCaseName"),output.getColumnNumber("status"),"PASSED");
		}
	}
	
}
