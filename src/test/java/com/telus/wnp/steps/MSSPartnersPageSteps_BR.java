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
import com.telus.wnp.pages.MSSPartnersPage_BR;
import com.telus.wnp.pages.MSSPartnersPage;
import com.telus.wnp.pages.MSSPartnersPage;
import com.telus.wnp.pages.MSSPartnersPage;
import com.telus.wnp.utils.GenericUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSession;

public class MSSPartnersPageSteps_BR extends BaseSteps {

	public static String activatedBAN = "Undefined";
	public static String activatedTN = "Undefined";
	public static String HomeTab = "Undefined";
	public static String newWindow = "Undefined";

	/**
	 * This method is used to do verify home page is displayed after successful
	 * login into smart desktop application.
	 * 
	 * @param none.
	 * @return nothing.
	 */
	public static void verifyMSSHomePageIsDisplayed() {
		Reporting.logReporter(Status.INFO, "STEP ===> MSS homepage is displayed after successful login ");

		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
		boolean actual = MSSPartnersPage.isHomePageDisplayed();
		System.out.println("===========> MSS");
		Validate.assertEquals(actual, true, "Unable to login into MSS.", false);
	}

	public static void performTelusBRLogin(JSONObject testDataJson) {
		boolean firstPage = false;
		try {

			BaseSteps.Windows.closeUniqueWindow(newWindow);
			WebDriverSession.getWebDriverSession().switchTo().window(HomeTab);
			firstPage = WebDriverSession.getWebDriverForCurrentThreat().getTitle().contains("Channel Portal") ? true
					: false;

		} catch (Exception e) {
		}
		if (!firstPage) {
			LoginPageSteps.appLogin_TelusCorp_MSS();
			BaseSteps.Waits.waitUntilPageLoadCompleteLongWait();
			verifyMSSHomePageIsDisplayed();
			submitDealerInfo(testDataJson);

		}
	}

	public static String performTelusBRActivationFromMSS(JSONObject staticDataJson, List<String> activeData,
			JSONObject userAccess) {

		Reporting.logReporter(Status.INFO, "STEP ===> Perform Activation from MSS ");
		String Output = "TelusBR-" + activeData.get(0);
		performTelusBRLogin(staticDataJson);
		HomeTab = WebDriverSession.getWebDriverSession().getWindowHandle();

		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();

		try {

			selectNewCustomerLink(MSSPartnersPage);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			selectMobility();

			BaseSteps.Waits.waitUntilPageLoadComplete();

			selectBanType(MSSPartnersPage);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			enterCustDetails(staticDataJson, activeData, MSSPartnersPage);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			selectPhoneNumber(staticDataJson, activeData, MSSPartnersPage);

			OffersPromo(staticDataJson, activeData, MSSPartnersPage);

			BaseSteps.Waits.waitUntilPageLoadComplete();

			PricePlanAddOn(staticDataJson, MSSPartnersPage);

			// BaseSteps.Waits.waitUntilPageLoadComplete();
			//
			// OrderConfirmation(MSSPartnersPage);
			//
			// BaseSteps.Waits.waitUntilPageLoadComplete();
			//
			// Checkout(MSSPartnersPage);
			//
			// BaseSteps.Waits.waitUntilPageLoadComplete();

			/*
			 * Final Verification Page
			 * 
			 */

			finalVerification(MSSPartnersPage);

		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to activate subscriber from MSS Portal");

		} finally {
			return Output + "-" + activatedBAN + "-" + activatedTN;
		}

	}

	public static void selectBanType(MSSPartnersPage_BR MSSPartnersPage) {
		boolean actual = false;

		try {
			JavascriptExecutor executor1 = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor1.executeScript("arguments[0].click();", MSSPartnersPage.SelectBussiness_Regular);

			Validate.takeStepFullScreenShot("Form Details1", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.Submitbutton_Accounttype);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.Submitbutton_Accounttype);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Select BAN type", false);
	}

	public static void selectMobility() {

		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
		try {

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.mobility_button);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.mobility_button);

		} catch (Exception e) {
			BaseSteps.Windows.switchToNewWindow();
			newWindow = WebDriverSession.getWebDriverSession().getWindowHandle();
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.mobility_button);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.mobility_button);

			Reporting.logReporter(Status.INFO, "Need to change window to Select Mobility ");
		}

	}

	public static void submitDealerInfo(JSONObject testDataJson) {

		MSSPartnersPage MSSPartnersPage = new MSSPartnersPage();

		try {
			// Dealers Page
			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.ChannelPartner_Province);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.ChannelPartner_Province);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.ChannelPartner_Province,
					testDataJson.getString("LOCATION_PROVINCE"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.ChannelPartner_Locationdropbutton);
			BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.ChannelPartner_Locationdropbutton,
					testDataJson.getString("LOCATION_DROP"));

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.Telus_button);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.Telus_button);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.Telus_button);

			BaseSteps.Waits.waitGeneric(500);
			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.ChannelPartner_Submitbutton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.ChannelPartner_Submitbutton);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to Submit Dealer details" + e);
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

	public static void enterCustomerInformation(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_BR MSSPartnersPage) {

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.photoIdDrpDwn, 60);
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.photoIdDrpDwn, "BC Services Card");
		// BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.firstName);
		BaseSteps.Waits.waitUntilPageLoadComplete();
		String firstName = GenericUtils.generateRandomName(testDataJson.getString("FIRST_NAME"));
		String lastName = GenericUtils.generateRandomName(testDataJson.getString("LAST_NAME"));
		String legalbusinessName = GenericUtils.generateRandomName(testDataJson.getString("LEGAL_BUSINESS_NAME"));
		String tradeName = GenericUtils.generateRandomName(testDataJson.getString("TRADE_NAME"));

		String businessPhoneNumber = testDataJson.getString("BUSINESS_PHONE_NUMBER")
				+ GenericUtils.generateRandomInteger(9999);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.firstName, firstName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.lastName, lastName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.legalBusinessName, legalbusinessName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.tradeName, tradeName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.businessPhoneNumber(), businessPhoneNumber);

		/**
		 * String homePhoneNumber = testDataJson.getString("HOME_PHONE_NUMBER") +
		 * GenericUtils.generateRandomInteger(9999);
		 * BaseSteps.SendKeys.sendKey(MSSPartnersPage.firstName, firstName);
		 * BaseSteps.SendKeys.sendKey(MSSPartnersPage.lastName, lastName);
		 * BaseSteps.SendKeys.sendKey(MSSPartnersPage.homePhoneNumber, homePhoneNumber);
		 **/

		/*
		 * BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.birthMonthDrpDwn,
		 * testDataJson.getString("BIRTH_MONTH"));
		 * BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.birthDateDrpDwn,
		 * testDataJson.getString("BIRTH_DATE"));
		 * BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.birthYearDrpDwn,
		 * testDataJson.getString("BIRTH_YEAR"));
		 */
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage.detailedAddress);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.detailedAddress);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.streetNumber, testDataJson.getString("STREET_NUMBER"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.streetName, testDataJson.getString("STREET_NAME"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage.customerCity, dynamicData.get(3));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.provinceDrpDwn, dynamicData.get(2));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.postalCode, testDataJson.getString("POSTAL_CODE"));

		Validate.takeStepFullScreenShot("Customer Information", Status.INFO);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage.creditCheckBtn);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.creditCheckBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.creditCheckBtn);
	}

	public static void enterExternalPortinNumberDetails(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {

		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
		executor.executeScript("arguments[0].click();", MSSPartnersPage.selectPortPhoneNumberRadioBtn);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterNumberToBePorted,
				testDataJson.getString("NUMBER_TO_BE_PORTED"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterNumberToBePorted, Keys.TAB);

		Validate.takeStepFullScreenShot("Form Details11", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToEnterPortDetailsBtn);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToEnterPortDetailsBtn);
		Validate.takeStepScreenShot("Continue to Enter Port Details");
		BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToEnterPortDetailsBtn);

		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.contactPhoneNumber);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage.contactPhoneNumber,
				String.valueOf(GenericUtils.generateRandomMobileNumber()));

		executor.executeScript("arguments[0].click();", MSSPartnersPage.authorizePortChkBox);
		executor.executeScript("arguments[0].click();", MSSPartnersPage.confirmNumberInServiceChkBox);

		BaseSteps.Clicks.clickElement(MSSPartnersPage.enterAccountNumberLink);
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.enterAccountNumber);
		String accountNum = testDataJson.getString("CONTACT_ACCOUNT_NUMBER") + GenericUtils.generateRandomInteger(9999);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterAccountNumber, accountNum);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.reEnterAccountNumber);
		BaseSteps.Waits.waitGeneric(200);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.reEnterAccountNumber, accountNum);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.confirmAccountNumberSubmitBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.confirmAccountNumberSubmitBtn);

		Validate.takeStepFullScreenShot("Form Details12", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.portDetailsSubmitBtn);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage.portDetailsSubmitBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.portDetailsSubmitBtn);

	}

	public static void performPREToPOSTMigrationFromMSS(JSONObject testDataJson, String subscriberType) {

		// login into application
		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
		BaseSteps.Waits.waitForElementInvisibilityLongWait(MSSPartnersPage.modifyExistingAccount);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.modifyExistingAccount);

		BaseSteps.Windows.switchToNewWindow();
	}

	public static void clickEasyRoamCheckBoxIfDisplayed() {
		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.easyRoamLabel, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.easyRoamLabel);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage.easyRoamChkBox);

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Easy Roam Check Box Not displayed");
		}
	}

	/*
	 * public static void enterSINNumber(MSSPartnersPage_BR MSSPartnersPage, String
	 * SINNum) {
	 * BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.sinLink);
	 * BaseSteps.Debugs.scrollToElement(MSSPartnersPage.sinLink);
	 * BaseSteps.Clicks.clickElement(MSSPartnersPage.sinLink);
	 * 
	 * BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterSIN, SINNum);
	 * Validate.takeStepScreenShot("Entered SIN Number");
	 * BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.
	 * enterSINSubmitButton);
	 * BaseSteps.Clicks.clickElement(MSSPartnersPage.enterSINSubmitButton); }
	 */

	public static void handleCreateNewAccountPopup(MSSPartnersPage_BR MSSPartnersPage) {

		try {
			BaseSteps.Windows.switchToNewWindow();
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.createNewAccountBtnFromPopup);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.createNewAccountBtnFromPopup);
			Validate.takeStepScreenShot("Entered Subscriber Information");
		} catch (Exception e) {
			System.out.println(e);
			Reporting.logReporter(Status.INFO, "Duplicate Account Pop up not displayed");
		}

	}

	public static void continueBtnforCompanyCreditPopUp(MSSPartnersPage_BR MSSPartnersPage) {

		try {
			if (MSSPartnersPage.companyCreditPopUp.isDisplayed()) {
				WebDriverSession.getWebDriverSession().switchTo().activeElement();
				BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueBtnforCompanyCreditPopUp);
				BaseSteps.Clicks.clickElement(MSSPartnersPage.continueBtnforCompanyCreditPopUp);
				Validate.takeStepScreenShot("Select Company Credit");
			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Company Credit Pop up not displayed");
		}

	}

	public static void handleConfirmAddressdialog(MSSPartnersPage_BR MSSPartnersPage) {
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

	public static void selectRequiredOffer(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {
		try {

			String offerName = testDataJson.getString("MSS_OFFER_HEADER");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Clicks.clickElement(MSSPartnersPage.clickMSSAutomationOfferExpandableButton(programId));

			String offerType = testDataJson.getString("MSS_OFFER_NAME");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage.selectSpecificOfferForActivation(programId, offerType));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.selectSpecificOfferForActivation(programId, offerType));
			boolean offerRadioBtnChck = MSSPartnersPage.offerRadioBtnChck(offerName,offerType).isSelected();
			if (!offerRadioBtnChck) {
				BaseSteps.Clicks.clickElement(MSSPartnersPage.selectSpecificOfferForActivation(programId, offerType));

			}
			
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select specific offer" + e);
		}
	}

	public static void declinePerkOfferIdIfDisplayed(MSSPartnersPage_BR MSSPartnersPage) {
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.declinePerkOfferId, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.declinePerkOfferId);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.declinePerkOfferId);

			Validate.takeStepScreenShot("Form Details6", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.submitPerkOffer);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.submitPerkOffer);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Optional Perks Displayed" + e);
		}
	}

	public static void selectDefaultPromotions(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {
		try {
			String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			BaseSteps.Waits
					.waitForElementToBeClickableLongWait(MSSPartnersPage.searchForSpecificPromotionOffer(programName));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.searchForSpecificPromotionOffer(programName));
			BaseSteps.Clicks.clickElement(MSSPartnersPage.searchForSpecificPromotionOffer(programName));
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Default Promotions Not Displayed" + e);
		}
	}

	public static void selectNewRatePlan(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {
		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.newRatePlanDropDown, 60);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.newRatePlanDropDown);
			BaseSteps.Waits.waitGeneric(2000);

			try {
				BaseSteps.Clicks.clickElement(MSSPartnersPage.newRatePlanDropDown);
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.newRatePlanDropDown,
						testDataJson.getString("NEW_RATE_PLAN"));
			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Required rate plan needs to be selected by going through the list");
				/*
				 * BaseSteps.Dropdowns.selectByGoingThroughList(MSSPartnersPage.
				 * newRatePlanDropDown, testDataJson.getString("NEW_RATE_PLAN"));
				 */
				selectByGoingThroughList(MSSPartnersPage.newRatePlanDropDown, testDataJson.getString("NEW_RATE_PLAN"));
				BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToAddOnBtn);
			}
		} catch (Exception e) {

			Reporting.logReporter(Status.INFO, "Unable to select New Rate Plan" + e);

		}
	}

	public static void handleEasyRoamCheckBox(MSSPartnersPage_BR mSSPartnersPage) {
		try {
			clickEasyRoamCheckBoxIfDisplayed();
			BaseSteps.Waits.waitForElementVisibility(mSSPartnersPage.finalContinueBtn, 60);
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Easy Roam Checkbox not displayed" + e);

		}
	}

	public static void checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_BR MSSPartnersPage) {

		try {
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueButtonBeforeMovingToAddOn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueButtonBeforeMovingToAddOn);
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
			MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
			LoginPageSteps.appLogin_MSS_PreToPost();
			selectDefaultLocation();
			navigateToPartnersPage(SubBrand);

			verifyMSSPartnersContentHomePageIsDisplayed();
			navigateToModifyExistingAccountPage(MSSPartnersPage);

			String firstName = testDataJson.getString("ACTUAL_FIRST_NAME");
			String lastName = testDataJson.getString("ACTUAL_LAST_NAME");

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.firstName_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.firstName_p2p, actualFirstName);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.lastName_p2p, actualLastName);

			try {
				BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.phoneNumberRadioBtn_p2p);
			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Page Loaded successfully");
			}

			/** BaseSteps.SendKeys.sendKey(MSSPartnersPage.prepaidNum_p2p, prepaidSub); **/

			String param = testDataJson.getString("IDENTIFICATION_PARAM");
			String value = testDataJson.getString("IDENTIFICATION_VALUE");

			provideIdentificationDetails(MSSPartnersPage, param, actualPostalCode);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage.idVeirifedCheckBox_p2p);

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.submitBtn_p2p);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.submitBtn_p2p);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.submitBtn_p2p);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.linkToUpgradeSubscriber, 20);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.linkToUpgradeSubscriber);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.linkToUpgradeSubscriber);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.bibDeviceLabel, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.notADefectiveDeviceRadioBtn);
			executor.executeScript("arguments[0].click();", MSSPartnersPage.notADefectiveDeviceRadioBtn);

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.enterValidSIMNumber);

			/*
			 * 
			 * 
			 * 
			 */
			String deviceName = testDataJson.getString("DEVICE_NAME");
			boolean imeiFlag = testDataJson.getBoolean("OFFER_TYPE_BOTH_SIM_IMEI_FLAG");
			String sim = dynamicData.get(0);

			selectDeviceFromOffersPage(deviceName, MSSPartnersPage);

			/*
			 * String imei = testDataJson.getString("VALID_TELUS_IMEI_NUMBER"); if
			 * (imeiFlag) { selectIMEIFromOffersPage(imei, MSSPartnersPage); }
			 * 
			 * selectSIMNumberFromOffersPage(sim, MSSPartnersPage);
			 * 
			 * reSelectSIMAndIMEIWithDevice(deviceName, sim, imei, MSSPartnersPage);
			 * 
			 * 
			 * // imei = 900949478465454 // sim = 8912230000415345792
			 * 
			 * 
			 * 
			 * BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.
			 * continueToOffersPage);
			 * BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToOffersPage);
			 * BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToOffersPage);
			 * 
			 * selectRequiredOffer_NEW(testDataJson, MSSPartnersPage);
			 * 
			 * 
			 */

			// =======>

			Validate.takeStepFullScreenShot("Form Details5", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToOffersPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToOffersPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToOffersPage);

			/*
			 * BaseSteps.Waits.waitGeneric(2000);
			 * BaseSteps.Debugs.scrollToElement(MSSPartnersPage.ExpandAllOffers);
			 * BaseSteps.Clicks.clickElement(MSSPartnersPage.ExpandAllOffers);
			 * selectRequiredOffer_NEW(testDataJson, MSSPartnersPage);
			 */

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToPromotionsPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToPromotionsPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToPromotionsPage);

			declinePerkOfferIdIfDisplayed(MSSPartnersPage);

			BaseSteps.Waits.waitGeneric(2000);
			selectSpecificPromotionByName(testDataJson, MSSPartnersPage);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.continueToPricing, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToPricing);

			Validate.takeStepFullScreenShot("Form Details7", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueAfterPricing);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueAfterPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueAfterPricing);

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

			initiateMigartionDetailsSubmission(testDataJson, MSSPartnersPage);

			enterCustomerInformation(testDataJson, dynamicData, MSSPartnersPage);

			/*
			 * String SINNum = testDataJson.getString("SIN_NUMBER") +
			 * GenericUtils.generateRandomInteger(9999);
			 * 
			 * enterSINNumber(MSSPartnersPage, SINNum);
			 */

			BaseSteps.Clicks.clickElement(MSSPartnersPage.consentCheckBox);

			Validate.takeStepFullScreenShot("Form Details2", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToPreferenceBtn);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToPreferenceBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToPreferenceBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.enterPIN);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.enterPIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterPIN, testDataJson.getString("PIN"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.enterEmailId);
			String emailId = GenericUtils.getRandomEmailId();

			// String emailId = "AUTO123@Telus.com";

			BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterEmailId, emailId);

			Validate.takeStepFullScreenShot("Form Details3", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToSubscriberInfoBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToSubscriberInfoBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.sameAsCustomerInfoCheckBox);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.sameAsCustomerInfoCheckBox);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.submitButtonAferSubInforFilled);
			Validate.takeStepFullScreenShot("Entered Subscriber Information", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.submitButtonAferSubInforFilled);

			// BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.newRatePlanDropDown,
			// 60);

			selectNewRatePlan(testDataJson, MSSPartnersPage);

			BaseSteps.Waits.waitGeneric(5000);
			Validate.takeStepFullScreenShot("Form Details8", Status.INFO);

			checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.continueToAddOnBtn, 120);
			BaseSteps.Waits.waitGeneric(5000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToAddOnBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToAddOnBtn);

			Validate.takeStepFullScreenShot("Form Details9", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.continueToFinalPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToFinalPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToFinalPricing);

			Validate.takeStepFullScreenShot("Form Details10", Status.INFO);

			handleEasyRoamCheckBox(MSSPartnersPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.finalContinueBtn);

			try {
				BaseSteps.Waits.waitGeneric(10000);
				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.doNotWantExtendedOfferRadioBtn, 60);
				executor.executeScript("arguments[0].click();", MSSPartnersPage.doNotWantExtendedOfferRadioBtn);

			}

			catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

			}

			Validate.takeStepFullScreenShot("Price Plans and Add On Details", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.finalContinueBtn);

			Validate.takeStepFullScreenShot("Proceed to check out details", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.proceedToCheckoutBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.proceedToCheckoutBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.proceedToCheckoutBtn);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.orderConfirmationPage, 90);

			try {

				executor.executeScript("arguments[0].click();", MSSPartnersPage.doNotWantExtendedOfferRadioBtn);

				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.proceedToCheckoutBtn, 60);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.proceedToCheckoutBtn);
				BaseSteps.Clicks.clickElement(MSSPartnersPage.proceedToCheckoutBtn);

				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.saveAndShareBtn, 90);
				Validate.takeStepFullScreenShot("Checkout Page Details", Status.INFO);

				executor.executeScript("arguments[0].click();", MSSPartnersPage.mailServiceAgreementCopyRadioBtn);
				executor.executeScript("arguments[0].click();", MSSPartnersPage.finalAgreeCheckBox);
				executor.executeScript("arguments[0].click();", MSSPartnersPage.finalCreditCheckBox);

				Validate.takeStepFullScreenShot("MSS Detais - Before Final Submit", Status.INFO);

			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Unable to submit the details" + e);
			}

			/*
			 * Need to check if authorized user check box is already checked or not
			 * 
			 */
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.finalSubmitButton);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.finalSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.finalSubmitButton);

			/*
			 * Final Verification Page
			 */

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.tnToBeActivated, 120);
			activatedTN = MSSPartnersPage.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.activatedAccountNumber);
			activatedBAN = MSSPartnersPage.getActivatedAccountNumberText();
			Reporting.logReporter(Status.INFO, "BAN to be activated: " + activatedBAN);

			Validate.takeStepFullScreenShot("MSS Activation Page", Status.INFO);

			Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);

		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to perform PRE to POST Migration from MSS Portal");
		}

	}

	public static void selectDefaultLocation() {
		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.selectLocationOutlet);

		// selecting default value at index 1
		BaseSteps.Dropdowns.selectByIndex(MSSPartnersPage.selectLocationOutlet, 1);
		BaseSteps.Waits.waitGeneric(500);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.submitButtonSetLocationPage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.submitButtonSetLocationPage);

	}

	public static void navigateToPartnersPage(String carrier) {
		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.selectSpecificCarrierType(carrier));
		BaseSteps.Clicks.clickElement(MSSPartnersPage.selectSpecificCarrierType(carrier));

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

		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
		boolean actual = MSSPartnersPage.isPartnersContentHomePageDisplayed();
		System.out.println("===========> MSS Partner Content");
		Validate.assertEquals(actual, true, "Unable to navigate to MSS Partners Content Page", false);
	}

	public static void navigateToModifyExistingAccountPage(MSSPartnersPage_BR MSSPartnersPage) {
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.modifyExistingAccount, 30);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.modifyExistingAccount);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage.modifyExistingAccount);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.modifyExistingAccount);
		BaseSteps.Windows.switchToNewWindow();

		BaseSteps.JavaScripts.handleInvalidCertificateError();
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.partnersHomePage);

	}

	public static void provideIdentificationDetails(MSSPartnersPage_BR MSSPartnersPage, String param, String value) {

		param = param.toUpperCase();
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

		switch (param) {

		case "POSTALCODE":
			executor.executeScript("arguments[0].click();", MSSPartnersPage.postalCoderadioBtn_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.postalCodeTextBox_p2p, value);
			break;

		case "LASTSIMDIGITS":
			executor.executeScript("arguments[0].click();", MSSPartnersPage.last6SimDigits_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.last6SimDigits_p2p, value);
			break;

		default:
			executor.executeScript("arguments[0].click();", MSSPartnersPage.pin_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.pin_p2p, value);

		}
	}

	public static void selectRequiredOffer_NEW(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {
		try {

			String offerName = testDataJson.getString("MSS_OFFER_HEADER");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			String offerType = testDataJson.getString("MSS_OFFER_NAME");

			try {

				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage.expandSpecificConsumerOffer(programId));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.expandSpecificConsumerOffer(programId));
				BaseSteps.Clicks.clickElement(MSSPartnersPage.expandSpecificConsumerOffer(programId));

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.selectSpecificOfferForActivation_NEW(offerType));
				boolean offerRadioBtnChck = MSSPartnersPage.offerRadioBtnChck(offerName,offerType).isSelected();
				if (!offerRadioBtnChck) {
					BaseSteps.Clicks.clickElement(MSSPartnersPage.selectSpecificOfferForActivation_NEW(offerType));
				}

			} catch (Exception e) {
				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage.expandSpecificConsumerOffer(programId));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.expandSpecificConsumerOffer(programId));
				BaseSteps.Clicks.clickElement(MSSPartnersPage.expandSpecificConsumerOffer(programId));

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Clicks.clickElement(MSSPartnersPage.selectSpecificOfferForActivation_NEW(offerType));
			}

		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select specific offer" + e);
		}
	}

	public static void initiateMigartionDetailsSubmission(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {

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

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.migrationPageText, 60);
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

		try {
			executor.executeScript("arguments[0].click();", MSSPartnersPage.consumerRegularRadioBtn_mpage);
		} catch (Exception e) {
			executor.executeScript("arguments[0].click();", MSSPartnersPage.consumerRegularRadioBtn_mpage);
			Reporting.logReporter(Status.INFO, "Consumer Regular option already Selected");
		}
		Validate.takeStepFullScreenShot("Form Details1", Status.INFO);

		// click continue to customer information button - continueToSutomerInfo_mpage

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToSutomerInfo_mpage);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToSutomerInfo_mpage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToSutomerInfo_mpage);

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.photoIdDrpDwn, 60);
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.photoIdDrpDwn, "BC Services Card");

	}

	public static void selectSpecificPromotionByName(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {
		try {
			// String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			String programName = "ESS Reward Bill Credit EN";

			BaseSteps.Clicks.clickElement(MSSPartnersPage.searchForSpecificPromotionOfferByName_Migration(programName));
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select" + e);
		}
	}

	public static void selectDeviceFromOffersPage(String deviceName, MSSPartnersPage_BR MSSPartnersPage) {
		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.selectADeviceBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.selectADeviceBtn);
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.searchDeviceTextBox);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.searchDeviceTextBox, Keys.chord(deviceName));
		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.selectDeviceAfterSearch);
		BaseSteps.Clicks.clickElement(MSSPartnersPage.selectDeviceAfterSearch);
		BaseSteps.Waits.waitGeneric(3000);
	}

	public static void selectIMEIFromOffersPage(String imei, MSSPartnersPage_BR MSSPartnersPage) {
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.imeiInputBox);
		BaseSteps.SendKeys.clearFieldAndSendKeys(MSSPartnersPage.imeiInputBox, imei);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.imeiInputBox, Keys.TAB);
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.validIMEITickMark, 30);
	}

	public static void selectSIMNumberFromOffersPage(String sim, MSSPartnersPage_BR MSSPartnersPage) {
		BaseSteps.SendKeys.clearFieldAndSendKeysUsingJS(MSSPartnersPage.enterValidSIMNumber, sim);
		BaseSteps.Waits.waitGeneric(1000);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterValidSIMNumber, Keys.TAB);
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.validSimTickMark, 30);
	}

	public static void reSelectSIMAndIMEIWithDevice(String deviceName, String sim, String imei,
			MSSPartnersPage_BR MSSPartnersPage) {

		selectIMEIFromOffersPage(imei, MSSPartnersPage);

		selectSIMNumberFromOffersPage(sim, MSSPartnersPage);

		selectDeviceFromOffersPage(deviceName, MSSPartnersPage);

		selectIMEIFromOffersPage(imei, MSSPartnersPage);
	}

	public static void loginToTelusBR(MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.accountTypeBusinessRegular);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.accountTypeBusinessRegular);

			Validate.takeStepFullScreenShot("Form Details1", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.submitButton);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Select BAN type", false);
	}

	public static void selectNewCustomerLink(MSSPartnersPage_BR MSSPartnersPage) {
		boolean actual = false;
		try {
			BaseSteps.Waits.waitUntilPageLoadComplete();

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage.activateNewCustomerLink);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.activateNewCustomerLink);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.activateNewCustomerLink);
			actual = true;
		} catch (Exception e) {

			e.printStackTrace();
			Reporting.logReporter(Status.INFO, "Unable to Select CorporateCustomerLink " + e);
		}

		Validate.assertEquals(actual, true, "Unable to Select CorporateCustomerLink", false);
	}

	public static void enterCustDetails(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {

			enterCustomerInformation(testDataJson, dynamicData, MSSPartnersPage);

			BaseSteps.Clicks.clickElement(MSSPartnersPage.consentCheckBox);

			Validate.takeStepFullScreenShot("Form Details2", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToPreferenceBtn);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToPreferenceBtn);
			Validate.takeStepScreenShot("Continue to Preference");
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToPreferenceBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.enterPIN);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.enterPIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterPIN, testDataJson.getString("PIN"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.enterEmailId);
			String emailId = GenericUtils.getRandomEmailId();

			BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterEmailId, emailId);

			Validate.takeStepFullScreenShot("Form Details3", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueToSubscriberInfoBtn);
			Validate.takeStepScreenShot("Continue to Subscriber Information");
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToSubscriberInfoBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.sameAsCustomerInfoCheckBox);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.sameAsCustomerInfoCheckBox);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.submitButtonAferSubInforFilled);
			Validate.takeStepFullScreenShot("Entered Subscriber Information", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.submitButtonAferSubInforFilled);
			// BaseSteps.Waits.waitGeneric(3000);

			handleCreateNewAccountPopup(MSSPartnersPage);
			handleConfirmAddressdialog(MSSPartnersPage);
			continueBtnforCompanyCreditPopUp(MSSPartnersPage);

			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to enter customer details", false);
	}

	public static void selectPhoneNumber(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.provinceLabel, 90);

			handleAutofillProvince(dynamicData, testDataJson);

			BaseSteps.Waits.waitGeneric(5000);

			boolean stat = true;
			if (stat) {

				BaseSteps.Waits
						.waitForElementToBeClickableLongWait(MSSPartnersPage.continuetoselectphonenumberRadioBtn);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continuetoselectphonenumberRadioBtn);
				BaseSteps.Clicks.clickElement(MSSPartnersPage.continuetoselectphonenumberRadioBtn);

				BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage.selectAreaCodeWithThreeDigits);
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.selectAreaCodeWithThreeDigits,
						dynamicData.get(1));

				BaseSteps.Waits.waitGeneric(5000);
				BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.submitBtnOnPhoneNumSelectionPage);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage.submitBtnOnPhoneNumSelectionPage);
				BaseSteps.Clicks.clickElement(MSSPartnersPage.submitBtnOnPhoneNumSelectionPage);

				Validate.takeStepFullScreenShot("Phone number is selected", Status.INFO);
				try {
					BaseSteps.Waits.waitGeneric(200);
					boolean phoneNumberTakenError = MSSPartnersPage.phoneNumberTakenError();
					while (phoneNumberTakenError) {
						BaseSteps.Waits.waitGeneric(200);
						BaseSteps.Debugs.scrollToElement(MSSPartnersPage.selectNextNumber);
						BaseSteps.Clicks.clickElement(MSSPartnersPage.selectNextNumber);
						BaseSteps.Debugs.scrollToElement(MSSPartnersPage.submitBtnOnPhoneNumSelectionPage);
						BaseSteps.Clicks.clickElement(MSSPartnersPage.submitBtnOnPhoneNumSelectionPage);
						phoneNumberTakenError = MSSPartnersPage.phoneNumberTakenError();
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

	public static void handleAutofillProvince(List<String> dynamicData, JSONObject testDataJson) {
		MSSPartnersPage_BR MSSPartnersPage = new MSSPartnersPage_BR();
		String provinceCheck = MSSPartnersPage.province_choosephonenumber();
		try {
			if (!(provinceCheck.equalsIgnoreCase(testDataJson.getString("PROVINCE"))))

			{
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.province_choosephonenumber, dynamicData.get(2));
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage.city_choosephonenumber, dynamicData.get(3));

				Reporting.logReporter(Status.INFO, "Province and city selected");
			}
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "AutoFetch done");
		}
	}

	public static void OffersPromo(JSONObject testDataJson, List<String> dynamicData,
			MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.bibDeviceLabel, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.notADefectiveDeviceRadioBtn);
			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage.notADefectiveDeviceRadioBtn);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.selectADeviceBtn, 60);
			executor.executeScript("arguments[0].click();", MSSPartnersPage.simOnlyActivationChkBox);
			BaseSteps.Waits.waitGeneric(5000);

			BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterValidSIMNumber, dynamicData.get(0));
			BaseSteps.SendKeys.sendKey(MSSPartnersPage.enterValidSIMNumber, Keys.TAB);
			BaseSteps.Waits.waitGeneric(20000);
		
			BaseSteps.Clicks.clickElement(MSSPartnersPage.deviceTypeDropDownOfferTypeSmartphoneOption);
			BaseSteps.Waits.waitGeneric(5000);

			Validate.takeStepFullScreenShot("SIM,IMEI,DEVICE Information", Status.INFO);
			BaseSteps.Waits.waitGeneric(5000);

			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToOffersPage);

			BaseSteps.Waits.waitGeneric(5000);

//			BaseSteps.Waits.waitGeneric(2000);
//			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.ExpandAllOffers);
//			BaseSteps.Clicks.clickElement(MSSPartnersPage.ExpandAllOffers);
			selectRequiredOffer(testDataJson, MSSPartnersPage);

			Validate.takeStepFullScreenShot("Form Details5", Status.INFO);

			BaseSteps.Waits.waitGeneric(5000);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToPromotionsPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToPromotionsPage);

			declinePerkOfferIdIfDisplayed(MSSPartnersPage);

			// selectDefaultPromotions(testDataJson, MSSPartnersPage);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.continueToPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToPricing);

			Validate.takeStepFullScreenShot("Form Details7", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.continueAfterPricing);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueAfterPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueAfterPricing);
			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Select offer", false);
	}

	public static void PricePlanAddOn(JSONObject testDataJson, MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.newRatePlanDropDown, 60);

			selectNewRatePlan(testDataJson, MSSPartnersPage);

			BaseSteps.Waits.waitGeneric(10000);
			Validate.takeStepFullScreenShot("Form Details8", Status.INFO);

			checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.continueToAddOnBtn, 60);
			BaseSteps.Waits.waitGeneric(5000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToAddOnBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToAddOnBtn);

			Validate.takeStepFullScreenShot("Form Details9", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.continueToFinalPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.continueToFinalPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.continueToFinalPricing);

			Validate.takeStepFullScreenShot("Form Details10", Status.INFO);

			handleEasyRoamCheckBox(MSSPartnersPage);

			Validate.takeStepFullScreenShot("Price Plans and Add On Details", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.finalContinueBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.finalContinueBtn);

			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Select Price plan type", false);
	}

	public static void OrderConfirmation(MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {
			try {
				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.orderConfirmationPage, 60);
				BaseSteps.Waits.waitGeneric(10000);
				JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
				executor.executeScript("arguments[0].click();", MSSPartnersPage.doNotWantExtendedOfferRadioBtn);

			}

			catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

			}

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.proceedToCheckoutBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.proceedToCheckoutBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.proceedToCheckoutBtn);

			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to confirm order", false);
	}

	public static void Checkout(MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.saveAndShareBtn, 60);
			Validate.takeStepFullScreenShot("Checkout Page Details", Status.INFO);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage.mailServiceAgreementCopyRadioBtn);
			executor.executeScript("arguments[0].click();", MSSPartnersPage.finalAgreeCheckBox);
			executor.executeScript("arguments[0].click();", MSSPartnersPage.finalCreditCheckBox);

			Validate.takeStepFullScreenShot("MSS Detais - Before Final Submit", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage.finalSubmitButton);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.finalSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage.finalSubmitButton);

			actual = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Validate.assertEquals(actual, true, "Unable to Checkout", false);
	}

	public static void finalVerification(MSSPartnersPage_BR MSSPartnersPage) {

		boolean actual = false;

		try {
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.tnToBeActivated, 90);
			activatedTN = MSSPartnersPage.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage.activatedAccountNumber);
			activatedBAN = MSSPartnersPage.getActivatedAccountNumberText();
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
