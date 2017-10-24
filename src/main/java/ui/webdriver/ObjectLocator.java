package ui.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import constants.FrameworkConstants;
import excel.runner.Driver;
import utility.PropertyFileReader;

public class ObjectLocator 
{

	public static WebElement getWebElement(String ObjectPath,String objectLocator)
	{
		PropertyFileReader propertyFileReader = new PropertyFileReader(Driver.PROPERTY_FILE_LOCATION);
		
		int waitTime = Integer.parseInt(propertyFileReader.getValue("wait.time"));
		
		FrameworkConstants.SELENIUM_DRIVER.manage().timeouts().pageLoadTimeout(waitTime, TimeUnit.SECONDS);
		
		switch(objectLocator.toUpperCase())
		{
			case "ID":
				return (new WebDriverWait(FrameworkConstants.SELENIUM_DRIVER, waitTime)).until(ExpectedConditions.presenceOfElementLocated(By.id(ObjectPath)));
						//FrameworkConstants.SELENIUM_DRIVER.findElement(By.id(ObjectPath));
			case "NAME":
				return (new WebDriverWait(FrameworkConstants.SELENIUM_DRIVER, waitTime)).until(ExpectedConditions.presenceOfElementLocated(By.name(ObjectPath)));
						//FrameworkConstants.SELENIUM_DRIVER.findElement(By.name(ObjectPath));
			case "XPATH":
				return (new WebDriverWait(FrameworkConstants.SELENIUM_DRIVER, waitTime)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjectPath)));
						//FrameworkConstants.SELENIUM_DRIVER.findElement(By.xpath(ObjectPath));
			case "LINKTEXT":
				return (new WebDriverWait(FrameworkConstants.SELENIUM_DRIVER, waitTime)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(ObjectPath)));
						//FrameworkConstants.SELENIUM_DRIVER.findElement(By.linkText(ObjectPath));
			default:
				return null;
		}
	}
	
	
}
