package com.ggk.applicationLibrary;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.ggk.publicLibrary.PublicClass;

public class HrmApplicationLibrary extends PublicClass{

	String employeeID;
	public int rowNumber;	

	public void openURL()
	{
		driver.get("https://opensource-demo.orangehrmlive.com/");
		loadProperties(System.getProperty("user.dir")+"\\src\\main\\resources\\orangehrmOR.properties");
		loadExcel(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\TestData.xls","Sheet1");
		outputFileLocation = System.getProperty("user.dir")+"\\src\\test\\resources\\data\\TestsResults.xls";
		outputSheetName = "Sheet1";
		output.loadExcel(outputFileLocation, outputSheetName);
	}
	
	public void login(long time)
	{
		String message = "";
		rowNumber = getRowNumber(className,"TestClassName");
		setText(getObject("loginUserName"),time, readData(rowNumber,getColumnNumber("LoginUserName")));
		setText(getObject("loginPassword"), time, readData(rowNumber,getColumnNumber("LoginPassword")));
		clickElement(getObject("loginButton"),time);
		message = getPageError(properties.getProperty("dashboardURL"),time);
		if(isError(message))
		{
			message = getErrorMessage(getObject("loginError"), time);
			if(!message.equals("can continue"))
			{
				log.info(message);
			}		
		}
	}
	
	public void addEmployee(long time)
	{
		String message;
//		JavascriptExecutor js = (JavascriptExecutor)driver;
//		js.executeScript("arguments[0].scrollIntoView", driver.findElement(getObject("PIM")));
//		moveCursorToElement(getObject("PIM"),time);
		clickElement(getObject("PIM"),time);//added for checking
//		moveCursorToElement(getObject("addEmployee"), time);
		clickElement(getObject("addEmployee"), time);
		setText(getObject("firstName"), time, readData(rowNumber,getColumnNumber("firstname")));
		setText(getObject("middleName"), time, readData(rowNumber,getColumnNumber("middlename")));
		setText(getObject("lastName"), time, readData(rowNumber,getColumnNumber("lastname")));
		employeeID = getAttributeValue(getObject("employeeID"),time,"value");
		//setText(getObject("photo"), time,System.getProperty("user.dir")+"\\src\\test\\resources\\data\\"+readData(getRowNumber(className,"TestClassName"),getColumnNumber("photo")));
		clickElement(getObject("loginCheck"), time);
		setText(getObject("userName"), time, readData(rowNumber,getColumnNumber("username")));
		setText(getObject("password"), time,readData(rowNumber,getColumnNumber("password")));
		setText(getObject("rePassword"), time, readData(rowNumber,getColumnNumber("password")));
		clickElement(getObject("save"), time);
		message = getPageError(properties.getProperty("personalDetailsURL")+employeeID,time);
		if(isError(message))
		{
			message = getErrorMessage(getObject("userNameError"), time);
			if(isError(message))
			{
				log.error(message);
			}
			
			message = getErrorMessage(getObject("passwordError"), time);
			if(isError(message))
			{
				log.error(message);
			}

			message = getErrorMessage(getObject("addEmpError"), time);
			if(isError(message))
			{
				log.error(message);
			}
			
			message = getErrorMessage(getObject("rePasswordError"), time);
			if(isError(message))
			{
				log.error(message);
			}
		}
		
	}
	
	private boolean isError(String message)
	{
		if(!message.equals("can continue"))
		{
			log.info(message);
			return true;
		}
		return false;
	}
	
	public void verify(long time)
	{
		String firstName = getAttributeValue(getObject("empFirstName"), time, "value");
		String middleName = getAttributeValue(getObject("empMiddleName"), time, "value");
		String lastName = getAttributeValue(getObject("empLastName"), time, "value");
		String ID = getAttributeValue(getObject("empID"), time, "value");
		Assert.assertEquals(readData(rowNumber,getColumnNumber("firstname")), firstName);
		Assert.assertEquals(readData(rowNumber,getColumnNumber("middlename")), middleName);
		Assert.assertEquals(readData(rowNumber,getColumnNumber("lastname")), lastName);
		Assert.assertEquals(employeeID, ID);
	}
	
	public void addMoreDetails(long time)
	{
		Select dropDown;
		clickElement(getObject("save"), time);
		int rowNum = getRowNumber(getObject("empGender"), time, readData(rowNumber,getColumnNumber("gender")));
		clickElement(By.cssSelector("li.radio ul li:nth-child("+rowNum+") input"), time);
		dropDown = selectDropdown(getObject("empNationality"),time);
		dropDown.selectByVisibleText(readData(rowNumber,getColumnNumber("nationality")));
		dropDown = selectDropdown(getObject("empMaritalStatus"), time);
		dropDown.selectByVisibleText(readData(rowNumber,getColumnNumber("maritalstatus")));
		clearElement(getObject("empDOB"),time);
		setText(getObject("empDOB"),time,"1");
		String dob = dateOfBirth('-',time);
		clearElement(getObject("empDOB"),time);
		setText(getObject("empDOB"), time, dob);
		//String dob = readData(rowNumber,getColumnNumber("dateofbirth"));
		//dob.replaceAll("\"", "");
		//sendKeys(getObject("empDOB"), time, dob);
		clickElement(getObject("save"), time);
		String message = getText(getObject("saveStatus"),time);
		log.info(message);
	}
	
	public String dateOfBirth(char ch,long time)
	{
		String month = readData(rowNumber,getColumnNumber("month"));
		String year = readData(rowNumber,getColumnNumber("year"));
		String date = readData(rowNumber,getColumnNumber("date"));
		int position = date.indexOf(".");
		date = date.substring(0, position);
		position = month.indexOf(".");
		month = month.substring(0, position);
		position = year.indexOf(".");
		year = year.substring(0, position);
		if(month.length()==1)
			month = "0" + month;
		if(date.length()==1)
			date = "0"+ date;
		StringBuilder dob = new StringBuilder(10);
		if(getErrorMessage(getObject("dobError"), time).contains("yyyy-"))
		{
			dob.append(year).append(ch).append(month).append(ch).append(date);
		}
		else
		{
			dob.append(date).append(ch).append(month).append(ch).append(year);
		}
		return dob.toString();
	}
	
	public int searchEmployee(long time)
	{
		moveCursorToElement(getObject("PIM"), time);
		moveCursorToElement(getObject("viewEmployee"), time);
		clickElement(getObject("viewEmployee"), time);
		clickElement(getObject("searchButton"), time);
		return getRowNumber(getObject("empRowNumber"),time,employeeID);	
	}
	
	public void verifyAllDetails(int rowNumber,long time)
	{
		clickElement(By.cssSelector("tbody tr:nth-child("+rowNumber+") td:nth-child(3) a "), time);
		SoftAssert softAssertion = new SoftAssert();
		verify(time);
		clickElement(getObject("save"),time);
		Select dropdown = selectDropdown(getObject("empNationality"), time);
		WebElement element = dropdown.getFirstSelectedOption();
		softAssertion.assertEquals(element.getText(),readData(rowNumber,getColumnNumber("nationality")));
		dropdown = selectDropdown(getObject("empMaritalStatus"), time);
		element  = dropdown.getFirstSelectedOption();
		softAssertion.assertEquals(element.getText(),readData(rowNumber,getColumnNumber("maritalstatus")));
		//String dob = getAttributeValue(getObject("empDOB"), time, "value");
		//softAssertion.assertEquals(dob,dateOfBirth('-',time));
		int rowNum = getRowNumber(getObject("empGender"), time, readData(rowNumber,getColumnNumber("gender")));
		String value = getAttributeValue(By.cssSelector("li.radio ul li:nth-child("+rowNum+") input"), time, "checked");
		softAssertion.assertEquals(value, "true");
		softAssertion.assertAll();
	}
	
	public void deleteEmployee(long time)
	{
		int rowNumber = searchEmployee(time);
		clickElement(By.cssSelector("tbody tr:nth-child("+rowNumber+") td  input"),time);
		moveCursorToElement(getObject("delete"),time);
		clickElement(getObject("delete"),time);
		clickElement(getObject("deleteConfirm"),time);
		String message = getText(getObject("deleteStatus"),time);
		log.info(message);
	}
	
	public void logout(long time)
	{
		moveCursorToElement(getObject("logoutPanel"), time);
		clickElement(getObject("logoutPanel"), time);
		moveCursorToElement(getObject("logoutButton"), time);
		clickElement(getObject("logoutButton"), time);
	}
	
	public void userOperations(long time)
	{
		clickElement(getObject("userDirectory"),time);
		clickElement(getObject("searchButton"),time);
	}
}
