package com.telus.wnp.steps;

import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;
import com.telus.wnp.pages.MSSPartnersPage_CE;
import com.telus.wnp.utils.GenericUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSession;

public class MSSPartnersPageSteps_CE extends BaseSteps {

	public static String activatedBAN = "Undefined";
	public static String activatedTN = "Undefined";
	public static JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

	/**
	 * This method is used to do verify home page is displayed after successful
	 * login into smart desktop application.
	 * 
	 * @param none.
	 * @return nothing.
	 */
	public static void verifyCE_MSSHomePageIsDisplayed() {
		Reporting.logReporter(Status.INFO, "STEP ===> MSS homepage is displayed after successful login ");

		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();
		boolean actual = MSSPartnersPage_CE.isCEHomePageDisplayed();
		System.out.println("===========> MSS");
		Validate.assertEquals(actual, true, "Unable to login into MSS.", false);
	}

	public static void performTelusCELogin(JSONObject testDataJson) {
		boolean firstPage = false;
		try {
			firstPage = WebDriverSession.getWebDriverForCurrentThreat().getTitle().contains("Channel Portal") ? true
					: false;
		} catch (Exception e) {
		}
		if (!firstPage) {
			LoginPageSteps.appLogin_TelusCorp_MSS();
			BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();
			verifyCE_MSSHomePageIsDisplayed();
			submitDealerInfo(testDataJson);

		}
	}

	public static String performTelusCEActivationFromMSS(JSONObject staticDataJson, List<String> activeData) {

		Reporting.logReporter(Status.INFO, "STEP ===> Perform Activation from MSS ");
		String Output = "TelusCE-" + activeData.get(0);
		performTelusCELogin(staticDataJson);
		String HomeTab = WebDriverSession.getWebDriverSession().getWindowHandle();
		WebDriverSession.getWebDriverSession().switchTo().window(HomeTab);
		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();

		try {
			selectCorporateCustomerLink(MSSPartnersPage_CE);
			BaseSteps.Windows.switchToNewWindow();
			selectRCID(staticDataJson, MSSPartnersPage_CE);
			selectBanType(MSSPartnersPage_CE);
			enterCustDetails(staticDataJson, activeData, MSSPartnersPage_CE);
			selectPhoneNumber(staticDataJson, activeData, MSSPartnersPage_CE);
			 OffersPromo(staticDataJson, activeData, MSSPartnersPage_CE);
			 PricePlanAddOn(staticDataJson, MSSPartnersPage_CE);
			 OrderConfirmation(MSSPartnersPage_CE);
			 Checkout(MSSPartnersPage_CE);
			 finalVerification(MSSPartnersPage_CE);

		} catch (

		Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to activate subscriber from MSS Portal");
			Output = Output + "-Failed";
		} finally {
			BaseSteps.Windows.closeNewWindow(HomeTab);
			return Output + "-" + activatedBAN + "-" + activatedTN;
		}

	}

	public static void enterCustomerInformation(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();

		try {

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.photoIdDrpDwn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.photoIdDrpDwn);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.photoIdDrpDwn, "BC Services Card");

			Reporting.logReporter(Status.INFO, "BC Services Card is Selected successfully");

		} catch (Exception e) {

			Reporting.logReporter(Status.INFO, "BC Services Card is not visible and trying one more time" + e);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.photoIdDrpDwn);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.photoIdDrpDwn, "BC Services Card");
		}

		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.firstName);

		String firstName = GenericUtils.generateRandomName(testDataJson.getString("FIRST_NAME"));
		String lastName = GenericUtils.generateRandomName(testDataJson.getString("LAST_NAME"));
		String homePhoneNumber = testDataJson.getString("HOME_PHONE_NUMBER")
				+ GenericUtils.generateRandomInteger(99999);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.firstName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.firstName, firstName);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.lastName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.lastName, lastName);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.detailedAddress);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.detailedAddress);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.streetNumber, testDataJson.getString("STREET_NUMBER"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.streetName, testDataJson.getString("STREET_NAME"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.customerCity, dynamicData.get(3));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.provinceDrpDwn, dynamicData.get(2));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.postalCode, testDataJson.getString("POSTAL_CODE"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.homePhoneNumber, homePhoneNumber);
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.birthMonthDrpDwn,
				testDataJson.getString("BIRTH_MONTH"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.birthDateDrpDwn,
				testDataJson.getString("BIRTH_DATE"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.birthYearDrpDwn,
				testDataJson.getString("BIRTH_YEAR"));

		Validate.takeStepFullScreenShot("Customer Information", Status.INFO);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.creditCheckBtn);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.creditCheckBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.creditCheckBtn);

	}

	public static void enterExternalPortinNumberDetails(JSONObject testDataJson,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
		executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.selectPortPhoneNumberRadioBtn);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterNumberToBePorted,
				testDataJson.getString("NUMBER_TO_BE_PORTED"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterNumberToBePorted, Keys.TAB);

		Validate.takeStepFullScreenShot("Form Details11", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToEnterPortDetailsBtn);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToEnterPortDetailsBtn);
		Validate.takeStepScreenShot("Continue to Enter Port Details");
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToEnterPortDetailsBtn);

		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.contactPhoneNumber);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.contactPhoneNumber,
				String.valueOf(GenericUtils.generateRandomMobileNumber()));

		executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.authorizePortChkBox);
		executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.confirmNumberInServiceChkBox);

		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.enterAccountNumberLink);
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.enterAccountNumber);
		String accountNum = testDataJson.getString("CONTACT_ACCOUNT_NUMBER") + GenericUtils.generateRandomInteger(9999);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterAccountNumber, accountNum);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.reEnterAccountNumber);
		BaseSteps.Waits.waitGeneric(200);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.reEnterAccountNumber, accountNum);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.confirmAccountNumberSubmitBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.confirmAccountNumberSubmitBtn);

		Validate.takeStepFullScreenShot("Form Details12", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.portDetailsSubmitBtn);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.portDetailsSubmitBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.portDetailsSubmitBtn);

	}

	public static void performPREToPOSTMigrationFromMSS(JSONObject testDataJson, String subscriberType) {

		// login into application
		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();
		BaseSteps.Waits.waitForElementInvisibilityLongWait(MSSPartnersPage_CE.modifyExistingAccount);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.modifyExistingAccount);

		BaseSteps.Windows.switchToNewWindow();
	}

	public static void clickEasyRoamCheckBoxIfDisplayed() {
		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.easyRoamLabel, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.easyRoamLabel);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.easyRoamChkBox);

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Easy Roam Check Box Not displayed");
		}
	}

	public static void enterSINNumber(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {

		String SINNum = testDataJson.getString("SIN_NUMBER") + GenericUtils.generateRandomInteger(9999);
		try {
			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.sinLink);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.sinLink);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.sinLink);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.enterSIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterSIN, SINNum);

			boolean errorMsgVisible = MSSPartnersPage_CE.sinErrorMsg.isDisplayed();
			while (errorMsgVisible) {
				SINNum = testDataJson.getString("SIN_NUMBER") + GenericUtils.generateRandomInteger(9999);
				BaseSteps.SendKeys.clearFieldAndSendKeys(MSSPartnersPage_CE.enterSIN, SINNum);
				BaseSteps.Waits.waitGeneric(100);
				errorMsgVisible = MSSPartnersPage_CE.sinErrorMsg.isDisplayed();

				if (!errorMsgVisible) {
					break;
				}

			}
			Validate.takeStepScreenShot("Entered SIN Number");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.enterSINSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.enterSINSubmitButton);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to enter SIN");
		}

	}

	public static void handleCreateNewAccountPopup(MSSPartnersPage_CE MSSPartnersPage_CE) {

		try {
			if(MSSPartnersPage_CE.createNewAccountBtnFromPopup.isDisplayed()) {
			BaseSteps.Windows.switchToNewWindow();
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.createNewAccountBtnFromPopup);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.createNewAccountBtnFromPopup);
			Validate.takeStepScreenShot("Entered Subscriber Information");
			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Duplicate Account Pop up not displayed");
		}

	}
	
	public static void continueBtnforCompanyCreditPopUp(MSSPartnersPage_CE MSSPartnersPage_CE) {

		try {
			if(MSSPartnersPage_CE.companyCreditPopUp.isDisplayed()) {
			WebDriverSession.getWebDriverSession().switchTo().activeElement();
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueBtnforCompanyCreditPopUp);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueBtnforCompanyCreditPopUp);
			Validate.takeStepScreenShot("Select Company Credit");
			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Company Credit Pop up not displayed");
		}

	}

	public static void handleAutofillProvince(List<String> dynamicData, JSONObject testDataJson) {
		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();
		String provinceCheck = MSSPartnersPage_CE.province_choosephonenumber();
		try {
			if (!(provinceCheck.equalsIgnoreCase(testDataJson.getString("PROVINCE"))))

			{
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.province_choosephonenumber,
						dynamicData.get(2));
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.city_choosephonenumber, dynamicData.get(3));

				Reporting.logReporter(Status.INFO, "Province and city selected");
			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "AutoFetch done");
		}
	}

	public static void handleConfirmAddressdialog(MSSPartnersPage_CE MSSPartnersPage_CE) {
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

	public static void selectRequiredOffer(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {
		try {

			String offerName = testDataJson.getString("MSS_OFFER_NAME");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_CE.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.clickMSSAutomationOfferExpandableButton(programId));

			String offerType = testDataJson.getString("MSS_SIM_ONLY_FINANCE_OFFER");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_CE.selectSpecificOfferForActivation(programId, offerType));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.selectSpecificOfferForActivation(programId, offerType));
			boolean offerRadioBtnChck = MSSPartnersPage_CE.offerRadioBtnChck(offerName,offerType).isSelected();
			if (!offerRadioBtnChck) {
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectSpecificOfferForActivation(programId, offerType));

			}
			

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select specific offer" + e);
		}
	}

	public static void declinePerkOfferIdIfDisplayed(MSSPartnersPage_CE MSSPartnersPage_CE) {
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.declinePerkOfferId, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.declinePerkOfferId);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.declinePerkOfferId);

			Validate.takeStepScreenShot("Form Details6", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.submitPerkOffer);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitPerkOffer);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Optional Perks Displayed" + e);
		}
	}

	public static void selectDefaultPromotions(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {
		try {
			String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_CE.searchForSpecificPromotionOffer(programName));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.searchForSpecificPromotionOffer(programName));
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.searchForSpecificPromotionOffer(programName));
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Default Promotions Not Displayed" + e);
		}
	}

	public static void selectNewRatePlan(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.newRatePlanDropDown, 60);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.newRatePlanDropDown);
			BaseSteps.Waits.waitGeneric(2000);

			try {
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.newRatePlanDropDown);
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.newRatePlanDropDown,
						testDataJson.getString("NEW_RATE_PLAN"));
			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Required rate plan needs to be selected by going through the list");
				/*
				 * BaseSteps.Dropdowns.selectByGoingThroughList(MSSPartnersPage_CE.
				 * newRatePlanDropDown, testDataJson.getString("NEW_RATE_PLAN"));
				 */
				selectByGoingThroughList(MSSPartnersPage_CE.newRatePlanDropDown,
						testDataJson.getString("NEW_RATE_PLAN"));
				BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToAddOnBtn);
			}

		} catch (Exception e) {

			Reporting.logReporter(Status.INFO, "Unable to select New Rate Plan" + e);
		}
	}

	public static void handleEasyRoamCheckBox(MSSPartnersPage_CE MSSPartnersPage_CE) {
		try {
			clickEasyRoamCheckBoxIfDisplayed();
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.finalContinueBtn, 60);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Easy Roam Checkbox not displayed" + e);

		}
	}

	public static void checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_CE MSSPartnersPage_CE) {

		try {
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueButtonBeforeMovingToAddOn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueButtonBeforeMovingToAddOn);
			Reporting.logReporter(Status.INFO, "Warning pop-up for selected offer not Valid is displayed");
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Warning pop-up for selected offer not Valid is Not displayed" + e);

		}
	}

	public static void scrollAndClickButton(WebElement e) {
		BaseSteps.Debugs.scrollToElement(e);
		BaseSteps.Clicks.clickElement(e);
	}

	/**
	 * 
	 */
	public static void performPreToPostFromMSS(JSONObject testDataJson, List<String> dynamicData, String SubBrand,
			String prepaidSub, String actualFirstName, String actualLastName, String actualPostalCode) {

		try {
			MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();
			LoginPageSteps.appLogin_MSS_PreToPost();
			selectDefaultLocation();
			navigateToPartnersPage(SubBrand);

			verifyMSSPartnersContentHomePageIsDisplayed();
			navigateToModifyExistingAccountPage(MSSPartnersPage_CE);

			String firstName = testDataJson.getString("ACTUAL_FIRST_NAME");
			String lastName = testDataJson.getString("ACTUAL_LAST_NAME");

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.firstName_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.firstName_p2p, actualFirstName);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.lastName_p2p, actualLastName);

			try {
				BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.phoneNumberRadioBtn_p2p);
			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Page Loaded successfully");
			}

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.prepaidNum_p2p, prepaidSub);

			String param = testDataJson.getString("IDENTIFICATION_PARAM");
			String value = testDataJson.getString("IDENTIFICATION_VALUE");

			provideIdentificationDetails(MSSPartnersPage_CE, param, actualPostalCode);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.idVeirifedCheckBox_p2p);

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.submitBtn_p2p);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.submitBtn_p2p);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitBtn_p2p);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.linkToUpgradeSubscriber, 20);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.linkToUpgradeSubscriber);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.linkToUpgradeSubscriber);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.bibDeviceLabel, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.notADefectiveDeviceRadioBtn);
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.notADefectiveDeviceRadioBtn);

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.enterValidSIMNumber);

			/*
			 * 
			 * 
			 * 
			 */
			String deviceName = testDataJson.getString("DEVICE_NAME");
			boolean imeiFlag = testDataJson.getBoolean("OFFER_TYPE_BOTH_SIM_IMEI_FLAG");
			String sim = dynamicData.get(0);

			selectDeviceFromOffersPage(deviceName, MSSPartnersPage_CE);

			/*
			 * String imei = testDataJson.getString("VALID_TELUS_IMEI_NUMBER"); if
			 * (imeiFlag) { selectIMEIFromOffersPage(imei, MSSPartnersPage_CE); }
			 * 
			 * selectSIMNumberFromOffersPage(sim, MSSPartnersPage_CE);
			 * 
			 * reSelectSIMAndIMEIWithDevice(deviceName, sim, imei, MSSPartnersPage_CE);
			 * 
			 * 
			 * // imei = 900949478465454 // sim = 8912230000415345792
			 * 
			 * 
			 * 
			 * BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.
			 * continueToOffersPage);
			 * BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToOffersPage);
			 * BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToOffersPage);
			 * 
			 * selectRequiredOffer_NEW(testDataJson, MSSPartnersPage_CE);
			 * 
			 * 
			 */

			// =======>

			Validate.takeStepFullScreenShot("Form Details5", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToOffersPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToOffersPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToOffersPage);

			BaseSteps.Waits.waitGeneric(2000);
			selectRequiredOffer_NEW(testDataJson, MSSPartnersPage_CE);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToPromotionsPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToPromotionsPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToPromotionsPage);

			declinePerkOfferIdIfDisplayed(MSSPartnersPage_CE);

			BaseSteps.Waits.waitGeneric(2000);
			selectSpecificPromotionByName(testDataJson, MSSPartnersPage_CE);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.continueToPricing, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToPricing);

			Validate.takeStepFullScreenShot("Form Details7", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueAfterPricing);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueAfterPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueAfterPricing);

			/**
			 * Need to add step to click on continue button
			 * 
			 * wait for migration page to be displayed
			 * 
			 * Select consumer regular
			 * 
			 * Click on continue to customer information button
			 * 
			 * Select PhotId from drop down
			 */

			initiateMigartionDetailsSubmission(testDataJson, MSSPartnersPage_CE);

			enterCustomerInformation(testDataJson, dynamicData, MSSPartnersPage_CE);

			// String SINNum = testDataJson.getString("SIN_NUMBER") +
			// GenericUtils.generateRandomInteger(9999);

			// enterSINNumber(MSSPartnersPage_CE, SINNum);

			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.consentCheckBox);

			Validate.takeStepFullScreenShot("Form Details2", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToPreferenceBtn);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToPreferenceBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToPreferenceBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.enterPIN);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.enterPIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterPIN, testDataJson.getString("PIN"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.enterEmailId);
			String emailId = GenericUtils.getRandomEmailId();

			// String emailId = "AUTO123@Telus.com";

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterEmailId, emailId);

			Validate.takeStepFullScreenShot("Form Details3", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToSubscriberInfoBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToSubscriberInfoBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.sameAsCustomerInfoCheckBox);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.sameAsCustomerInfoCheckBox);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.submitButtonAferSubInforFilled);
			Validate.takeStepFullScreenShot("Entered Subscriber Information", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitButtonAferSubInforFilled);

			// BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.newRatePlanDropDown,
			// 60);

			selectNewRatePlan(testDataJson, MSSPartnersPage_CE);

			BaseSteps.Waits.waitGeneric(5000);
			Validate.takeStepFullScreenShot("Form Details8", Status.INFO);

			checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_CE);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.continueToAddOnBtn, 120);
			BaseSteps.Waits.waitGeneric(5000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToAddOnBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToAddOnBtn);

			Validate.takeStepFullScreenShot("Form Details9", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.continueToFinalPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToFinalPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToFinalPricing);

			Validate.takeStepFullScreenShot("Form Details10", Status.INFO);

			handleEasyRoamCheckBox(MSSPartnersPage_CE);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.finalContinueBtn);

			try {
				BaseSteps.Waits.waitGeneric(10000);
				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.doNotWantExtendedOfferRadioBtn, 60);
				executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.doNotWantExtendedOfferRadioBtn);

			}

			catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

			}

			Validate.takeStepFullScreenShot("Price Plans and Add On Details", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.finalContinueBtn);

			Validate.takeStepFullScreenShot("Proceed to check out details", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.proceedToCheckoutBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.proceedToCheckoutBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.proceedToCheckoutBtn);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.orderConfirmationPage, 90);

			try {

				executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.doNotWantExtendedOfferRadioBtn);

				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.proceedToCheckoutBtn, 60);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.proceedToCheckoutBtn);
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.proceedToCheckoutBtn);

				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.saveAndShareBtn, 90);
				Validate.takeStepFullScreenShot("Checkout Page Details", Status.INFO);

				executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.mailServiceAgreementCopyRadioBtn);
				executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.finalAgreeCheckBox);
				executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.finalCreditCheckBox);

				Validate.takeStepFullScreenShot("MSS Detais - Before Final Submit", Status.INFO);

			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Unable to submit the details" + e);
			}

			/*
			 * Need to check if authorized user check box is already checked or not
			 * 
			 */
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.finalSubmitButton);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.finalSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.finalSubmitButton);

			/*
			 * Final Verification Page
			 */

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.tnToBeActivated, 120);
			activatedTN = MSSPartnersPage_CE.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage_CE.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.activatedAccountNumber);
			activatedBAN = MSSPartnersPage_CE.getActivatedAccountNumberText();
			Reporting.logReporter(Status.INFO, "BAN to be activated: " + activatedBAN);

			Validate.takeStepFullScreenShot("MSS Activation Page", Status.INFO);

			Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);

		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to perform PRE to POST Migration from MSS Portal");
		}

	}

	public static void selectDefaultLocation() {
		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.selectLocationOutlet);

		// selecting default value at index 1
		BaseSteps.Dropdowns.selectByIndex(MSSPartnersPage_CE.selectLocationOutlet, 1);
		BaseSteps.Waits.waitGeneric(500);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.submitButtonSetLocationPage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitButtonSetLocationPage);

	}

	public static void navigateToPartnersPage(String carrier) {
		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.selectSpecificCarrierType(carrier));
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectSpecificCarrierType(carrier));

	}

	/**
	 * This method is used to do verify home page is displayed after successful
	 * login into smart desktop application.
	 * 
	 * @param none.
	 * @return nothing.
	 */
	public static void verifyMSSPartnersContentHomePageIsDisplayed() {
		Reporting.logReporter(Status.INFO, "STEP ===> MSS homepage is displayed after successful login ");

		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();
		boolean actual = MSSPartnersPage_CE.isPartnersContentHomePageDisplayed();
		System.out.println("===========> MSS Partner Content");
		Validate.assertEquals(actual, true, "Unable to navigate to MSS Partners Content Page", false);
	}

	public static void navigateToModifyExistingAccountPage(MSSPartnersPage_CE MSSPartnersPage_CE) {
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.modifyExistingAccount, 30);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.modifyExistingAccount);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.modifyExistingAccount);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.modifyExistingAccount);
		BaseSteps.Windows.switchToNewWindow();

		BaseSteps.JavaScripts.handleInvalidCertificateError();
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.partnersHomePage);

	}

	public static void provideIdentificationDetails(MSSPartnersPage_CE MSSPartnersPage_CE, String param, String value) {

		param = param.toUpperCase();
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

		switch (param) {

		case "POSTALCODE":
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.postalCoderadioBtn_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.postalCodeTextBox_p2p, value);
			break;

		case "LASTSIMDIGITS":
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.last6SimDigits_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.last6SimDigits_p2p, value);
			break;

		default:
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.pin_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.pin_p2p, value);

		}
	}

	public static void selectRequiredOffer_NEW(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {
		MSSPartnersPage_CE = new MSSPartnersPage_CE ();
		try {

			String offerName = testDataJson.getString("MSS_OFFER_NAME");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			String offerType = testDataJson.getString("MSS_SIM_ONLY_FINANCE_OFFER");

			try {

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage_CE.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.selectSpecificOfferForActivation_NEW(offerType));

				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectSpecificOfferForActivation_NEW(offerType));
			} catch (Exception e) {
				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.expandSpecificConsumerOffer(programId));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.expandSpecificConsumerOffer(programId));
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.expandSpecificConsumerOffer(programId));

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage_CE.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectSpecificOfferForActivation_NEW(offerType));
			}

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select specific offer" + e);
		}
	}

	public static void initiateMigartionDetailsSubmission(JSONObject testDataJson,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		/**
		 * Need to add step to click on continue button
		 * 
		 * wait for migration page to be displayed
		 * 
		 * Select consumer regular
		 * 
		 * Click on continue to customer information button
		 * 
		 * Select PhotId from drop down
		 */

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.migrationPageText, 60);
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

		try {
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.consumerRegularRadioBtn_mpage);
		} catch (Exception e) {
			executor.executeScript("arguments[0].click();", MSSPartnersPage_CE.consumerRegularRadioBtn_mpage);
			Reporting.logReporter(Status.INFO, "Consumer Regular option already Selected");
		}
		Validate.takeStepFullScreenShot("Form Details1", Status.INFO);

		// click continue to customer information button - continueToSutomerInfo_mpage

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToSutomerInfo_mpage);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToSutomerInfo_mpage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToSutomerInfo_mpage);

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.photoIdDrpDwn, 60);
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.photoIdDrpDwn, "BC Services Card");

	}

	public static void selectSpecificPromotionByName(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {
		try {
			// String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			String programName = "ESS Reward Bill Credit EN";

			BaseSteps.Clicks
					.clickElement(MSSPartnersPage_CE.searchForSpecificPromotionOfferByName_Migration(programName));
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select" + e);
		}
	}

	public static void selectDeviceFromOffersPage(String deviceName, MSSPartnersPage_CE MSSPartnersPage_CE) {
		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.selectADeviceBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectADeviceBtn);
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.searchDeviceTextBox);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.searchDeviceTextBox, Keys.chord(deviceName));
		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.selectDeviceAfterSearch);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectDeviceAfterSearch);
		BaseSteps.Waits.waitGeneric(3000);
	}

	public static void selectIMEIFromOffersPage(String imei, MSSPartnersPage_CE MSSPartnersPage_CE) {
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.imeiInputBox);
		BaseSteps.SendKeys.clearFieldAndSendKeys(MSSPartnersPage_CE.imeiInputBox, imei);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.imeiInputBox, Keys.TAB);
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.validIMEITickMark, 30);
	}

	public static void selectSIMNumberFromOffersPage(String sim, MSSPartnersPage_CE MSSPartnersPage_CE) {
		BaseSteps.SendKeys.clearFieldAndSendKeysUsingJS(MSSPartnersPage_CE.enterValidSIMNumber, sim);
		BaseSteps.Waits.waitGeneric(1000);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterValidSIMNumber, Keys.TAB);
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.validSimTickMark, 30);
	}

	public static void reSelectSIMAndIMEIWithDevice(String deviceName, String sim, String imei,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		selectIMEIFromOffersPage(imei, MSSPartnersPage_CE);

		selectSIMNumberFromOffersPage(sim, MSSPartnersPage_CE);

		selectDeviceFromOffersPage(deviceName, MSSPartnersPage_CE);

		selectIMEIFromOffersPage(imei, MSSPartnersPage_CE);
	}

	public static void submitDealerInfo(JSONObject testDataJson) {

		MSSPartnersPage_CE MSSPartnersPage_CE = new MSSPartnersPage_CE();

		try {
			// Dealers Page
			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.ChannelPartner_Province);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.ChannelPartner_Province);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.ChannelPartner_Province,
					testDataJson.getString("LOCATION_PROVINCE"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.ChannelPartner_Locationdropbutton);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.ChannelPartner_Locationdropbutton,
					testDataJson.getString("LOCATION_DROP"));

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.Telus_button);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.Telus_button);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.Telus_button);

			BaseSteps.Waits.waitGeneric(500);
			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.ChannelPartner_Submitbutton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.ChannelPartner_Submitbutton);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to Submit Dealer details" + e);
		}

	}

	public static void selectCorporateCustomerLink(MSSPartnersPage_CE MSSPartnersPage_CE) {
		boolean actual = false;
		try {
			BaseSteps.Waits.waitUntilPageLoadComplete();

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_CE.activateNewCorporateCustomerLink);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.activateNewCorporateCustomerLink);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.activateNewCorporateCustomerLink);
			actual = true;
		} catch (Exception e) {

			e.printStackTrace();
			Reporting.logReporter(Status.INFO, "Unable to Select CorporateCustomerLink " + e);
		}

		Validate.assertEquals(actual, true, "Unable to Select CorporateCustomerLink", false);
	}

	public static void selectRCID(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {
		boolean actual = false;

		try {
			BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();
			BaseSteps.JavaScripts.handleInvalidCertificateError();

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.identifier_RCID);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.identifier_RCID);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.identifier_RCID, testDataJson.getString("identifier_RCID"));

			Validate.takeStepFullScreenShot("Form Details0", Status.INFO);
			BaseSteps.Waits.waitGeneric(500);

			JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.checkbox_RCID);

			BaseSteps.Waits.waitGeneric(500);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.searchbutton_RCID);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.searchbutton_RCID);

			BaseSteps.Waits.waitGeneric(500);

			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.radiobutton_RCID);

			BaseSteps.Waits.waitGeneric(500);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.corporateHierarchyContinue_RCID);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.corporateHierarchyContinue_RCID);
			actual = true;
		} catch (Exception e) {

			e.printStackTrace();
			Reporting.logReporter(Status.INFO, "Unable to Select RCID " + e);
		}

		Validate.assertEquals(actual, true, "Unable to Select RCID", false);
	}

	public static void selectBanType(MSSPartnersPage_CE MSSPartnersPage_CE) {
		boolean actual = false;

		try {
			BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();

			JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.SelectCorporate_Employee);
			
			Validate.takeStepFullScreenShot("Form Details1", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.Submitbutton_Accounttype);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.Submitbutton_Accounttype);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Select BAN type", false);
	}

	public static void enterCustDetails(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		boolean actual = false;
		try {
			enterCustomerInformation(testDataJson, dynamicData, MSSPartnersPage_CE);

			// String SINNum = testDataJson.getString("SIN_NUMBER") +
			// GenericUtils.generateRandomInteger(9999);
			// enterSINNumber(MSSPartnersPage_CE, SINNum);
			enterSINNumber(testDataJson, MSSPartnersPage_CE);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.consentCheckBox, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.consentCheckBox);
			JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.consentCheckBox);

			Validate.takeStepFullScreenShot("Form Details2", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToPreferenceBtn);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToPreferenceBtn);
			Validate.takeStepScreenShot("Continue to Preference");
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToPreferenceBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.enterPIN);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.enterPIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterPIN, testDataJson.getString("PIN"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.enterEmailId);
			String emailId = GenericUtils.getRandomEmailId();

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterEmailId, emailId);

			Validate.takeStepFullScreenShot("Form Details3", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToSubscriberInfoBtn);
			Validate.takeStepScreenShot("Continue to Subscriber Information");
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToSubscriberInfoBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.sameAsCustomerInfoCheckBox);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.sameAsCustomerInfoCheckBox);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.submitButtonAferSubInforFilled);
			Validate.takeStepFullScreenShot("Entered Subscriber Information", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitButtonAferSubInforFilled);

			
			handleCreateNewAccountPopup(MSSPartnersPage_CE);
			handleConfirmAddressdialog(MSSPartnersPage_CE);
			
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to setUp", false);
	}

	public static void selectPhoneNumber(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		boolean actual = false;
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.provinceLabel, 90);

			handleAutofillProvince(dynamicData, testDataJson);

			BaseSteps.Waits.waitGeneric(5000);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.transfer_choosephonenumber);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.transfer_choosephonenumber, "Transfers Allowed");

			boolean stat = true;
			if (stat) {

				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continuetoselectphonenumberRadioBtn);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continuetoselectphonenumberRadioBtn);
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continuetoselectphonenumberRadioBtn);

				BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_CE.selectAreaCodeWithThreeDigits);
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.selectAreaCodeWithThreeDigits,
						dynamicData.get(1));

				BaseSteps.Waits.waitGeneric(5000);
				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.submitBtnOnPhoneNumSelectionPage);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.submitBtnOnPhoneNumSelectionPage);
				BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitBtnOnPhoneNumSelectionPage);

				Validate.takeStepFullScreenShot("Phone number is selected", Status.INFO);
				try {
					BaseSteps.Waits.waitGeneric(200);
					boolean phoneNumberTakenError = MSSPartnersPage_CE.phoneNumberTakenError();
					while (phoneNumberTakenError) {
						BaseSteps.Waits.waitGeneric(200);
						BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.selectNextNumber);
						BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.selectNextNumber);
						BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.submitBtnOnPhoneNumSelectionPage);
						BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.submitBtnOnPhoneNumSelectionPage);
						phoneNumberTakenError = MSSPartnersPage_CE.phoneNumberTakenError();
					}

				} catch (Exception e) {

				}
				actual = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to select Phone Number", false);
	}

	public static void OffersPromo(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_CE MSSPartnersPage_CE) {

		BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();
		boolean actual = false;
		try {
			try {
				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.BIB_Text, 60);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.BIB_deviceradioBtn);
				JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
				executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.BIB_deviceradioBtn);
			} catch (Exception e) {

				Reporting.logReporter(Status.INFO, "No BIB button is available for this ENV" + e);
			}

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.simOnlyActivationChkBox);
			JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.simOnlyActivationChkBox);

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_CE.enterValidSIMNumber, dynamicData.get(0));

			BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();

			BaseSteps.Waits.waitGeneric(10000);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.deviceTypeDropDownOfferType);

			BaseSteps.Waits.waitGeneric(5000);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.deviceTypeDropDownOfferType);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_CE.deviceTypeDropDownOfferType,
					testDataJson.getString("DEVICE_TYPE_OFFER_PAGE"));

			BaseSteps.Waits.waitGeneric(3000);

			Validate.takeStepFullScreenShot("Form Details4", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToOffersPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToOffersPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToOffersPage);

			selectRequiredOffer(testDataJson, MSSPartnersPage_CE);

			Validate.takeStepFullScreenShot("Form Details5", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueToPromotionsPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToPromotionsPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToPromotionsPage);

			declinePerkOfferIdIfDisplayed(MSSPartnersPage_CE);

			selectDefaultPromotions(testDataJson, MSSPartnersPage_CE);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.continueToPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToPricing);

			Validate.takeStepFullScreenShot("Form Details7", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.continueAfterPricing);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueAfterPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueAfterPricing);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to select Offer", false);
	}

	public static void PricePlanAddOn(JSONObject testDataJson, MSSPartnersPage_CE MSSPartnersPage_CE) {

		BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();
		boolean actual = false;

		try {
			selectNewRatePlan(testDataJson, MSSPartnersPage_CE);

			BaseSteps.Waits.waitGeneric(10000);
			Validate.takeStepFullScreenShot("Form Details8", Status.INFO);

			checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_CE);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.continueToAddOnBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToAddOnBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToAddOnBtn);

			Validate.takeStepFullScreenShot("Form Details9", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.continueToFinalPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.continueToFinalPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.continueToFinalPricing);

			Validate.takeStepFullScreenShot("Form Details10", Status.INFO);

			handleEasyRoamCheckBox(MSSPartnersPage_CE);

			Validate.takeStepFullScreenShot("Price Plans and Add On Details", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.finalContinueBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.finalContinueBtn);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Validate.assertEquals(actual, true, "Unable to select PricePlanAddOn", false);
	}

	public static void OrderConfirmation(MSSPartnersPage_CE MSSPartnersPage_CE) {

		boolean actual = false;
		try {

			try {
				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.orderConfirmationPage, 60);
				BaseSteps.Waits.waitGeneric(10000);
				JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
				executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.doNotWantExtendedOfferRadioBtn);

			}

			catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

			}

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.proceedToCheckoutBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.proceedToCheckoutBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.proceedToCheckoutBtn);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to select OrderConfirmation", false);
	}

	public static void Checkout(MSSPartnersPage_CE MSSPartnersPage_CE) {

		boolean actual = false;
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.saveAndShareBtn, 60);
			Validate.takeStepFullScreenShot("Checkout Page Details", Status.INFO);

			JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.mailServiceAgreementCopyRadioBtn);
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.finalAgreeCheckBox);
			executor1.executeScript("arguments[0].click();", MSSPartnersPage_CE.finalCreditCheckBox);

			Validate.takeStepFullScreenShot("MSS Detais - Before Final Submit", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_CE.finalSubmitButton);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.finalSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_CE.finalSubmitButton);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Checkout", false);
	}

	public static void finalVerification(MSSPartnersPage_CE MSSPartnersPage_CE) {

		boolean actual = false;
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.tnToBeActivated, 90);
			activatedTN = MSSPartnersPage_CE.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage_CE.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_CE.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_CE.activatedAccountNumber);
			activatedBAN = MSSPartnersPage_CE.getActivatedAccountNumberText();
			Reporting.logReporter(Status.INFO, "BAN to be activated: " + activatedBAN);

			Validate.takeStepFullScreenShot("MSS Activation Page", Status.INFO);
			Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to do final Verification", false);
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
