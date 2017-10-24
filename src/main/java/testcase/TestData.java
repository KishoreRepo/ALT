package testcase;

import java.util.HashMap;

public class TestData 
{
	
	private String sNo;
	private String testStepDescription; 
	private HashMap<String, String> testData;
	
	
	public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	public String getTestStepDescription() {
		return testStepDescription;
	}
	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}
	public HashMap<String, String> getTestData() {
		return testData;
	}
	public void setTestData(HashMap<String, String> testData) {
		this.testData = testData;
	}

}
