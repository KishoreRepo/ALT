package ui.webdriver.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import constants.FrameworkConstants;

public class CheckBox 
{
	
	public static void selectDeSelectCheckBox(WebElement webElement, boolean selectCheckBox)
	{
		if (!webElement.isSelected() && selectCheckBox)
		{
		     webElement.click();
		}
	}

}
