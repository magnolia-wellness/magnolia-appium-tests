package com.magnoliacollectivewellness.appium.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Handles the signup steps (Step 1 → Step 3).
 */
public class SignupFlowPage {
    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public SignupFlowPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillStep1(String firstName, String lastName) {
        List<WebElement> editTexts = driver.findElements(AppiumBy.className("android.widget.EditText"));
        if (editTexts.size() >= 2) {
            editTexts.get(0).click();
            editTexts.get(0).clear();
            editTexts.get(0).sendKeys(firstName);
            editTexts.get(1).click();
            editTexts.get(1).clear();
            editTexts.get(1).sendKeys(lastName);
        }
        clickContinue();
    }

    public void completeStep2() {
        selectFirstOption();
        clickContinue();
    }

    public void completeStep3() {
        selectFirstOption();
        clickContinue();
    }

    public void selectPreferredPronoun(String pronoun) {
        try {
            WebElement pronounButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@text='" + pronoun + "']")));
            pronounButton.click();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("⚠️ Could not select pronoun '" + pronoun + "': " + e.getMessage());
        }
    }

    public void selectCountry(String country) {
        try {
            WebElement dropdownField = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.EditText[contains(@text,'Select Country') or contains(@hint,'Select country') or contains(@text,'Select country')]")));
            dropdownField.click();
            Thread.sleep(1000);
            WebElement countryOption = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.TextView[contains(@text,'" + country + "')]")));
            countryOption.click();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("⚠️ Could not select country '" + country + "': " + e.getMessage());
        }
    }

    private void selectFirstOption() {
        try {
            List<WebElement> clickableViews = driver.findElements(
                AppiumBy.xpath("//android.view.ViewGroup[@clickable='true']"));
            for (WebElement view : clickableViews) {
                if (view.isDisplayed()) {
                    view.click();
                    Thread.sleep(500);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not select signup option: " + e.getMessage());
        }
    }

    private void clickContinue() {
        try {
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//*[(contains(@text,'Continue') or contains(@text,'CONTINUE')) and @clickable='true']")));
            continueBtn.click();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("⚠️ Continue button not found on signup screen: " + e.getMessage());
        }
    }
}

