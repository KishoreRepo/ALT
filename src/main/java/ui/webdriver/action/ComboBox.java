package ui.webdriver.action;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ComboBox 
{

	public static void selectFromDropDown(WebElement webElement, String inputValue, String dropDownValueIdentifier)
	{
		
		Select select = new Select(webElement);
		
		if(dropDownValueIdentifier.equalsIgnoreCase("value"))
		{
			select.selectByValue(inputValue);
		}
		else if(dropDownValueIdentifier.equalsIgnoreCase("text"))
		{
			select.selectByVisibleText(inputValue.trim());
		}
		else if (dropDownValueIdentifier.equalsIgnoreCase("index"))
		{
			List<WebElement> oPtionsList = new ArrayList<WebElement>();
			oPtionsList = select.getOptions();
			for (int i = 0; i < oPtionsList.size(); i++) 
			{
			  if (oPtionsList.get(i).getText().trim().replaceAll("\\s+", " ").equalsIgnoreCase(inputValue.trim().replaceAll("\\s+", " ")))
				select.selectByIndex(i);
			}
		}
	}
}