package testcase;

public class TestCase 
{
	private String testStepNo;
	private String testStepDescription;
	private String testStepScreenShotRequired;
	private String keyword;
	private String controlName;
	private String param1;
	private String param2;
	private String param3;
	
	public String getTestStepNo() {
		return testStepNo;
	}
	public void setTestStepNo(String testStepNo) {
		this.testStepNo = testStepNo;
	}
	public String getTestStepDescription() {
		return testStepDescription;
	}
	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}
	public String getTestStepScreenShotRequired() {
		return testStepScreenShotRequired;
	}
	public void setTestStepScreenShotRequired(String testStepExpectedResult) {
		this.testStepScreenShotRequired = testStepExpectedResult;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
}
