package com.telus.wnp.steps;

import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;
import com.telus.wnp.pages.MSSPartnersPage_IR;
import com.telus.wnp.pages.MSSPartnersPage_IR;
import com.telus.wnp.utils.GenericUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSession;

public class MSSPartnersPageSteps_IR extends BaseSteps {

	public static String activatedBAN = "Undefined";
	public static String activatedTN = "Undefined";

	public static void main(String[] args) {

		String encrptData = "Consil141";

		byte[] encodedBytes = Base64.getEncoder().encode(encrptData.getBytes());

		System.out.println("encodedBytes --------------->" + new String(encodedBytes));
	}

	/**
	 * This method is used to do verify home page is displayed after successful
	 * login into smart desktop application.
	 * 
	 * @param none.
	 * @return nothing.
	 */
	public static void verifyMSSHomePageIsDisplayed() {
		Reporting.logReporter(Status.INFO, "STEP ===> MSS homepage is displayed after successful login ");

		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		boolean actual = MSSPartnersPage_IR.isHomePageDisplayed();
		System.out.println("===========> MSS");
		Validate.assertEquals(actual, true, "Unable to login into MSS.", false);
	}

	public static String performIRActivationFromMSS(JSONObject staticDataJson, String subscriberType,
			List<String> activeData, JSONObject userAccess) {

		Reporting.logReporter(Status.INFO, "STEP ===> Perform Activation from MSS ");
		String Output = subscriberType + "IR-" + activeData.get(0);
		String status = "";
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		try {
			try {
				if (subscriberType.equalsIgnoreCase("KOODO")) {
					LoginPageSteps.appLogin_KOODO_MSS();
				} else {
					LoginPageSteps.appLogin_TELUS_MSS();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			verifyMSSHomePageIsDisplayed();

			// FormDetails1
			selectBanType(staticDataJson, MSSPartnersPage_IR);
			// // FormDetails2 and FormDetails3
			//
			enterCustDetails(staticDataJson, activeData, MSSPartnersPage_IR);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			selectPhoneNumber(activeData, MSSPartnersPage_IR);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			OffersPromo(staticDataJson, activeData, MSSPartnersPage_IR);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			PricePlanAddOn(staticDataJson, MSSPartnersPage_IR, subscriberType);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			OrderConfirmation(MSSPartnersPage_IR);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			Checkout();

			BaseSteps.Waits.waitUntilPageLoadComplete();

			finalVerification(subscriberType);

			status = "Completed";

		} catch (AssertionError e) {
			status = "ActivationFailed";
			Reporting.logReporter(Status.FAIL, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to activate subscriber from MSS Portal");

		} catch (Exception e) {
			status = "ActivationFailed";
			Reporting.logReporter(Status.FAIL, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to activate subscriber from MSS Portal");

		} finally {
			return Output + "-" + activatedBAN + "-" + activatedTN + "-" + status;
		}
	}

	public static void finalVerification(String subscriberType) {

		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.tnToBeActivated, 90);
		activatedTN = MSSPartnersPage_IR.getTNToBeActivatedText();
		Reporting.logReporter(Status.INFO, "Activated TN: " + activatedTN);

		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.statusOfActivationProcess);
		String workFlowStatus = MSSPartnersPage_IR.getActivationStatusText();
		Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

		String originalWindow = WebDriverSession.getWebDriverSession().getWindowHandle();

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.activatedAccountNumber, 30);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.activatedAccountNumber);
		activatedBAN = MSSPartnersPage_IR.getActivatedAccountNumberText();
		Reporting.logReporter(Status.INFO, "Activated BAN: " + activatedBAN);

		Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);

		if (subscriberType.equals("KOODO")) {
			BaseSteps.Windows.switchToNewWindow();
			String billWindow = WebDriverSession.getWebDriverSession().getWindowHandle();
			BaseSteps.Windows.closeUniqueWindow(billWindow);
			WebDriverSession.getWebDriverSession().switchTo().window(originalWindow);

		}

	}

	public static void pageload() {
		JavascriptExecutor js = BaseSteps.JavaScripts.getJavaScriptExecutor();
		while (js.executeScript("return document.readyState").toString().equals("complete")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void enterCustomerInformation(List<String> dynamicData, JSONObject testDataJson) {
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		// BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.firstName);
		BaseSteps.Waits.waitUntilPageLoadComplete();
		String firstName = GenericUtils.generateRandomName(testDataJson.getString("FIRST_NAME"));
		String lastName = GenericUtils.generateRandomName(testDataJson.getString("LAST_NAME"));
		String homePhoneNumber = testDataJson.getString("HOME_PHONE_NUMBER")
				+ GenericUtils.generateRandomInteger(99999);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.firstName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.firstName, firstName);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.lastName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.lastName, lastName);

		BaseSteps.Waits.waitGeneric(5000);
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.birthMonthDrpDwn,
				testDataJson.getString("BIRTH_MONTH"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.birthDateDrpDwn,
				testDataJson.getString("BIRTH_DATE"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.birthYearDrpDwn,
				testDataJson.getString("BIRTH_YEAR"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.homePhoneNumber, homePhoneNumber);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.detailedAddress);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.detailedAddress);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.streetNumber, testDataJson.getString("STREET_NUMBER"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.streetName, testDataJson.getString("STREET_NAME"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.customerCity, dynamicData.get(3)); // input List(dynamicData) have
																							// City Value at 3rd Index
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.provinceDrpDwn, dynamicData.get(2)); // input
																										// List(dynamicData)
																										// have Province
																										// at 2nd Index
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.postalCode, testDataJson.getString("POSTAL_CODE"));

		Validate.takeStepFullScreenShot("Customer Information", Status.INFO);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.creditCheckBtn);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.creditCheckBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.creditCheckBtn);
	}

	public static void clickEasyRoamCheckBoxIfDisplayed() {
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		try {

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.easyRoamLabel);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.easyRoamLabel);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.easyRoamChkBox);

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Easy Roam Check Box Not displayed");
		}
	}

	public static void enterSINNumber(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {

		String SINNum = testDataJson.getString("SIN_NUMBER") + GenericUtils.generateRandomInteger(9999);
		try {
			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.sinLink);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.sinLink);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.sinLink);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.enterSIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterSIN, SINNum);
			BaseSteps.Waits.waitGeneric(1000);
			boolean errorMsgVisible = MSSPartnersPage_IR.sinErrorMsg.isDisplayed();
			while (errorMsgVisible) {
				SINNum = testDataJson.getString("SIN_NUMBER") + GenericUtils.generateRandomInteger(9999);
				BaseSteps.SendKeys.clearFieldAndSendKeys(MSSPartnersPage_IR.enterSIN, SINNum);
				BaseSteps.Waits.waitGeneric(100);
				errorMsgVisible = MSSPartnersPage_IR.sinErrorMsg.isDisplayed();
				if (!errorMsgVisible) {
					break;
				}
			}
			Validate.takeStepScreenShot("Entered SIN Number");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.enterSINSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.enterSINSubmitButton);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to enter SIN");
		}

	}

	public static void handleCreateNewAccountPopup(MSSPartnersPage_IR MSSPartnersPage_IR) {

		try {
			BaseSteps.Windows.switchToNewWindow();
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.createNewAccountBtnFromPopup);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.createNewAccountBtnFromPopup);
			Validate.takeStepScreenShot("Entered Subscriber Information");
		} catch (Exception e) {
			System.out.println(e);
			Reporting.logReporter(Status.INFO, "Duplicate Account Pop up not displayed");
		}

	}

	public static void handleConfirmAddressdialog(MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			WebDriver driver = WebDriverSession.getWebDriverSession();
			driver.switchTo().activeElement();
			driver.findElement(By.xpath("//*[@id=\"suggestAddressRadio999\"]")).click();
			driver.findElement(By.xpath("//*[@id=\"credit-check-driver-license-cta\"]/a")).click();
			Reporting.logReporter(Status.INFO, "Confirmed Address");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void selectRequiredOffer(JSONObject testDataJson) {
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		try {

			String offerName = testDataJson.getString("MSS_OFFER_HEADER");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_IR.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.clickMSSAutomationOfferExpandableButton(programId));

			String offerType = testDataJson.getString("MSS_OFFER_NAME");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_IR.selectSpecificOfferForActivation(programId, offerType));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.selectSpecificOfferForActivation(programId, offerType));
			boolean offerRadioBtnChck = MSSPartnersPage_IR.offerRadioBtnChck(offerName,offerType).isSelected();
			if (!offerRadioBtnChck) {
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectSpecificOfferForActivation(programId, offerType));

			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select specific offer" + e);
		}
	}

	public static void declinePerkOfferIdIfDisplayed(MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.declinePerkOfferId, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.declinePerkOfferId);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.declinePerkOfferId);

			Validate.takeStepScreenShot("Form Details6", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.submitPerkOffer);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitPerkOffer);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Optional Perks Displayed" + e);
		}
	}

	public static void mobileAndHomePromotionValPopUpIfDisplayed(MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.PopUpcontinueToPricing, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.PopUpcontinueToPricing);
			Validate.takeStepScreenShot("mobile And Home Promotion Val PopUp ", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.PopUpcontinueToPricing);

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "No Popup displayed for Validation" + e);

		}
	}

	public static void selectDefaultPromotions(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_IR.searchForSpecificPromotionOffer(programName));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.searchForSpecificPromotionOffer(programName));
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.searchForSpecificPromotionOffer(programName));
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Default Promotions Not Displayed" + e);
		}
	}

	public static void selectNewRatePlan(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.newRatePlanDropDown, 60);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.newRatePlanDropDown);
			BaseSteps.Waits.waitGeneric(2000);

			try {
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.newRatePlanDropDown);
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.newRatePlanDropDown,
						testDataJson.getString("NEW_RATE_PLAN"));
			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Required rate plan needs to be selected by going through the list");

				try {
					// WebDriver driver = WebDriverSession.getWebDriverSession();
					// Select offer = new
					// Select(driver.findElement(By.xpath("//*[@id='introductionNewRatePlan']")));
					selectByGoingThroughList(MSSPartnersPage_IR.newRatePlanDropDown,
							testDataJson.getString("NEW_RATE_PLAN"));
					BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToAddOnBtn);

				} catch (Exception E) {

				}
			}

		} catch (Exception e) {

			Reporting.logReporter(Status.INFO, "Unable to select New Rate Plan" + e);
		}
	}

	public static void handleEasyRoamCheckBox(MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			clickEasyRoamCheckBoxIfDisplayed();
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.finalContinueBtn, 60);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Easy Roam Checkbox not displayed" + e);

		}
	}

	public static void checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_IR MSSPartnersPage_IR) {
		MSSPartnersPage_IR = new MSSPartnersPage_IR();
		try {
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueButtonBeforeMovingToAddOn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueButtonBeforeMovingToAddOn);
			Reporting.logReporter(Status.INFO, "Warning pop-up for selected offer not Valid is displayed");
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Warning pop-up for selected offer not Valid is Not displayed" + e);

		}
	}

	/**
	 * This method is used to do verify home page is displayed after successful
	 * login into smart desktop application.
	 * 
	 * @param none.
	 * @return nothing.
	 */
	public static void selectRequiredOffer_NEW(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		MSSPartnersPage_IR = new MSSPartnersPage_IR();
		try {

			String offerName = testDataJson.getString("MSS_OFFER_HEADER");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			String offerType = testDataJson.getString("MSS_OFFER_NAME");

			try {

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				// offerRadioBtnChck
				boolean button = MSSPartnersPage_IR.offerRadioBtnChck();
				if (!MSSPartnersPage_IR.offerRadioBtnChck()) {
					BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				}

			} catch (Exception e) {
				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.expandSpecificConsumerOffer(programId));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.expandSpecificConsumerOffer(programId));
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.expandSpecificConsumerOffer(programId));

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
			}

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select specific offer" + e);
		}
	}

	public static void selectBanType(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		boolean actual = false;
		BaseSteps.Waits.waitUntilPageLoadComplete();
		try {
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectAccountType(testDataJson.getString("ACCOUNT_TYPE")));
			Validate.takeStepScreenShot("Form Details1", Status.INFO);
			System.out.println("IsClickable: " + MSSPartnersPage_IR.submitButton.isEnabled());
			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.submitButton);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void enterCustDetails(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_IR MSSPartnersPage_IR) {
		boolean actual = false;

		try {
			BaseSteps.Waits.waitUntilPageLoadComplete();

			enterCustomerInformation(dynamicData, testDataJson);

			// String SINNum = testDataJson.getString("SIN_NUMBER") +
			// GenericUtils.generateRandomInteger(9999);
			enterSINNumber(testDataJson, MSSPartnersPage_IR);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.consentCheckBox);
			Validate.takeStepScreenShot("Form Details2", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPreferenceBtn);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToPreferenceBtn);
			Validate.takeStepScreenShot("Continue to Preference");
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPreferenceBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.enterPIN);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.enterPIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterPIN, testDataJson.getString("PIN"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.enterEmailId);
			String emailId = GenericUtils.getRandomEmailId();
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterEmailId, emailId);

			Validate.takeStepFullScreenShot("Form Details3", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToSubscriberInfoBtn);
			Validate.takeStepScreenShot("Continue to Subscriber Information");
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToSubscriberInfoBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.sameAsCustomerInfoCheckBox);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.sameAsCustomerInfoCheckBox);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.submitButtonAferSubInforFilled);
			Validate.takeStepFullScreenShot("Entered Subscriber Information", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitButtonAferSubInforFilled);

			handleCreateNewAccountPopup(MSSPartnersPage_IR);
			handleConfirmAddressdialog(MSSPartnersPage_IR);
			actual = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void selectPhoneNumber(List<String> dynamicData, MSSPartnersPage_IR MSSPartnersPage_IR) {
		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.provinceLabel, 90);
			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.choosePhoneNumberRadioBtn);
			BaseSteps.Waits.waitGeneric(2000);

			Select selectProvience = new Select(
					BaseSteps.Finds.findElement(By.xpath("//*[@id=\"introductionProvince\"]")));
			Select selectCity = new Select(BaseSteps.Finds.findElement(By.xpath("//*[@id=\"introductionCity\"]")));
			selectProvience.selectByVisibleText(dynamicData.get(2)); // Province is as 2nd Index
			BaseSteps.Waits.waitGeneric(1000);
			selectCity.selectByVisibleText(dynamicData.get(3).toLowerCase()); // City is as 3rd Index

			BaseSteps.Waits.waitGeneric(3000);
			Validate.takeStepFullScreenShot("Select phone number", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToSelectPhoneNumberBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToSelectPhoneNumberBtn);
			BaseSteps.Waits.waitGeneric(3000);

			String newseriesnumber = dynamicData.get(1); // NPANXX is at 1st Index of input List
			if (!newseriesnumber.equals("")) {
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectNewSeriesNumber(newseriesnumber));
				BaseSteps.Waits.waitGeneric(7000);
			} else {
				WebDriver driver = WebDriverSession.getWebDriverSession();
				Select NumList = new Select(WebDriverSession.getWebDriverSession()
						.findElement(By.xpath("//*[@id=\"select-phone-number-choose-areacode-areacode\"]")));
				for (int i = 0; i < NumList.getOptions().size(); i++) {
					NumList.selectByValue("" + i);
					BaseSteps.Waits.waitGeneric(7000);
					try {
						if (driver.findElement(By.xpath(
								"//*[@id=\"select-phone-number-choose-areacode-numbers-container\"]/div[1]/span"))
								.getText().contains("Available phone numbers")) {
							break;
						}
					} catch (Exception e) {
						BaseSteps.Debugs.scrollToElement(WebDriverSession.getWebDriverSession()
								.findElement(By.xpath("//*[@id=\"select-phone-number-choose-areacode-areacode\"]")));
						continue;
					}
				}
			}

			Validate.takeStepFullScreenShot("Select phone number", Status.INFO);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.submitBtnOnPhoneNumSelectionPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitBtnOnPhoneNumSelectionPage);
			try {
				BaseSteps.Waits.waitGeneric(2000);
				boolean phoneNumberTakenError = MSSPartnersPage_IR.phoneNumberTakenError();
				while (phoneNumberTakenError) {
					
					BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.selectNextNumber);
					BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectNextNumber);
					BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.submitBtnOnPhoneNumSelectionPage);
					BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitBtnOnPhoneNumSelectionPage);
					phoneNumberTakenError = MSSPartnersPage_IR.phoneNumberTakenError();
				}

			} catch (Exception e) {

			}
			actual = true;
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void OffersPromo(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_IR MSSPartnersPage_IR) {
		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.selectADeviceBtn, 60);
			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.simOnlyActivationChkBox);
			BaseSteps.Waits.waitGeneric(5000);

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterValidSIMNumber, dynamicData.get(0));
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterValidSIMNumber, Keys.TAB);
			try {
				BaseSteps.Waits.waitForElementInvisibilityLongWait(WebDriverSession.getWebDriverSession()
						.findElement(By.xpath("//*[@id=\"introduction-options-device-sim-checkmark-icon\"]/span")));
				BaseSteps.Waits.waitGeneric(10000);
				BaseSteps.Waits
						.waitForElementInvisibility(MSSPartnersPage_IR.deviceTypeDropDownOfferTypeSmartphoneOption);
			} catch (Exception e) {
			}
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.deviceTypeDropDownOfferTypeSmartphoneOption);
			BaseSteps.Waits.waitGeneric(5000);

			Validate.takeStepScreenShot("SIM,IMEI,DEVICE Information", Status.INFO);
			BaseSteps.Waits.waitGeneric(5000);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToOffersPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToOffersPage);

			BaseSteps.Waits.waitGeneric(2000);

			selectRequiredOffer(testDataJson);

			Validate.takeStepScreenShot("Form Details5", Status.INFO);

			BaseSteps.Waits.waitGeneric(2000);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToPromotionsPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPromotionsPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPromotionsPage);

			declinePerkOfferIdIfDisplayed(MSSPartnersPage_IR);

			selectDefaultPromotions(testDataJson, MSSPartnersPage_IR);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPricing);

			Validate.takeStepFullScreenShot("Form Details7", Status.INFO);

			mobileAndHomePromotionValPopUpIfDisplayed(MSSPartnersPage_IR);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueAfterPricing);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueAfterPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueAfterPricing);
			actual = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void PricePlanAddOn(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR,
			String subscriberType) {
		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.newRatePlanDropDown, 60);
			BaseSteps.Waits.waitGeneric(5000);

			selectNewRatePlan(testDataJson, MSSPartnersPage_IR);

			BaseSteps.Waits.waitGeneric(10000);
			Validate.takeStepFullScreenShot("Form Details8", Status.INFO);

			checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_IR);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToAddOnBtn, 60);
			BaseSteps.Waits.waitGeneric(5000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToAddOnBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToAddOnBtn);
			try {
				BaseSteps.Clicks.clickElement(
						BaseSteps.Finds.findElement(By.xpath("//*[@id='add-ons-content']/div[1]/span/a[2]")));
				String IDList = "";
				if (subscriberType.equals("KOODO")) {
					IDList = "7-03SERUS12,7-03SERIN15,7-3SINTDTEN,7-3SWORLDPH,7-03SERID15,7-03SERUD12";
				} else {
					IDList = "7-SERI13,7-SRLH9,7-SINTL,7-SINTDTENB,7-0SERID15,7-0SERUD12";
				}
				String[] Elementid = IDList.split(",");
				for (String e : Elementid) {
					try {
						WebElement temp = BaseSteps.Finds.findElement(By.id(e));
						BaseSteps.Debugs.scrollToElement(temp);
						BaseSteps.CheckBoxes.uncheckCheckbox(temp);
					} catch (Exception e1) {
						System.out.println(e1);
					}
				}
			} catch (Exception e) {
			}
			Validate.takeStepFullScreenShot("Form Details9", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToFinalPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToFinalPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToFinalPricing);

			Validate.takeStepFullScreenShot("Form Details10", Status.INFO);

			handleEasyRoamCheckBox(MSSPartnersPage_IR);

			Validate.takeStepFullScreenShot("Price Plans and Add On Details", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.finalContinueBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.finalContinueBtn);
			actual = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void OrderConfirmation(MSSPartnersPage_IR MSSPartnersPage_IR) {
		boolean actual = false;

		try {
			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			try {
				BaseSteps.Waits.waitUntilPageLoadComplete();
				// BaseSteps.Waits.waitForElementVisibilityLonfWait(MSSPartnersPage_IR.orderConfirmationPage,
				// 60);
				// BaseSteps.Waits.waitForElementInvisibilityLongWait(MSSPartnersPage_IR.orderConfirmationPage);
				BaseSteps.Waits.waitGeneric(10000);
				BaseSteps.Debugs.scrollToElement(WebDriverSession.getWebDriverSession()
						.findElement(By.xpath("//span[contains(text(),'Extended Warranty')]")));
				executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.doNotWantExtendedOfferRadioBtn);

			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

			}

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.proceedToCheckoutBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.proceedToCheckoutBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.proceedToCheckoutBtn);
			actual = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void Checkout() {
		boolean actual = false;
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.saveAndShareBtn, 60);
			Validate.takeStepFullScreenShot("Checkout Page Details", Status.INFO);
			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.mailServiceAgreementCopyRadioBtn);
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.finalAgreeCheckBox);
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.finalCreditCheckBox);

			Validate.takeStepFullScreenShot("MSS Detais - Before Final Submit", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.finalSubmitButton);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.finalSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.finalSubmitButton);
			actual = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to get expected Result", false);
	}

	public static void selectByGoingThroughList(WebElement dropdownElement, String expectedOptionText) {
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		Select selectList = new Select(dropdownElement);

		int selectSize = selectList.getOptions().size();
		Reporting.logReporter(Status.DEBUG,
				"Size of list is " + selectSize + NAME_OF_CURR_CLASS + "." + nameofCurrMethod);

		for (int i = 0; i <= selectSize; i++) {

			BaseSteps.Clicks.clickElement(dropdownElement);
			selectList.selectByIndex(i);

			String selectedOption = selectList.getFirstSelectedOption().getText();

			Reporting.logReporter(Status.DEBUG,
					"Selected Item " + selectedOption + NAME_OF_CURR_CLASS + "." + nameofCurrMethod);

			BaseSteps.Waits.waitUntilPageLoadComplete();
			if (selectedOption.trim().equals(expectedOptionText.trim())) {
				Reporting.logReporter(Status.DEBUG, "Value Matched" + NAME_OF_CURR_CLASS + "." + nameofCurrMethod);
				break;
			} else {
				Reporting.logReporter(Status.DEBUG, "Value Not Matched" + NAME_OF_CURR_CLASS + "." + nameofCurrMethod);
				if (i == selectSize) {
					Reporting.logReporter(Status.DEBUG, "Value NOT FOUND in List " + expectedOptionText
							+ NAME_OF_CURR_CLASS + "." + nameofCurrMethod);
				}

			}
		}
	}

}
