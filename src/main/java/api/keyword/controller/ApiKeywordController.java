package api.keyword.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.InvalidAttributesException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.openqa.selenium.By;

import api.keyword.KeywordEnum;
import api.keyword.service.ApiKeywordService;
import api.keyword.service.ApiNonKeywordService;
import constants.FrameworkConstants;
import testcase.RequestBase;
import testcase.RequestData;
import testcase.TestCase;
import testcase.TestSuite;
import ui.webdriver.Browser;
import ui.webdriver.ObjectLocator;
import ui.webdriver.action.CheckBox;
import ui.webdriver.action.Click;
import ui.webdriver.action.ComboBox;
import ui.webdriver.action.InputLabel;
import utility.StringUtilities;

public class ApiKeywordController 
{
	
	public static final String BASIC_AUTHENTICATION_HEADER="basic";
	static final String HEADER="header";
	
	public void executeKeyword(TestSuite testCase, TestCase testStep, RequestBase requestBase, List<RequestData> requestDataList) throws ClientProtocolException, IOException, JSONException, InvalidAttributesException, NumberFormatException, InterruptedException
	{
		KeywordEnum keyword = KeywordEnum.getKeywordValue(testStep.getKeyword());
		String[] objectKeyValue;
		
		switch(keyword)
		{
			case BASEURL:
				ApiKeywordService.setUrl(requestBase, testCase.getTestData().get(testStep.getParam1()));
				break;
			case SERVICE:
				ApiKeywordService.setServiceUrl(requestBase, testCase.getTestData().get(testStep.getParam1()));
				break;
			case REQUESTMETHOD:
				ApiKeywordService.setMethod(requestBase, testStep.getParam1(), testCase.getTestData().get(testStep.getParam1()));
				break;
			case REQUESTHEADER:
				if(testCase.getTestData().get(testStep.getParam1()).equalsIgnoreCase(BASIC_AUTHENTICATION_HEADER))
				{
					requestBase.setAuthenticationType(BASIC_AUTHENTICATION_HEADER);
					requestBase.setAuthenicationUser(testCase.getTestData().get(testStep.getParam2()));
					requestBase.setAuthenicationPassword(testCase.getTestData().get(testStep.getParam3()));
					ApiKeywordService.setHeader(requestDataList, "username;;password", testCase.getTestData().get(testStep.getParam2())+";;"+testCase.getTestData().get(testStep.getParam3()),BASIC_AUTHENTICATION_HEADER);
				}
				else
					ApiKeywordService.setHeader(requestDataList, testStep.getParam1(), testCase.getTestData().get(testStep.getParam1()),HEADER);
				break;
			case REQUESTPARAMETER:
				ApiKeywordService.setParameter(requestDataList, testStep.getParam1(), testCase.getTestData().get(testStep.getParam1()));
				break;
			case REQUESTSUBMISSION:
				ApiKeywordService.requestSubmission(requestBase, requestDataList);
				break;
			case VERIFYSTATUS:
				ApiKeywordService.verifyResponseStatus(requestBase,testCase.getTestData().get(testStep.getParam1()));
				break;
			case VERIFYPARAMETERCOUNT:
				ApiKeywordService.verifyParamterCount(requestBase,testCase.getTestData().get(testStep.getParam1()));
				break;
			case VERIFYDATATYPE:
				ApiKeywordService.verifyDataType(testCase.getTestData().get(testStep.getParam1()));
				break;
			case VERIFYTEXT:
				if(testStep.getControlName()==null || testStep.getControlName().trim().equals(""))
				{
					if(testStep.getParam2()==null || testStep.getParam2().trim().equals(""))
					{
						ApiKeywordService.verifyText(RequestBase.responseParameters.get(testStep.getParam1()), testCase.getTestData().get(testStep.getParam1()));
					}
					else
						ApiKeywordService.verifyText(RequestBase.responseParameters.get(testStep.getParam2()), testCase.getTestData().get(testStep.getParam1()));
				}
				else
				{
					objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
					
					if(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]).getText()==null || 
							ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]).getText().equals(""))
					{
						ApiKeywordService.verifyText(FrameworkConstants.SELENIUM_DRIVER.findElement(By.name("member(email)")).getAttribute("value").trim(), 
								testCase.getTestData().get(testStep.getParam1()));
					}
					else
						ApiKeywordService.verifyText(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]).getText(), testCase.getTestData().get(testStep.getParam1()));
				}
				break;
			case VERIFYRESPONSETYPE:
				ApiKeywordService.verifyResponseContentType(requestBase,testCase.getTestData().get(testStep.getParam1()));
				break;
			case REQUEST_CONTENT:
				if(testCase.getTestData().get(testStep.getParam1()).contains("{")||	testCase.getTestData().get(testStep.getParam1()).contains("<"))
				{
					ApiKeywordService.setRequestMedia(requestDataList, testStep.getParam1(), testCase.getTestData().get(testStep.getParam1()));
					ApiNonKeywordService.identifyRequestContentType(testCase.getTestData().get(testStep.getParam1()), requestBase);
				}
				else
					ApiKeywordService.setRequestMediaFromFile(requestDataList, testStep.getParam1(), testCase.getTestData().get(testStep.getParam1()),requestBase);
					
				break;
			case OPENAPP:
				if(FrameworkConstants.SELENIUM_DRIVER==null)
				{
					Browser initiateWebDriver = new Browser();
					initiateWebDriver.getWebDriver();
				}
				FrameworkConstants.SELENIUM_DRIVER.get(testCase.getTestData().get(testStep.getParam1()));
				break;
			case INPUTTEXT:
				objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
				
				InputLabel.enterText(
						ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]),
						testCase.getTestData().get(testStep.getParam1()));
				break;
			
			case CLICK:
				objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
				Click.click(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]));
				break;
			case SELECTFROMLIST:
				objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
				
				ComboBox.selectFromDropDown(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]),
						testCase.getTestData().get(testStep.getParam1()),testCase.getTestData().get(testStep.getParam2()));
				break;
			case STORE:
				objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
				
				if(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]).getText()==null || 
						ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]).getText().trim().equals(""))
				{
					testCase.setTestData(testStep.getParam1(), FrameworkConstants.SELENIUM_DRIVER.findElement(By.name("member(email)")).getAttribute("value").trim(),null);
				}
				else
				{
					testCase.setTestData(testStep.getParam1(), ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]).getText().trim(),null);
				}
				break;
			case ALERTOK:
				FrameworkConstants.SELENIUM_DRIVER.switchTo().alert().accept();
				break;
			case SELECTCHECKBOX:
				objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
				CheckBox.selectDeSelectCheckBox(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]), true);
				break;
			case WAIT:
				TimeUnit.SECONDS.sleep(Integer.parseInt(testCase.getTestData().get(testStep.getParam1()))); 
				break;
			case IFCLICK:
				objectKeyValue = StringUtilities.splitObjectRepoValue(FrameworkConstants.ObjectRepository.get(testStep.getControlName()));
				try
				{
					Click.click(ObjectLocator.getWebElement(objectKeyValue[0],objectKeyValue[1]));
				}
				catch(Exception E)
				{
					break;
				}
				break;
			default:
				break;
				
		}
		
	}
	
}
