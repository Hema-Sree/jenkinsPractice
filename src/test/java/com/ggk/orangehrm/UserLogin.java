package com.ggk.orangehrm;

import org.testng.annotations.Test;

import com.ggk.applicationLibrary.HrmApplicationLibrary;

public class UserLogin extends HrmApplicationLibrary{

	@Test
	public void userLogin()
	{
		className = this.getClass().getSimpleName();
		long time=10;
		openBrowser("chrome");
		openURL();
		login(time);
		userOperations(time);
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
