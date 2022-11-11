package com.telus.wnp.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.telus.wnp.steps.MSSPartnersPageSteps_IR;
import com.telus.wnp.utils.GenericUtils;
import com.test.files.interaction.ReadJSON;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.SystemProperties;

public class MSSPartnersPageTest_TelusIR extends BaseTest {

	String testCaseName = null;
	String scriptName = null;
	String testCaseDescription = null;
	String environment = null;
	String testDataFilePath = null;
	JSONObject testDataJson = null;
	JSONObject fixedDataJson = null;
	JSONObject dynamicDataJson = null;
	JSONObject userAccess = null;
	List<String> activationList = new ArrayList<String>();

	/**
	 * @param iTestContext
	 */
	@BeforeTest(alwaysRun = true)
	public void BeforeMethod(ITestContext iTestContext) {

		testCaseName = this.getClass().getName();
		scriptName = GenericUtils.getTestCaseName(testCaseName);
		testCaseDescription = "The purpose of this test case is to verify \"" + scriptName + "\" workflow";
		testDataFilePath = "\\TestData\\" + scriptName + ".json";
		JSONObject jsonFileObject = GenericUtils.getJSONObjectFromJSONFile(testDataFilePath);
		environment = SystemProperties.EXECUTION_ENVIRONMENT;
		testDataJson = jsonFileObject.getJSONObject(environment);
		fixedDataJson = testDataJson.getJSONObject("MSS_CONSTANTS");
		dynamicDataJson = testDataJson.getJSONObject("SUBSCRIBER_DETAILS");
		JSONObject userAccessJsonFile = new JSONObject(ReadJSON.parse("UserAccess.json"));
		userAccess = userAccessJsonFile.getJSONObject(SystemProperties.EXECUTION_ENVIRONMENT);
	}

	@Test(dataProvider = "test-data", groups = { "MSS_Activation_TelusIR" })
	public void testMethod_MSS(ITestContext iTestContext, String dynamicData) throws Exception {
		/*** INPUT DYNAMIC DATA ***/
		Reporting.setNewGroupName("Input Activation DATA");
		List<String> activationData = Arrays.asList(dynamicData.split("-"));
		boolean stat = GenericUtils.validateSIMDetails(dynamicData);
		Validate.assertEquals(stat, true, true);
		Reporting.printAndClearLogGroupStatements();

		/*** Test Case Details ***/
		Reporting.setNewGroupName("Automation Configurations / Environment Details & Data Setup");
		Reporting.logReporter(Status.INFO,
				"Automation Configuration - Environment Configured for Automation Execution [" + environment + "]");
		Reporting.logReporter(Status.INFO, "Test Data File for " + scriptName + " placed at : " + testDataFilePath);
		Reporting.printAndClearLogGroupStatements();

		Reporting.setNewGroupName("Test Case Details");
		Reporting.logReporter(Status.INFO, "Test Case Name : [" + scriptName + "]");
		Reporting.logReporter(Status.INFO, "Test Case Description : [" + testCaseDescription + "]");
		Reporting.printAndClearLogGroupStatements();

		/*** MSS WORKFLOW STEPS ***/
		Reporting.setNewGroupName("MSS TELUS IR ACTIVATION");
		activationList.add(
				MSSPartnersPageSteps_IR.performIRActivationFromMSS(fixedDataJson, "TELUS", activationData, userAccess));
		Reporting.printAndClearLogGroupStatements();

	}

	@DataProvider(name = "test-data")
	public Object[][] dataProvFunc() {

		String subDetails = "ACTIVATION_DATA";
		String[][] arr1 = GenericUtils.readDataFromJSONFile(dynamicDataJson, subDetails);
		return arr1;

	}

	/**
	 * Close any opened browser instances
	 */
	@AfterClass(alwaysRun = true)
	public void afterTest() {
		// Reporting Consolidated List of Activation Data
		Reporting.setNewGroupName("Consilidated Activation List");
		for (String temp : activationList) {
			Reporting.logReporter(Status.INFO, temp);
		}
		Reporting.printAndClearLogGroupStatements();

		// Close any opened browser instances
		Reporting.setNewGroupName("Close All Browser");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();
	}

}