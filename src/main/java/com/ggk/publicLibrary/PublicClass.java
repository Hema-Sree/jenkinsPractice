package com.ggk.publicLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ggk.utils.ExcelOperations;

public class PublicClass extends ExcelOperations{
		
	public WebDriver driver;
	WebDriverWait wait;
	Actions action;
	public Logger log = Logger.getLogger(PublicClass.class);
	public Properties properties = new Properties();
	public String outputFileLocation;
	public String outputSheetName;
	public String className;
	public ExcelOperations output = new ExcelOperations();
	
	public void loadProperties(String fileLocation)
	{
		 				
		    FileInputStream fileObject;
			try 
			{
				fileObject = new FileInputStream(fileLocation);
				properties.load(fileObject);
			} 
			catch (FileNotFoundException e)
			{
				log.error(e);
			} 
			catch (IOException e) 
			{
				log.error(e);
			}									
	}
	
	public void openBrowser(String browser)
	{
		if(browser.equals("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\drivers\\geckodriver.exe"); 
			File pathBinary = new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
			FirefoxBinary firefoxBinary = new FirefoxBinary(pathBinary);   
			DesiredCapabilities desired = DesiredCapabilities.firefox();
			FirefoxOptions options = new FirefoxOptions();
			desired.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options.setBinary(firefoxBinary));
			driver = new FirefoxDriver(options);
		}
		else if(browser.equals("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\drivers\\chromedriver.exe");
			driver= new ChromeDriver();	
		}
		else if(browser.equals("ie"))
		{
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\drivers\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
		//driver.manage().window().maximize();
		
	}
	
	public void openBrowserStack(String browser)
	{
		String USERNAME = "paryanshu2";
		String ACCESSKEY = "hExtsDCzDzkKnYx6H57i";
		String URL = "https://"+USERNAME+":"+ACCESSKEY+"@hub.browserstack.com/wd/hub";
		URL browserStackURL = null;
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("os", "Windows");
		caps.setCapability("os_version", "10");
		caps.setCapability("resolution", "1920x1080");
		caps.setCapability("browserstack.local", "false");
		caps.setCapability("browserstack.selenium_version", "3.10.0");
		if(browser.equals("chrome"))
		{
			caps.setCapability("browser", "Chrome");
			caps.setCapability("browser_version", "76.0 beta");
		}
		else if(browser.equals("firefox"))
		{
			caps.setCapability("browser", "Firefox");
			caps.setCapability("browser_version", "68.0 beta");
		}
		else if(browser.equals("ie"))
		{
			caps.setCapability("browser", "IE");
			caps.setCapability("browser_version", "11.0");
		}
		try 
		{
			 browserStackURL = new URL(URL);
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		driver = new RemoteWebDriver(browserStackURL, caps);
	}
	
	public void closeBrowser()
	{
		driver.close();
		log.info("closed browser");
	}
	
	public void quitBrowser()
	{
		driver.quit();
	}
	
	public By getObject(String objectKey)
	{
		objectKey = properties.getProperty(objectKey);
		By object= null;
		String[] keyValue = objectKey.split(":",2);
		switch(keyValue[0])
		{
		case "id":
			object = By.id(keyValue[1]);
			break;
		case "xpath":
			object = By.xpath(keyValue[1]);
			break;
		case "cssSelector":
			object = By.cssSelector(keyValue[1]);
			break;
		case "name":
			object = By.name(keyValue[1]);
			break;
		case "className":
			object = By.className(keyValue[1]);
			break;
		case "linkText":
			object = By.linkText(keyValue[1]);
			break;
		case "partialLinkText":
			object = By.partialLinkText(keyValue[1]);
			break;
		case "tagName":
			object = By.tagName(keyValue[1]);
			break;
		}
		return object;
		
	}

    public void setText(By elementLocator, long timeOutInSeconds,String charSequence)
	{
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
			if(element.isDisplayed() && element.isEnabled())
			{
				element.sendKeys(charSequence);		
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
	}
	
	public void clickElement(By elementLocator, long timeOutInSeconds)
	{
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
			if(element.isDisplayed() && element.isEnabled())
			{
				element.click();
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
	}
	
	public String getErrorMessage(By elementLocator,long timeOutInSeconds)
	{
		String message = "can continue";
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
			if(element.isDisplayed())
			{
				message = element.getText();
			}
		}
		catch(Exception e)
		{
			
		}
		return message;
	}
	
	public void moveCursorToElement(By elementLocator,long timeOutInSeconds)
	{
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].scrollIntoView",element);
			if(element.isDisplayed())
			{
				
				action = new Actions(driver);
				action.moveToElement(element).build().perform();
			}
		}
		catch(Exception e)
		{
			
			log.error(e);
			testReporter("FAILED");
		}
	}
	
	public String getAttributeValue(By elementLocator,long timeOutInSeconds, String attributeName)
	{
		String value="";
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
			if(element.isDisplayed())
			{
				value = element.getAttribute(attributeName);
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);	
		}
		return value;
	}
	
	public Select selectDropdown(By elementLocator,long timeOutInSeconds)
	{
		Select dropDown=null;
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
			if(element.isDisplayed() && element.isEnabled())
			{
				dropDown = new Select(element);
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
		return dropDown;
	}
	
	public void clearElement(By elementLocator,long timeOutInSeconds)
	{
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
			if(element.isDisplayed() && element.isEnabled())
			{
				element.clear();
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
	}
	
	public String getText(By elementLocator, long timeOutInSeconds)
	{
		String message="";
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
			if(element.isDisplayed())
			{
				message = element.getText();
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
		return message;
	}
	
	public int getRowNumber(By elementLocator, long timeOutInSeconds,String text)
	{
		int rowNumber = -1;
		try
		{
			WebElement element;
			wait = new WebDriverWait(driver,timeOutInSeconds);
			List<WebElement> list = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementLocator));
			for(int i=0;i<list.size();i++)
			{
				element = list.get(i);
				if(text.equalsIgnoreCase(element.getText()))
				{
					rowNumber = i+1;
				}
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
		return rowNumber;
	}
	
	public void switchToAlertBox(long timeOutInSeconds, String message)
	{
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			log.info("confirmation alert: "+alert.getText());
			if(message.equals("ok"))
			{
				alert.accept();
			}
			else
			{
				alert.dismiss();
			}
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
	}
	
	public String getPageError(String pageURL,long timeOutInSeconds)
	{
		String message = "";
		try
		{
			wait = new WebDriverWait(driver,timeOutInSeconds);
			wait.until(ExpectedConditions.urlToBe(pageURL));
			message = "can continue";
			
		}
		catch(Exception e)
		{
			testReporter("FAILED");
			log.error(e);
		}
		return message;
	}
	
	public void testReporter(String status)
	{
		output.writeData(output.getRowNumber(className,"TestCaseName"),output.getColumnNumber("status"),status);
	}
}
