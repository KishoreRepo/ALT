package ui.webdriver.action;

import javax.naming.directory.InvalidAttributesException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import constants.FrameworkConstants;

public class InputLabel 
{
	
	public static void enterText(WebElement element, String inputText)
	{
		element.clear();
		element.sendKeys(inputText);
	}
	
	public static void verifyText(WebElement element, String inputData) throws InvalidAttributesException
	{
		if(!element.getText().equals(inputData))
			throw new InvalidAttributesException("value mismatch, Expected: " + inputData + " and Actual: " + element.getText());
	}
}
