package testcase;

import java.util.HashMap;

public class TestSuite 
{
	private String rowNumber;
	private String scriptName;
	private String isScriptToBeExecuted;
	private String testDataSNo;
	private String testDataDescription;
	private HashMap<String, String> testData;
	
	
	public String getTestDataSNo() {
		return testDataSNo;
	}
	public void setTestDataSNo(String testDataSNo) {
		this.testDataSNo = testDataSNo;
	}
	public String getTestDataDescription() {
		return testDataDescription;
	}
	public void setTestDataDescription(String testDataDescription) {
		this.testDataDescription = testDataDescription;
	}

	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getIsScriptToBeExecuted() {
		return isScriptToBeExecuted;
	}
	public void setIsScriptToBeExecuted(String isScriptToBeExecuted) {
		this.isScriptToBeExecuted = isScriptToBeExecuted;
	}
	public HashMap<String, String> getTestData() {
		return testData;
	}
	public void setTestData(String key, String value, HashMap<String, String> hashData) {
		if(hashData==null)
			this.testData.put(key, value);
		else
			this.testData = hashData;
	}

}
