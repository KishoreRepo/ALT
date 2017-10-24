package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.relevantcodes.extentreports.LogStatus;

import api.keyword.controller.ApiKeywordController;
import excel.runner.Driver;
import testcase.RequestBase;
import testcase.RequestData;
import testcase.TestCase;
import testcase.TestSuite;
import ui.webdriver.Browser;
import utility.ExcelReader;

public class ExcelRunner {

	public void executeAllScript(List<TestSuite> testCaseList) throws IOException 
	{
		ExcelReader excelTestScriptReader = new ExcelReader();
		System.out.println("========================================================================================");
		System.out.println("                                 EXECUTION STARTED");
		System.out.println("========================================================================================");
		System.out.println("Total number of scripts to execute : "  + testCaseList.size());
		for(TestSuite testCase : testCaseList)
		{
			if(testCase.getTestDataDescription()!=null)
				Driver.LOGGER = Driver.EXTENT.startTest(testCase.getScriptName()+"-"+testCase.getTestDataDescription());
			else
				Driver.LOGGER = Driver.EXTENT.startTest(testCase.getScriptName());
				
			Driver.LOGGER.log(LogStatus.INFO, testCase.getScriptName());
			List<TestCase> testSteps = excelTestScriptReader.readTestCase(testCase.getScriptName());
			RequestBase.responseParameters = new HashMap<String,String>();
			RequestBase requestBase = new RequestBase();
			List<RequestData> requestDataList = new ArrayList<RequestData>();
			
			for( TestCase testStep : testSteps)
			{
				try
				{
					executeTestStep(testCase,testStep,requestBase, requestDataList);
					
					String testData = "{ ";
					
					if(testCase.getTestData().get(testStep.getParam1())!=null) 
							testData = testData+testStep.getParam1()+"::" + testCase.getTestData().get(testStep.getParam1());
					if(testCase.getTestData().get(testStep.getParam2())!=null) 
						testData = testData+testStep.getParam2()+"::" + testCase.getTestData().get(testStep.getParam2());
					if(testCase.getTestData().get(testStep.getParam3())!=null) 
						testData = testData+testStep.getParam3()+"::" + testCase.getTestData().get(testStep.getParam3());
					
					testData = testData+"}";
					
					if(testStep.getTestStepScreenShotRequired().equalsIgnoreCase("y"))
					{
						if(!testData.equals("{ }")) 
							Driver.LOGGER.log(LogStatus.PASS, testStep.getTestStepDescription(), testStep.getTestStepDescription() 
																								+ " by using [keyword -" +testStep.getKeyword()  
																								+ "] & with [Data - " + testData + "], Screenshot below: " 
																								+ Driver.LOGGER.addScreenCapture(Browser.takeScreenshot(testCase.getScriptName()+testStep.getTestStepNo()+testCase.getRowNumber())) );
						else
							Driver.LOGGER.log(LogStatus.PASS, testStep.getTestStepDescription(), testStep.getTestStepDescription()
									
																								+ " by using [keyword -" +testStep.getKeyword()+ "], Screenshot below: "
																								+ Driver.LOGGER.addScreenCapture(Browser.takeScreenshot(testCase.getScriptName()+testStep.getTestStepNo()+testCase.getRowNumber())));
					}
					else
					{
						if(!testData.equals("{ }")) 
							Driver.LOGGER.log(LogStatus.PASS, testStep.getTestStepDescription(), testStep.getTestStepDescription() 
																								+ " by using [keyword -" +testStep.getKeyword()  
																								+ "] & with [Data - " + testData + "]");
						else
							Driver.LOGGER.log(LogStatus.PASS, testStep.getTestStepDescription(), testStep.getTestStepDescription()
									
																								+ " by using [keyword -" +testStep.getKeyword()+ "]");
					}

				}
				catch(Exception e)
				{
					Driver.LOGGER.log(LogStatus.FAIL, testStep.getTestStepDescription(), testStep.getTestStepDescription() + "-" +e.getMessage());
					System.out.println("Error occurred: " + e.getMessage());
					//e.printStackTrace();
					if(!testStep.getKeyword().toLowerCase().contains("verify"))
						break;
				}
			}
			Driver.EXTENT.endTest(Driver.LOGGER);
		}
	}
	
	
	public void executeTestStep(TestSuite testCase, TestCase testStep, RequestBase requestBase, List<RequestData> requestDataList) throws ClientProtocolException, IOException, JSONException, InvalidAttributesException, NumberFormatException, InterruptedException
	{
		ApiKeywordController keywordController = new ApiKeywordController();
		boolean isApiKeyword = true;
		
		/*try
		{
			KeywordEnum.getKeywordValue(testStep.getKeyword());	
		}
		catch(Exception e)
		{
			isApiKeyword = false;
		}
		*/
		if(isApiKeyword)
			keywordController.executeKeyword(testCase, testStep, requestBase, requestDataList);
		//else
			
			
	}


}
