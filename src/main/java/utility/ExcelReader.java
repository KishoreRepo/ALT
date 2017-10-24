package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import constants.FrameworkConstants;
import excel.runner.Driver;
import testcase.TestCase;
import testcase.TestData;
import testcase.TestSuite;

public class ExcelReader {

	public List<TestCase> readTestCase(String scriptName) throws IOException {

		PropertyFileReader propertyFileReader = new PropertyFileReader(Driver.PROPERTY_FILE_LOCATION);
		String testCaseFolder = propertyFileReader.getValue("BASE_LOCATION")+propertyFileReader.getValue("TEST_CASE_FOLDER");
		
		String filePath = testCaseFolder+scriptName+".xls";
		
		System.out.println("\n\nTest Script file: " + filePath);
		
		List<TestCase> excelTestScriptVOsList = new ArrayList<TestCase>();

		File fileForScript = null;
		FileInputStream fileInputStreamForScript = null;
		Workbook workbookForScript = null;
		Sheet sheetDetailsForScript = null;

		try {
			fileForScript = new File(filePath);
			fileInputStreamForScript = new FileInputStream(fileForScript);
			
			if(filePath.contains("xlsx")) {
				workbookForScript = new XSSFWorkbook(fileInputStreamForScript);
			}else{
				workbookForScript = new HSSFWorkbook(fileInputStreamForScript);
			}
			sheetDetailsForScript = workbookForScript.getSheet("Test steps");

			int rows = sheetDetailsForScript.getPhysicalNumberOfRows();
			
			for (int i = 1; i < rows; i++) {

				GetExcelCellVaueUtil cellVaueUtil = new GetExcelCellVaueUtil();

				TestCase excelTestScriptVO = new TestCase();
				excelTestScriptVO.setTestStepNo(cellVaueUtil.getCellValueForFirstColumn(sheetDetailsForScript.getRow(i).getCell(0)));
				excelTestScriptVO.setTestStepDescription(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(1)));
				excelTestScriptVO.setTestStepScreenShotRequired(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(2)));
				excelTestScriptVO.setKeyword(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(3)));
				excelTestScriptVO.setControlName(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(4)));
				excelTestScriptVO.setParam1(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(5)));
				excelTestScriptVO.setParam2(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(6)));
				excelTestScriptVO.setParam3(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(7)));
				excelTestScriptVOsList.add(excelTestScriptVO);
			}
		} 
		finally 
		{
			if(workbookForScript!=null)
				workbookForScript.close();
			
			if(fileInputStreamForScript!=null)
				fileInputStreamForScript.close();
		}
		return excelTestScriptVOsList;
	}
	
	public List<TestSuite> getTestScriptsToExecute(boolean isTestDataInRunner) throws IOException
	{
		PropertyFileReader propertyFileReader = new PropertyFileReader(Driver.PROPERTY_FILE_LOCATION);
		
		String runnerFile = propertyFileReader.getValue("BASE_LOCATION")+propertyFileReader.getValue("RUNNER_LOCATION");
		
		File fileForScript = null;
		FileInputStream fileInputStreamForScript = null;
		Workbook workbookForScript = null;
		Sheet sheetDetailsForScript = null;
		List<TestSuite> testSuite = new ArrayList<TestSuite>();
		
		try 
		{
			fileForScript = new File(runnerFile);
			fileInputStreamForScript = new FileInputStream(fileForScript);
			workbookForScript = new HSSFWorkbook(fileInputStreamForScript);
			sheetDetailsForScript = workbookForScript.getSheetAt(0);
			int rows = sheetDetailsForScript.getPhysicalNumberOfRows();
			
			GetExcelCellVaueUtil cellVaueUtil = new GetExcelCellVaueUtil();
			
			for (int i = 1; i<rows; i++) 
			{
				if (sheetDetailsForScript.getRow(i)!=null && sheetDetailsForScript.getRow(i).getCell(2)!=null &&
						cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(2)).equalsIgnoreCase("yes"))
				{
					if(isTestDataInRunner)
					{
						TestSuite testSuiteObject = new TestSuite();
						testSuiteObject.setRowNumber(cellVaueUtil.getCellValueForFirstColumn(sheetDetailsForScript.getRow(i).getCell(0)));
						testSuiteObject.setScriptName(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(1)));
						testSuiteObject.setIsScriptToBeExecuted(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(2)));
						
						int  lastColumn = sheetDetailsForScript.getRow(i).getLastCellNum();
						HashMap<String, String> excelTestData = new HashMap<String, String>();
							
						for(int j = 3; j < lastColumn; j++)
						{
							if(sheetDetailsForScript.getRow(i).getCell(j)!=null)
							{
								String rowData = sheetDetailsForScript.getRow(i).getCell(j).getStringCellValue();
								if (rowData.contains("="))
								{
									String key = rowData.substring(0,rowData.indexOf( "=" )).trim();
									String value = rowData.substring(rowData.indexOf( "=" )+1,rowData.length()).trim();
									excelTestData.put(key, value);
								}
							}
						}
						testSuiteObject.setTestData(null, null, excelTestData);
						
						testSuite.add(testSuiteObject);
					}
					else
					{
						List<TestData> testDataSheet = readTestData(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(1)));
						
						for(TestData testData : testDataSheet)
						{
							TestSuite testSuiteObject = new TestSuite();
							
							testSuiteObject.setRowNumber(cellVaueUtil.getCellValueForFirstColumn(sheetDetailsForScript.getRow(i).getCell(0)));
							testSuiteObject.setScriptName(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(1)));
							testSuiteObject.setIsScriptToBeExecuted(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(2)));
							testSuiteObject.setTestDataSNo(testData.getsNo());
							testSuiteObject.setTestDataDescription(testData.getTestStepDescription());
							testSuiteObject.setTestData(null, null, testData.getTestData());
							
							testSuite.add(testSuiteObject);
						}
					}
				}
			}
		} 
		finally 
		{
			if(workbookForScript!=null)
				workbookForScript.close();
			
			if(fileInputStreamForScript!=null)
				fileInputStreamForScript.close();
		}
		
		return testSuite;
	}
	
	public static List<TestData> readTestData(String scriptName) throws IOException {

		PropertyFileReader propertyFileReader = new PropertyFileReader(Driver.PROPERTY_FILE_LOCATION);
		String testCaseFolder = propertyFileReader.getValue("BASE_LOCATION")+propertyFileReader.getValue("TEST_CASE_FOLDER");
		
		String filePath = testCaseFolder+scriptName+".xls";
		
		System.out.println("\n\nTest Script file: " + filePath);
		
		File fileForScript = null;
		FileInputStream fileInputStreamForScript = null;
		Workbook workbookForScript = null;
		Sheet sheetDetailsForScript = null;
		
		HashMap<String, String> excelTestData = new HashMap<String, String>();
		
		List<TestData> testDataSheet = new ArrayList<TestData>();

		try {
			fileForScript = new File(filePath);
			fileInputStreamForScript = new FileInputStream(fileForScript);
			
			if(filePath.contains("xlsx")) {
				workbookForScript = new XSSFWorkbook(fileInputStreamForScript);
			}else{
				workbookForScript = new HSSFWorkbook(fileInputStreamForScript);
			}
			sheetDetailsForScript = workbookForScript.getSheet("Test Data");

			int rows = sheetDetailsForScript.getPhysicalNumberOfRows();
			
			for (int i = 1; i < rows; i++) 
			{
				int  lastColumn = sheetDetailsForScript.getRow(i).getLastCellNum();
				
				GetExcelCellVaueUtil cellVaueUtil = new GetExcelCellVaueUtil();
				
				TestData testData = null;
				
				if(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(2)).equalsIgnoreCase("yes") || 
						cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(2)).equalsIgnoreCase("y"))
				{
					testData = new TestData();
					
					testData.setsNo(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(0)));
					testData.setTestStepDescription(cellVaueUtil.getCellValue(sheetDetailsForScript.getRow(i).getCell(1)));
					excelTestData = new HashMap<String, String>();
					
					for(int j = 3; j < lastColumn; j++)
					{
						String rowData = sheetDetailsForScript.getRow(i).getCell(j).getStringCellValue();
						if (rowData.contains("="))
						{
							String key = rowData.substring(0,rowData.indexOf( "=" )).trim();
							String value = rowData.substring(rowData.indexOf( "=" )+1,rowData.length()).trim();
							excelTestData.put(key, value);
						}
					}
					
					testData.setTestData(excelTestData);
					testDataSheet.add(testData);
				}
				
			}
		} 
		finally 
		{
			if(workbookForScript!=null)
				workbookForScript.close();
			
			if(fileInputStreamForScript!=null)
				fileInputStreamForScript.close();
		}
		return testDataSheet;
	}
	
	public void readObjectRepo() throws IOException
	{

		PropertyFileReader propertyFileReader = new PropertyFileReader(Driver.PROPERTY_FILE_LOCATION);
		String filePath = propertyFileReader.getValue("BASE_LOCATION")+propertyFileReader.getValue("OBJECT_REPO_FOLDER");
		
		//String filePath = objectRepoLocation+".xls";
		
		System.out.println("\n\nObject Repository file: " + filePath);
		
		File file = null;
		FileInputStream fileInputStream = null;
		Workbook workbook = null;
		Sheet sheetDetails = null;
		
		try 
		{
			file = new File(filePath);
			fileInputStream = new FileInputStream(file);
			
			if(filePath.contains("xlsx")) {
				workbook = new XSSFWorkbook(fileInputStream);
			}else{
				workbook = new HSSFWorkbook(fileInputStream);
			}
			sheetDetails = workbook.getSheet("Object Repo");

			int rows = sheetDetails.getPhysicalNumberOfRows();
			
			for (int i = 1; i < rows; i++) 
			{
				GetExcelCellVaueUtil cellVaueUtil = new GetExcelCellVaueUtil();
				
				FrameworkConstants.ObjectRepository.put(cellVaueUtil.getCellValue(sheetDetails.getRow(i).getCell(0)), 
						cellVaueUtil.getCellValue(sheetDetails.getRow(i).getCell(1))+";;" + cellVaueUtil.getCellValue(sheetDetails.getRow(i).getCell(2)));
			}
		}
		finally 
		{
			if(workbook!=null)
				workbook.close();
			
			if(fileInputStream!=null)
				fileInputStream.close();
		}
	}
	

	/**
	 * Main method
	 *
	 * @param args
	 *            - arguments
	 */
	/*public static void main(String[] args) {
		ExcelReader excelTestScript = new ExcelReader();
		try {
			List<TestSuite> testSuite = excelTestScript.getTestScriptsToExecute();
			//System.out.println(testSuite.size());
			
			
			for(TestSuite test :testSuite)
			{
				System.out.println(excelTestScript.readTestCase(test.getScriptName()).size());
				System.out.println(test.getScriptName());
				System.out.println(test.getTestData());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// excelTestScript.readWorkbook("");
	}*/
}
