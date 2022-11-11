package com.telus.wnp.steps;

import java.io.FileOutputStream;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;
import com.telus.wnp.pages.MSSPartnersPage_IR;
import com.telus.wnp.utils.GenericUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSession;

public class MSSPartnersPageSteps extends BaseSteps {

	public static String activatedBAN = null;
	public static String activatedTN = null;

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
		

	public static void performActivationFromMSS(JSONObject testDataJson, String subscriberType, String SIMNumber, JSONObject userAccess) {

		Reporting.logReporter(Status.INFO, "STEP ===> Perform Activation from MSS ");
		

		try {
			if (subscriberType.equalsIgnoreCase("KOODO")) {
				LoginPageSteps.appLogin_KOODO_MSS(userAccess);
			} else {
				LoginPageSteps.appLogin_TELUS_MSS(userAccess);
			}

			verifyMSSHomePageIsDisplayed();

			MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
			
			//FormDetails1 
			productType(testDataJson,MSSPartnersPage_IR);
			//FormDetails2 and FormDetails3
			setupGettingStarted(testDataJson,MSSPartnersPage_IR);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.provinceLabel, 90);

			if (testDataJson.getBoolean("PORT-IN_FLAG")) {
				enterExternalPortinNumberDetails(testDataJson, MSSPartnersPage_IR);			
			}
			else {	
				selectPhoneNumber(testDataJson,MSSPartnersPage_IR);
			}
			
			BaseSteps.Waits.waitUntilPageLoadComplete();

			OffersPromo(testDataJson,MSSPartnersPage_IR,SIMNumber);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();
			
			PricePlanAddOn(testDataJson,MSSPartnersPage_IR);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();

			OrderConfirmation(testDataJson,MSSPartnersPage_IR);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();
			
			Checkout(testDataJson,MSSPartnersPage_IR);
			
			/*
			 * Final Verification Page
			 */
			BaseSteps.Waits.waitUntilPageLoadComplete();
			
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.tnToBeActivated, 90);
			activatedTN = MSSPartnersPage_IR.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage_IR.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.activatedAccountNumber);
			activatedBAN = MSSPartnersPage_IR.getActivatedAccountNumberText();
			Reporting.logReporter(Status.INFO, "BAN to be activated: " + activatedBAN);

			Validate.takeStepFullScreenShot("MSS Activation Page", Status.INFO);
			
			Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);
			
			String FilePath = "C:\\Tools\\Results\\Activation\\data.txt"; 
			String Output = ""+SIMNumber+" "+activatedBAN+" "+activatedTN+"\n";
			ActivationDetails(FilePath,Output); 
			System.out.println(Output);
			
		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to activate subscriber from MSS Portal");
		}

	}
	
	public static void ActivationDetails(String FilePath, String Content) {
		System.out.println(Content);
		try {
			FileOutputStream FS = new FileOutputStream(FilePath, true); 
			FS.write(Content.getBytes());
			FS.close();
		}catch (Exception e) {}
	}

	public static void performActivationFromMSS(JSONObject testDataJson, String subscriberType) {

		Reporting.logReporter(Status.INFO, "STEP ===> Perform Activation from MSS ");
		

		try {
			if (subscriberType.equalsIgnoreCase("KOODO")) {
				LoginPageSteps.appLogin_KOODO_MSS();
			} else {
				LoginPageSteps.appLogin_TELUS_MSS();
			}

			verifyMSSHomePageIsDisplayed();

			MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
			
			//FormDetails1 
			productType(testDataJson,MSSPartnersPage_IR);
			//FormDetails2 and FormDetails3
			setupGettingStarted(testDataJson,MSSPartnersPage_IR);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.provinceLabel, 90);

			if (testDataJson.getBoolean("PORT-IN_FLAG")) {
				enterExternalPortinNumberDetails(testDataJson, MSSPartnersPage_IR);			
			}
			else {	
				selectPhoneNumber(testDataJson,MSSPartnersPage_IR);
			}
			
			BaseSteps.Waits.waitUntilPageLoadComplete();

			OffersPromo(testDataJson,MSSPartnersPage_IR,testDataJson.getString("VALID_TELUS_SIM_NUMBER"));
			
			BaseSteps.Waits.waitUntilPageLoadComplete();
			
			PricePlanAddOn(testDataJson,MSSPartnersPage_IR);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();

			OrderConfirmation(testDataJson,MSSPartnersPage_IR);
			
			BaseSteps.Waits.waitUntilPageLoadComplete();
			
			Checkout(testDataJson,MSSPartnersPage_IR);
			
			/*
			 * Final Verification Page
			 */
			BaseSteps.Waits.waitUntilPageLoadComplete();
			
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.tnToBeActivated, 90);
			activatedTN = MSSPartnersPage_IR.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage_IR.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.activatedAccountNumber);
			activatedBAN = MSSPartnersPage_IR.getActivatedAccountNumberText();
			Reporting.logReporter(Status.INFO, "BAN to be activated: " + activatedBAN);

			Validate.takeStepFullScreenShot("MSS Activation Page", Status.INFO);

			Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);

		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to activate subscriber from MSS Portal");
		}

	}
	public static void pageload() {
		JavascriptExecutor js = BaseSteps.JavaScripts.getJavaScriptExecutor();
		while(js.executeScript("return document.readyState").toString().equals("complete")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void enterCustomerInformation(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {

		//BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.firstName);
		BaseSteps.Waits.waitUntilPageLoadComplete();	
		String firstName = GenericUtils.generateRandomName(testDataJson.getString("FIRST_NAME"));
		String lastName = GenericUtils.generateRandomName(testDataJson.getString("LAST_NAME"));
		String homePhoneNumber = testDataJson.getString("HOME_PHONE_NUMBER")
				+ GenericUtils.generateRandomInteger(99999);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.firstName, firstName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.lastName, lastName);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.homePhoneNumber, homePhoneNumber);
		
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.birthMonthDrpDwn,testDataJson.getString("BIRTH_MONTH"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.birthDateDrpDwn, testDataJson.getString("BIRTH_DATE"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.birthYearDrpDwn, testDataJson.getString("BIRTH_YEAR"));
		
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.detailedAddress);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.detailedAddress);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.streetNumber, testDataJson.getString("STREET_NUMBER"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.streetName, testDataJson.getString("STREET_NAME"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.customerCity, testDataJson.getString("CUSTOMER_CITY"));
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.provinceDrpDwn, testDataJson.getString("PROVINCE"));
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.postalCode, testDataJson.getString("POSTAL_CODE"));
		
		
		Validate.takeStepFullScreenShot("Customer Information", Status.INFO);

		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.creditCheckBtn);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.creditCheckBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.creditCheckBtn);
	}

	public static void enterExternalPortinNumberDetails(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {

		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
		executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.selectPortPhoneNumberRadioBtn);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterNumberToBePorted,
				testDataJson.getString("NUMBER_TO_BE_PORTED"));

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterNumberToBePorted, Keys.TAB);
		Validate.takeStepFullScreenShot("Form Details11", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToEnterPortDetailsBtn);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToEnterPortDetailsBtn);
		Validate.takeStepScreenShot("Continue to Enter Port Details");
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToEnterPortDetailsBtn);

		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.contactPhoneNumber);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.contactPhoneNumber,
				String.valueOf(GenericUtils.generateRandomMobileNumber()));

		executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.authorizePortChkBox);
		executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.confirmNumberInServiceChkBox);

		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.enterAccountNumberLink);
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.enterAccountNumber);
		String accountNum = testDataJson.getString("CONTACT_ACCOUNT_NUMBER") + GenericUtils.generateRandomInteger(9999);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterAccountNumber, accountNum);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_IR.reEnterAccountNumber);
		BaseSteps.Waits.waitGeneric(200);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.reEnterAccountNumber, accountNum);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.confirmAccountNumberSubmitBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.confirmAccountNumberSubmitBtn);

		Validate.takeStepFullScreenShot("Form Details12", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.portDetailsSubmitBtn);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.portDetailsSubmitBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.portDetailsSubmitBtn);

	}

	public static void performPREToPOSTMigrationFromMSS(JSONObject testDataJson, String subscriberType) {

		// login into application
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		BaseSteps.Waits.waitForElementInvisibilityLongWait(MSSPartnersPage_IR.modifyExistingAccount);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.modifyExistingAccount);

		BaseSteps.Windows.switchToNewWindow();
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

	public static void enterSINNumber(MSSPartnersPage_IR MSSPartnersPage_IR, String SINNum) {
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.sinLink);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.sinLink);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.sinLink);

		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterSIN, SINNum);
		Validate.takeStepScreenShot("Entered SIN Number");
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.enterSINSubmitButton);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.enterSINSubmitButton);
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
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void selectRequiredOffer(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {

			String offerName = testDataJson.getString("MSS_OFFER_NAME");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			BaseSteps.Waits.waitForElementToBeClickableLongWait(
					MSSPartnersPage_IR.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.clickMSSAutomationOfferExpandableButton(programId));
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.clickMSSAutomationOfferExpandableButton(programId));

			String offerType = testDataJson.getString("MSS_SIM_ONLY_FINANCE_OFFER");
			BaseSteps.Waits
					.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.selectSpecificOfferForActivation(offerType));
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.selectSpecificOfferForActivation(offerType));
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectSpecificOfferForActivation(offerType));

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

	public static void selectDefaultPromotions(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			BaseSteps.Waits
					.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.searchForSpecificPromotionOffer(programName));
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
				BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.newRatePlanDropDown, testDataJson.getString("NEW_RATE_PLAN"));
			}
			catch(Exception e) {
				Reporting.logReporter(Status.INFO, "Required rate plan needs to be selected by going through the list");
				//BaseSteps.Dropdowns.selectByGoingThroughList(MSSPartnersPage_IR.newRatePlanDropDown,
				//		testDataJson.getString("NEW_RATE_PLAN"));
				//BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToAddOnBtn);
				try {
					WebDriver driver = WebDriverSession.getWebDriverSession();
					Select offer = new Select(driver.findElement(By.xpath("//*[@id=\"introductionNewRatePlan\"]")));
					offer.selectByVisibleText(testDataJson.getString("NEW_RATE_PLAN"));
					BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToAddOnBtn);
				}
				catch(Exception E){
					
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

		try {
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueButtonBeforeMovingToAddOn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueButtonBeforeMovingToAddOn);
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
	public static void performPreToPostFromMSS(JSONObject testDataJson, String SubBrand, String prepaidSub,
			String actualFirstName, String actualLastName, String actualPostalCode) {

		try {
			MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
			LoginPageSteps.appLogin_MSS_PreToPost();
			selectDefaultLocation();
			navigateToPartnersPage(SubBrand);

			verifyMSSPartnersContentHomePageIsDisplayed();
			navigateToModifyExistingAccountPage(MSSPartnersPage_IR);

			String firstName = testDataJson.getString("ACTUAL_FIRST_NAME");
			String lastName = testDataJson.getString("ACTUAL_LAST_NAME");

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.firstName_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.firstName_p2p, actualFirstName);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.lastName_p2p, actualLastName);

			try {
				BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.phoneNumberRadioBtn_p2p);
			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Page Loaded successfully");
			}

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.prepaidNum_p2p, prepaidSub);

			String param = testDataJson.getString("IDENTIFICATION_PARAM");
			String value = testDataJson.getString("IDENTIFICATION_VALUE");

			provideIdentificationDetails(MSSPartnersPage_IR, param, actualPostalCode);

			JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.idVeirifedCheckBox_p2p);

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_IR.submitBtn_p2p);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.submitBtn_p2p);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitBtn_p2p);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.linkToUpgradeSubscriber, 20);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.linkToUpgradeSubscriber);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.linkToUpgradeSubscriber);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.bibDeviceLabel, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.notADefectiveDeviceRadioBtn);
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.notADefectiveDeviceRadioBtn);

			BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_IR.enterValidSIMNumber);

			/*
			 * 
			 * 
			 * 
			 */
			String deviceName = testDataJson.getString("DEVICE_NAME");
			boolean imeiFlag = testDataJson.getBoolean("OFFER_TYPE_BOTH_SIM_IMEI_FLAG");
			String sim = testDataJson.getString("VALID_TELUS_SIM_NUMBER");

			selectDeviceFromOffersPage(deviceName, MSSPartnersPage_IR);

			/*String imei = testDataJson.getString("VALID_TELUS_IMEI_NUMBER");
			if (imeiFlag) {
				selectIMEIFromOffersPage(imei, MSSPartnersPage_IR);
			}

			selectSIMNumberFromOffersPage(sim, MSSPartnersPage_IR);

			reSelectSIMAndIMEIWithDevice(deviceName, sim, imei, MSSPartnersPage_IR);
			
			
			// imei = 900949478465454
			// sim = 8912230000415345792

			
			
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToOffersPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToOffersPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToOffersPage);

			selectRequiredOffer_NEW(testDataJson, MSSPartnersPage_IR);
			
			
			*/
			

			// =======>
			
			
			Validate.takeStepFullScreenShot("Form Details5", Status.INFO);
			
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToOffersPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToOffersPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToOffersPage);
			
			BaseSteps.Waits.waitGeneric(2000);
			selectRequiredOffer_NEW(testDataJson, MSSPartnersPage_IR);

			
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToPromotionsPage);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPromotionsPage);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPromotionsPage);

			declinePerkOfferIdIfDisplayed(MSSPartnersPage_IR);

			BaseSteps.Waits.waitGeneric(2000);
			selectSpecificPromotionByName(testDataJson, MSSPartnersPage_IR);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToPricing, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPricing);

			Validate.takeStepFullScreenShot("Form Details7", Status.INFO);
			
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueAfterPricing);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueAfterPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueAfterPricing);

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

			initiateMigartionDetailsSubmission(testDataJson, MSSPartnersPage_IR);

			enterCustomerInformation(testDataJson, MSSPartnersPage_IR);

			String SINNum = testDataJson.getString("SIN_NUMBER") + GenericUtils.generateRandomInteger(9999);

			enterSINNumber(MSSPartnersPage_IR, SINNum);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.consentCheckBox);

			Validate.takeStepFullScreenShot("Form Details2", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPreferenceBtn);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToPreferenceBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPreferenceBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.enterPIN);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.enterPIN);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterPIN, testDataJson.getString("PIN"));

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.enterEmailId);
			String emailId = GenericUtils.getRandomEmailId();

			// String emailId = "AUTO123@Telus.com";

			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterEmailId, emailId);

			Validate.takeStepFullScreenShot("Form Details3", Status.INFO);

			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToSubscriberInfoBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToSubscriberInfoBtn);

			BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.sameAsCustomerInfoCheckBox);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.sameAsCustomerInfoCheckBox);
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.submitButtonAferSubInforFilled);
			Validate.takeStepFullScreenShot("Entered Subscriber Information", Status.INFO);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitButtonAferSubInforFilled);

			//BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.newRatePlanDropDown, 60);

			selectNewRatePlan(testDataJson, MSSPartnersPage_IR);

			BaseSteps.Waits.waitGeneric(5000);
			Validate.takeStepFullScreenShot("Form Details8", Status.INFO);

			checkIfWarningPopupForSelectedOfferDisplayed(MSSPartnersPage_IR);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToAddOnBtn, 120);
			BaseSteps.Waits.waitGeneric(5000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToAddOnBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToAddOnBtn);

			Validate.takeStepFullScreenShot("Form Details9", Status.INFO);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToFinalPricing, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToFinalPricing);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToFinalPricing);

			Validate.takeStepFullScreenShot("Form Details10", Status.INFO);

			handleEasyRoamCheckBox(MSSPartnersPage_IR);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.finalContinueBtn);


			try {
				BaseSteps.Waits.waitGeneric(10000);
				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.doNotWantExtendedOfferRadioBtn, 60);
				executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.doNotWantExtendedOfferRadioBtn);

			}

			catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

			}

			Validate.takeStepFullScreenShot("Price Plans and Add On Details", Status.INFO);

			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.finalContinueBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.finalContinueBtn);
			
			
			Validate.takeStepFullScreenShot("Proceed to check out details", Status.INFO);
			
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.proceedToCheckoutBtn, 60);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.proceedToCheckoutBtn);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.proceedToCheckoutBtn);

			

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.orderConfirmationPage, 90);

			try {

				executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.doNotWantExtendedOfferRadioBtn);

				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.proceedToCheckoutBtn, 60);
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.proceedToCheckoutBtn);
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.proceedToCheckoutBtn);

				BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.saveAndShareBtn, 90);
				Validate.takeStepFullScreenShot("Checkout Page Details", Status.INFO);

				executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.mailServiceAgreementCopyRadioBtn);
				executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.finalAgreeCheckBox);
				executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.finalCreditCheckBox);

				Validate.takeStepFullScreenShot("MSS Detais - Before Final Submit", Status.INFO);

			} catch (Exception e) {
				Reporting.logReporter(Status.INFO, "Unable to submit the details" + e);
			}

			/*
			 * Need to check if authorized user check box is already checked or not
			 * 
			 */
			BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.finalSubmitButton);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.finalSubmitButton);
			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.finalSubmitButton);

			/*
			 * Final Verification Page
			 */

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.tnToBeActivated, 120);
			activatedTN = MSSPartnersPage_IR.getTNToBeActivatedText();
			Reporting.logReporter(Status.INFO, "TN to be activated: " + activatedTN);

			BaseSteps.Waits.waitGeneric(2000);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.statusOfActivationProcess);
			String workFlowStatus = MSSPartnersPage_IR.getActivationStatusText();
			Reporting.logReporter(Status.INFO, "Status of the Request: " + workFlowStatus);

			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.activatedAccountNumber, 30);
			BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.activatedAccountNumber);
			activatedBAN = MSSPartnersPage_IR.getActivatedAccountNumberText();
			Reporting.logReporter(Status.INFO, "BAN to be activated: " + activatedBAN);

			Validate.takeStepFullScreenShot("MSS Activation Page", Status.INFO);

			Validate.assertEquals(workFlowStatus, "Complete", "Unable to activate subscriber from MSS", false);

		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to Activate Subscriber from MSS Portal " + e);
			Validate.assertTrue(false, "Unable to perform PRE to POST Migration from MSS Portal");
		}

	}

	public static void selectDefaultLocation() {
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.selectLocationOutlet);

		// selecting default value at index 1
		BaseSteps.Dropdowns.selectByIndex(MSSPartnersPage_IR.selectLocationOutlet, 1);
		BaseSteps.Waits.waitGeneric(500);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.submitButtonSetLocationPage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitButtonSetLocationPage);

	}

	public static void navigateToPartnersPage(String carrier) {
		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.selectSpecificCarrierType(carrier));
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectSpecificCarrierType(carrier));

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

		MSSPartnersPage_IR MSSPartnersPage_IR = new MSSPartnersPage_IR();
		boolean actual = MSSPartnersPage_IR.isPartnersContentHomePageDisplayed();
		System.out.println("===========> MSS Partner Content");
		Validate.assertEquals(actual, true, "Unable to navigate to MSS Partners Content Page", false);
	}

	public static void navigateToModifyExistingAccountPage(MSSPartnersPage_IR MSSPartnersPage_IR) {
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.modifyExistingAccount, 30);
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.modifyExistingAccount);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.modifyExistingAccount);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.modifyExistingAccount);
		BaseSteps.Windows.switchToNewWindow();

		BaseSteps.JavaScripts.handleInvalidCertificateError();
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.partnersHomePage);

	}

	public static void provideIdentificationDetails(MSSPartnersPage_IR MSSPartnersPage_IR, String param, String value) {

		param = param.toUpperCase();
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

		switch (param) {

		case "POSTALCODE":
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.postalCoderadioBtn_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.postalCodeTextBox_p2p, value);
			break;

		case "LASTSIMDIGITS":
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.last6SimDigits_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.last6SimDigits_p2p, value);
			break;

		default:
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.pin_p2p);
			BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.pin_p2p, value);

		}
	}

	public static void selectRequiredOffer_NEW(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {

			String offerName = testDataJson.getString("MSS_OFFER_HEADER");
			BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.searchForSpecificConsumerOffer(offerName), 60);

			String programId = testDataJson.getString("MSS_OFFER_PROGRAM_ID");
			String offerType = testDataJson.getString("MSS_OFFER_NAME");

			try {

				BaseSteps.Waits.waitForElementToBeClickableLongWait(
						MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
				BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectSpecificOfferForActivation_NEW(offerType));
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

	public static void initiateMigartionDetailsSubmission(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {

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

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.migrationPageText, 60);
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();

		try {
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.consumerRegularRadioBtn_mpage);
		} catch (Exception e) {
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.consumerRegularRadioBtn_mpage);
			Reporting.logReporter(Status.INFO, "Consumer Regular option already Selected");
		}
		Validate.takeStepFullScreenShot("Form Details1", Status.INFO);

		//click continue to customer information button - continueToSutomerInfo_mpage
		
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueToSutomerInfo_mpage);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToSutomerInfo_mpage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToSutomerInfo_mpage);
		
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.photoIdDrpDwn, 60);
		BaseSteps.Dropdowns.selectByVisibleText(MSSPartnersPage_IR.photoIdDrpDwn, "BC Services Card");

	}

	public static void selectSpecificPromotionByName(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		try {
			// String programName = testDataJson.getString("PROMOTION_PROGRAM_ID");
			String programName = "ESS Reward Bill Credit EN";

			BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.searchForSpecificPromotionOfferByName_Migration(programName));
		} catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Unable to select" + e);
		}
	}

	public static void selectDeviceFromOffersPage(String deviceName, MSSPartnersPage_IR MSSPartnersPage_IR) {
		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_IR.selectADeviceBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectADeviceBtn);
		BaseSteps.Waits.waitForElementVisibilityLongWait(MSSPartnersPage_IR.searchDeviceTextBox);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.searchDeviceTextBox, Keys.chord(deviceName));
		BaseSteps.Waits.waitGeneric(2000);
		BaseSteps.Waits.waitForElementToBeClickable(MSSPartnersPage_IR.selectDeviceAfterSearch);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectDeviceAfterSearch);
		BaseSteps.Waits.waitGeneric(3000);
	}

	public static void selectIMEIFromOffersPage(String imei, MSSPartnersPage_IR MSSPartnersPage_IR) {
		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.imeiInputBox);
		BaseSteps.SendKeys.clearFieldAndSendKeys(MSSPartnersPage_IR.imeiInputBox, imei);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.imeiInputBox, Keys.TAB);
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.validIMEITickMark, 30);
	}

	public static void selectSIMNumberFromOffersPage(String sim, MSSPartnersPage_IR MSSPartnersPage_IR) {
		BaseSteps.SendKeys.clearFieldAndSendKeysUsingJS(MSSPartnersPage_IR.enterValidSIMNumber, sim);
		BaseSteps.Waits.waitGeneric(1000);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterValidSIMNumber, Keys.TAB);
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.validSimTickMark, 30);
	}

	public static void reSelectSIMAndIMEIWithDevice(String deviceName, String sim, String imei,
			MSSPartnersPage_IR MSSPartnersPage_IR) {

		selectIMEIFromOffersPage(imei, MSSPartnersPage_IR);

		selectSIMNumberFromOffersPage(sim, MSSPartnersPage_IR);

		selectDeviceFromOffersPage(deviceName, MSSPartnersPage_IR);
		
		selectIMEIFromOffersPage(imei, MSSPartnersPage_IR);
	}
	
	public static void productType(JSONObject testDataJson,MSSPartnersPage_IR MSSPartnersPage_IR) {
		BaseSteps.Waits.waitUntilPageLoadComplete();
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectAccountType(testDataJson.getString("ACCOUNT_TYPE")));
		Validate.takeStepFullScreenShot("Form Details1", Status.INFO);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitButton);
	}
	
	public static void setupGettingStarted(JSONObject testDataJson,MSSPartnersPage_IR MSSPartnersPage_IR) {
		BaseSteps.Waits.waitUntilPageLoadComplete();
		
		enterCustomerInformation(testDataJson, MSSPartnersPage_IR);
		
		String SINNum = testDataJson.getString("SIN_NUMBER") + GenericUtils.generateRandomInteger(9999);
		enterSINNumber(MSSPartnersPage_IR, SINNum);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.consentCheckBox);
		Validate.takeStepFullScreenShot("Form Details2", Status.INFO);
		
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
	}
	
	public static void selectPhoneNumber(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
		executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.choosePhoneNumberRadioBtn);
		BaseSteps.Waits.waitGeneric(2000);
		
		Select selectProvience = new Select(BaseSteps.Finds.findElement(By.xpath("//*[@id=\"introductionProvince\"]")));
		Select selectCity = new Select(BaseSteps.Finds.findElement(By.xpath("//*[@id=\"introductionCity\"]")));
		selectProvience.selectByVisibleText(testDataJson.getString("PROVINCE"));
		BaseSteps.Waits.waitGeneric(1000);
		selectCity.selectByVisibleText(testDataJson.getString("CUSTOMER_CITY").toLowerCase());
		
		BaseSteps.Waits.waitGeneric(3000);
		Validate.takeStepFullScreenShot("Select phone number", Status.INFO);
		
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToSelectPhoneNumberBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToSelectPhoneNumberBtn);
		BaseSteps.Waits.waitGeneric(3000);
		
		String newseriesnumber = testDataJson.getString("NEW_NUMBER_SERIES");
		if(!newseriesnumber.equals("")) {
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.selectNewSeriesNumber(newseriesnumber));
		BaseSteps.Waits.waitGeneric(7000);
		}
		else {
			WebDriver driver = WebDriverSession.getWebDriverSession();
			Select NumList = new Select(WebDriverSession.getWebDriverSession().findElement(By.xpath("//*[@id=\"select-phone-number-choose-areacode-areacode\"]")));
			for(int i =0; i < NumList.getOptions().size(); i++) {
				NumList.selectByValue(""+i);
				BaseSteps.Waits.waitGeneric(7000);
				try {
					if(driver.findElement(By.xpath("//*[@id=\"select-phone-number-choose-areacode-numbers-container\"]/div[1]/span")).getText().contains("Available phone numbers")) {
						break;
					}
				}
				catch(Exception e) {
					BaseSteps.Debugs.scrollToElement(WebDriverSession.getWebDriverSession().findElement(By.xpath("//*[@id=\"select-phone-number-choose-areacode-areacode\"]")));
					continue; 
				}
			}
		}
		Validate.takeStepFullScreenShot("Select phone number", Status.INFO);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.submitBtnOnPhoneNumSelectionPage);
		Validate.takeStepFullScreenShot("Selected Number", Status.INFO);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.submitBtnOnPhoneNumSelectionPage);
	}
	public static void OffersPromo(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR,String SIM) {
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.selectADeviceBtn, 60);
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
		executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.simOnlyActivationChkBox);
		BaseSteps.Waits.waitGeneric(5000);
		
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterValidSIMNumber, SIM);
		BaseSteps.SendKeys.sendKey(MSSPartnersPage_IR.enterValidSIMNumber,Keys.TAB);
		try{
			BaseSteps.Waits.waitForElementInvisibilityLongWait(WebDriverSession.getWebDriverSession().findElement(By.xpath("//*[@id=\"introduction-options-device-sim-checkmark-icon\"]/span")));
			BaseSteps.Waits.waitGeneric(10000);
			BaseSteps.Waits.waitForElementInvisibility(MSSPartnersPage_IR.deviceTypeDropDownOfferTypeSmartphoneOption);
		}
		catch(Exception e) {}
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.deviceTypeDropDownOfferTypeSmartphoneOption);
		BaseSteps.Waits.waitGeneric(5000);

		Validate.takeStepFullScreenShot("SIM,IMEI,DEVICE Information", Status.INFO);
		BaseSteps.Waits.waitGeneric(5000);

		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToOffersPage);
		
		BaseSteps.Waits.waitGeneric(5000);
		
		BaseSteps.Waits.waitGeneric(2000);
        selectRequiredOffer_NEW(testDataJson, MSSPartnersPage_IR);
        
        Validate.takeStepFullScreenShot("Form Details5", Status.INFO);

		BaseSteps.Waits.waitGeneric(5000);
		
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPromotionsPage);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPromotionsPage);

		declinePerkOfferIdIfDisplayed(MSSPartnersPage_IR);

		selectDefaultPromotions(testDataJson, MSSPartnersPage_IR);

		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.continueToPricing, 60);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueToPricing);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueToPricing);

		Validate.takeStepFullScreenShot("Form Details7", Status.INFO);

		BaseSteps.Waits.waitForElementToBeClickableLongWait(MSSPartnersPage_IR.continueAfterPricing);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.continueAfterPricing);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.continueAfterPricing);
	}
	
	public static void PricePlanAddOn(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
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
	}
	
	public static void OrderConfirmation(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverSession.getWebDriverSession();
		try {
			BaseSteps.Waits.waitUntilPageLoadComplete();
			//BaseSteps.Waits.waitForElementVisibilityLonfWait(MSSPartnersPage_IR.orderConfirmationPage, 60);
			//BaseSteps.Waits.waitForElementInvisibilityLongWait(MSSPartnersPage_IR.orderConfirmationPage);
			BaseSteps.Waits.waitGeneric(10000);
			BaseSteps.Debugs.scrollToElement(WebDriverSession.getWebDriverSession().findElement(By.xpath("//*[@id=\"offer-hardware-content\"]/div[3]/div[3]/div[3]/div[1]/div/span")));
			executor.executeScript("arguments[0].click();", MSSPartnersPage_IR.doNotWantExtendedOfferRadioBtn);

		}catch (Exception e) {
			Reporting.logReporter(Status.INFO, "Extended Warranty Options Not Displayed" + e);

		}
		
		BaseSteps.Waits.waitForElementVisibility(MSSPartnersPage_IR.proceedToCheckoutBtn, 60);
		BaseSteps.Debugs.scrollToElement(MSSPartnersPage_IR.proceedToCheckoutBtn);
		BaseSteps.Clicks.clickElement(MSSPartnersPage_IR.proceedToCheckoutBtn);
	}
	
	public static void Checkout(JSONObject testDataJson, MSSPartnersPage_IR MSSPartnersPage_IR) {
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
	}
	
}
