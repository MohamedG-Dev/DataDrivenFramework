package dataDriven.Demo.Tests;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import dataDriven.Demo.Base.BaseTest;
import dataDriven.Demo.Util.DataUtil;
import dataDriven.Demo.Util.ExcelReader;
import dataDriven.Demo.Util.ExtentReportsManager;

public class LoginTest extends BaseTest {
	ExcelReader excelReader;

	@BeforeClass
	public void init() {
		initialize();
	}

	@DataProvider
	public Object[][] getInputData() {
		// get data from the excel file
		Object[][] obj = null;
		try {
			String excelFilePath = prop.getProperty("xlsxFilePath");
			excelReader = new ExcelReader(excelFilePath);
			obj = DataUtil.getTestData(excelReader, "LoginTest", "Data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Test(dataProvider = "getInputData")
	public void testLoginPage(HashMap<String, String> dataSet) {
		eReport = ExtentReportsManager.getInstance();
		eTest = eReport.createTest("Validation of Login Page");
		eTest.log(Status.INFO, "Testing of login page started");
		if (!DataUtil.isRunnable(excelReader, "Login Test", "Testcases") || dataSet.get("Runmode").equals("N")) {
			eTest.log(Status.SKIP, "Skipping the Test Case as the RunMode is set to N");
			throw new SkipException("Skipping the Test Case as the RunMode is set to N");

		}
		// Automation Code starts here
		launchBrowser(dataSet.get("Browser"));
		navigateApp(prop.getProperty("appURL"));
		boolean actualStatus = doLogin(dataSet.get("Username"), dataSet.get("Password"));
		String expectedResult = dataSet.get("ExpectedResult");
		boolean expectedStatus = expectedResult.equalsIgnoreCase("failure") ? false : true;
		if (actualStatus == expectedStatus) {
			reportPass("LoginTest got passed");
		} else {
			reportFail("LoginTest got failed");
		}
	}
	
	public void testClosure() {
		if(eReport!=null)
		eReport.flush();
		if(driver!=null) 
			driver.quit();
	}
}
