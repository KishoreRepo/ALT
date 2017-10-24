package excel.runner; 

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import constants.FrameworkConstants;
import runner.ExcelRunner;
import testcase.TestSuite;
import utility.ExcelReader;
import utility.PropertyFileReader;

public class Driver 
{
	public static ExtentReports EXTENT;
	public static ExtentTest LOGGER;
	public static String PROPERTY_FILE_LOCATION = "";
	
	@Test
	public void startTest()
	{		
		//System.out.println("Curr dir - " + System.getProperty("temp.dir"));
		
		ExcelRunner apiExecutor = new ExcelRunner();
		ExcelReader excelReader = new ExcelReader();
	
		try 
		{
			PropertyFileReader propertyFileReader = new PropertyFileReader(PROPERTY_FILE_LOCATION);
			excelReader.readObjectRepo();
			List<TestSuite> testSuite = excelReader.getTestScriptsToExecute(Boolean.parseBoolean(propertyFileReader.getValue("TEST_DATA_SAPARATE_FILE")));
			//List<TestSuite> testCaseList = apiExecutor.getTestScriptsToExecute();
			apiExecutor.executeAllScript(testSuite);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@BeforeClass
	public static void startReport()
	{
		PropertyFileReader propertyFileReader = new PropertyFileReader(PROPERTY_FILE_LOCATION);
		EXTENT = new ExtentReports(propertyFileReader.getValue("report.dir") +"/test-output/Report.html", true,NetworkMode.OFFLINE);
		EXTENT
			.addSystemInfo("Project name", "API Working demo")
			.addSystemInfo("Environment", "Local Demo")
			.addSystemInfo("User", "Demo User");
		EXTENT.loadConfig(new File(propertyFileReader.getValue("report.dir")+"/test-output/extent-config.xml"));
	}
	

	@AfterClass
	public static void endReport()
	{
		EXTENT.flush();
		EXTENT.close();
		
		if(FrameworkConstants.SELENIUM_DRIVER!=null)
			FrameworkConstants.SELENIUM_DRIVER.quit();
	}
}
