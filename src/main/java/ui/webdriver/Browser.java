package ui.webdriver;

import java.io.File;
import java.io.IOException;

import org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;

import constants.FrameworkConstants;


public class Browser 
{

	public void getWebDriver()
	{
		System.setProperty("webdriver.chrome.driver","C:/Kishore/build/api/library/chromedriver.exe");
		
		if(FrameworkConstants.SELENIUM_DRIVER==null)
				FrameworkConstants.SELENIUM_DRIVER  = new ChromeDriver();
		
		FrameworkConstants.SELENIUM_DRIVER.manage().window().maximize();
		
	}
	
	public static String takeScreenshot(String screenShotName) throws IOException
	{
        TakesScreenshot ts = (TakesScreenshot)FrameworkConstants.SELENIUM_DRIVER;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = System.getProperty("user.dir") +"\\screenshots\\"+screenShotName+".png";
        File destination = new File(dest);
        FileUtils.copyFile(source, destination);      
                     
        return dest;
	}
	
}
